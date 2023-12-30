package fr.instalite.media.image;

import fr.instalite.common.EVisibility;
import fr.instalite.common.FileUtil;
import fr.instalite.common.RandomIDGenerator;
import fr.instalite.user.ERole;
import fr.instalite.user.UserEntity;
import fr.instalite.user.UserRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService implements Serializable {

	private static final long serialVersionUID = 1L;

	private final ImageRepository imageRepository;
	private final UserRepository userRepository;
	private String imagesFolder;
	private final Path imagesFolderPath;
	private final ImageMapper imageMapper;
	private final Environment environment;
	private final RandomIDGenerator randomIDGenerator;

	public ImageService(
			@Autowired ImageRepository imageRepository,
			@Autowired UserRepository userRepository,
			@Autowired ImageMapper imageMapper,
			@Autowired Environment environment,
			@Autowired RandomIDGenerator randomIDGenerator) {
		this.imageRepository = imageRepository;
		this.userRepository = userRepository;
		this.randomIDGenerator = randomIDGenerator;
		this.environment = environment;
		this.imagesFolder = this.environment.getProperty("image.upload.path");
		this.imagesFolderPath = Paths.get(this.imagesFolder);
		FileUtil.createFolderIfNotExists(this.imagesFolderPath);
		this.imageMapper = imageMapper;
	}

	private String getBaseImageUrl() {
		final String port = this.environment.getProperty("server.port");
		final String address = this.environment.getProperty("server.address");
		final String contextPath = this.environment.getProperty("server.servlet.context-path");
		final String imageUrl = "http://" + address + ":" + port + contextPath + "images/";
		return imageUrl;
	}

	/**
	 * 
	 * @param filename : Le nom du fichier avec l'extension (ex: fichier.txt)
	 * @return
	 */
	private String getImageUrl(String filename) {
		filename = filename == null ? "" : filename;
		return this.getBaseImageUrl() + filename;
	}

	public List<ImageResponse> getAllImages() {
		return imageMapper.toImageResponseList(imageRepository.findAll());
	}

	public Page<ImageResponse> getAllImagesPaginated(Authentication authentication, Pageable pageable) {
		Page<ImageEntity> imagePage = null;
		if (authentication != null && authentication.isAuthenticated()) {
			final UserEntity userEntity = userRepository.findByEmail(authentication.getName())
					.orElseThrow(() -> new UsernameNotFoundException(
							String.format("L'utilisateur avec l'email %s n'a pas été trouvé.",
									authentication.getName())));
			if (userEntity.getRole() == ERole.ADMIN) {
				imagePage = imageRepository.findAll(pageable);
			} else if (userEntity.getRole() == ERole.USER) {
				final List<EVisibility> allowedVisibilities = Arrays.asList(EVisibility.PUBLIC);
				imagePage = imageRepository.findByAuthorEmailOrVisibilityIn(pageable, userEntity.getEmail(),
						allowedVisibilities);
			}
			List<ImageEntity> filteredImages = imagePage.getContent();
			Path filePath;
			for (ImageEntity imageEntity : filteredImages) {
				filePath = Paths.get(imageEntity.getFilePath());
				imageEntity.setFileUrl(this.getImageUrl(filePath.getFileName().toString()));
			}
			return new PageImpl<>(imageMapper.toImageResponseList(filteredImages), pageable,
					imagePage.getTotalElements());
		}
		final List<EVisibility> allowedVisibilities = Arrays.asList(EVisibility.PUBLIC);
		imagePage = imageRepository.findByVisibilityIn(pageable, allowedVisibilities);
		List<ImageEntity> filteredImages = imagePage.getContent();
		Path filePath;
		for (ImageEntity imageEntity : filteredImages) {
			filePath = Paths.get(imageEntity.getFilePath());
			imageEntity.setFileUrl(this.getImageUrl(filePath.getFileName().toString()));
		}
		return new PageImpl<>(imageMapper.toImageResponseList(filteredImages), pageable, imagePage.getTotalElements());
	}
   
	public ImageResponse getImageById(Long id) {
		final ImageEntity imageEntity = imageRepository
				.findById(id)
				.orElseThrow(() -> new ImageNotAcceptedException(
						"Image not found",
						String.format("L'image avec l'id %d n'a pas été trouvé.", id),
						HttpStatus.NOT_FOUND));
		final Path filePath = Paths.get(imageEntity.getFilePath());
		imageEntity.setFileUrl(this.getImageUrl(filePath.getFileName().toString()));
		return imageMapper.toImageResponse(imageEntity);
	}

	public ImageResponse getImageByPublicId(String publicId) {
		final ImageEntity imageEntity = imageRepository
				.findByPublicId(publicId)
				.orElseThrow(() -> new ImageNotFoundException(
						"Image not found",
						String.format("L'image avec l'identifiant publique %s n'a pas été trouvé.", publicId),
						HttpStatus.NOT_FOUND));
		final Path filePath = Paths.get(imageEntity.getFilePath());
		imageEntity.setFileUrl(this.getImageUrl(filePath.getFileName().toString()));
		return imageMapper.toImageResponse(imageEntity);
	}

	@Transactional
	public ImageResponse createImage(ImageCreateRequest imageCreateRequest) {
		final String authorPublicId = imageCreateRequest.getAuthor() != null
				? imageCreateRequest.getAuthor().getPublicId()
				: null;
		final UserEntity author = userRepository
				.findByPublicId(authorPublicId)
				.orElseThrow(() -> new UsernameNotFoundException(
						String.format(
								"L'utilisateur avec l'identifiant publique %s n'a pas été trouvé.",
								authorPublicId)));
		final MultipartFile imageFile = imageCreateRequest.getFile();
		final String originalFilename = imageCreateRequest
				.getFile()
				.getOriginalFilename();
		final String imageExtension = StringUtils.getFilenameExtension(
				originalFilename);
		if (EImageType.fromValueIgnoreCase(imageExtension) == null) {
			final List<String> allowedExtensionNames = new ArrayList<>();
			for (EImageType eImageType : EImageType.values()) {
				allowedExtensionNames.add(eImageType.name());
			}
			throw new ImageNotAcceptedException(
					"Image not accepted",
					String.format(
							"Extension de fichier %s non valide pour le fichier %s. Les extensions acceptées sont : %s",
							imageExtension,
							originalFilename,
							String.join(", ", allowedExtensionNames)),
					HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		}
		final Instant currentInstant = Instant.now();
		final String publicId = randomIDGenerator.generateStringId(
				RandomIDGenerator.getDefaultLength());
		final String customFilename = publicId + "." + imageExtension;
		EVisibility visibility = EVisibility.fromValue(
				imageCreateRequest.getVisibility());
		visibility = visibility != null ? visibility : EVisibility.PRIVATE;
		final ImageEntity imageEntity = new ImageEntity(
				0L,
				currentInstant,
				currentInstant,
				publicId,
				currentInstant,
				customFilename,
				imageFile,
				"",
				imageCreateRequest.getTitle(),
				imageCreateRequest.getDescription(),
				visibility,
				author);
		imageEntity.setPublicId(publicId);
		imageEntity.setCreatedAt(currentInstant);
		imageEntity.setUpdatedAt(currentInstant);
		imageEntity.setPublishedAt(currentInstant);
		// Vérifie si le répertoire existe, sinon le crée
		if (!Files.exists(this.imagesFolderPath)) {
			try {
				Files.createDirectories(this.imagesFolderPath);
			} catch (IOException e) {
				throw new ImageException(
						"Error while creation of directory",
						String.format(
								"Impossible de créer le répertoire : %s",
								imagesFolderPath.toString()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		try {
			// Enregistre le fichier image sur le serveur avec un nom de fichier
			// personnalisé
			final Path filePath = this.imagesFolderPath.resolve(customFilename);
			Files.copy(
					imageFile.getInputStream(),
					filePath);
			imageEntity.setFilePath(filePath.toString());
		} catch (FileAlreadyExistsException e) {
			throw new ImageException(
					"Image already exists",
					String.format(
							"L'image avec le nom de fichier %s existe déjà.",
							customFilename),
					HttpStatus.CONFLICT);
		} catch (IOException e) {
			throw new ImageException(
					"Erreur lors de l'enregistrement",
					String.format(
							"Erreur lors de l'enregistrement du fichier dans : %s",
							imagesFolderPath.toString()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// Enregistre en base de données
		final ImageEntity imageSaved = imageRepository.save(imageEntity);
		final ImageResponse imageResponse = imageMapper.toImageResponse(imageSaved);
		imageResponse.setFileUrl(this.getImageUrl(customFilename));
		return imageResponse;
	}

	@Transactional
	public ImageResponse updateImageByPublicId(String publicId, ImageUpdateRequest imageUpdateRequest) {
		final ImageEntity imageToUpdate = imageRepository
				.findByPublicId(publicId)
				.orElseThrow(() -> new ImageNotAcceptedException(
						"Image not found",
						String.format("L'image avec l'id %s n'a pas été trouvé.", publicId),
						HttpStatus.NOT_FOUND));
		final String authorPublicId = imageUpdateRequest.getAuthor() != null
				? imageUpdateRequest.getAuthor().getPublicId()
				: null;
		final UserEntity author = userRepository
				.findByPublicId(authorPublicId)
				.orElseThrow(() -> new UsernameNotFoundException(
						String.format(
								"L'utilisateur avec l'identifiant publique %s n'a pas été trouvé.",
								authorPublicId)));
		final MultipartFile imageFile = imageUpdateRequest.getFile();
		final String originalFilename = imageUpdateRequest
				.getFile()
				.getOriginalFilename();
		final String imageExtension = StringUtils.getFilenameExtension(
				originalFilename);
		if (EImageType.fromValueIgnoreCase(imageExtension) == null) {
			final List<String> allowedExtensionNames = new ArrayList<>();
			for (EImageType eImageType : EImageType.values()) {
				allowedExtensionNames.add(eImageType.name());
			}
			throw new ImageNotAcceptedException(
					"Image not accepted",
					String.format(
							"Extension de fichier %s non valide pour le fichier %s. Les extensions acceptées sont : %s",
							imageExtension,
							originalFilename,
							String.join(", ", allowedExtensionNames)),
					HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		}
		final String oldFilePath = imageToUpdate.getFilePath();
		final Instant currentInstant = Instant.now();
		final String customFilename = publicId + "." + imageExtension;
		EVisibility visibility = EVisibility.fromValue(
				imageUpdateRequest.getVisibility());
		visibility = visibility != null ? visibility : EVisibility.PRIVATE;
		imageToUpdate.setUpdatedAt(currentInstant);
		imageToUpdate.setUpdatedAt(currentInstant);
		imageToUpdate.setFile(imageFile);
		imageToUpdate.setTitle(imageUpdateRequest.getTitle());
		imageToUpdate.setDescription(imageUpdateRequest.getDescription());
		imageToUpdate.setVisibility(visibility);
		imageToUpdate.setAuthor(author);
		// Vérifie si le répertoire existe, sinon le crée
		if (!Files.exists(this.imagesFolderPath)) {
			try {
				Files.createDirectories(this.imagesFolderPath);
			} catch (IOException e) {
				throw new ImageException(
						"Error while creation of directory",
						String.format(
								"Impossible de créer le répertoire : %s",
								imagesFolderPath.toString()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		try {
			// Supprime l'ancien fichier
			Files.delete(Paths.get(oldFilePath));
			// Enregistre le fichier image sur le serveur avec un nom de fichier
			// personnalisé
			final Path filePath = this.imagesFolderPath.resolve(customFilename);
			Files.copy(
					imageFile.getInputStream(),
					filePath);
			imageToUpdate.setFilePath(filePath.toString());
		} catch (FileAlreadyExistsException e) {
			throw new ImageException(
					"Image already exists",
					String.format(
							"L'image avec le nom de fichier %s existe déjà.",
							customFilename),
					HttpStatus.CONFLICT);
		} catch (IOException e) {
			throw new ImageException(
					"Erreur lors de l'enregistrement",
					String.format(
							"Erreur lors de l'enregistrement du fichier dans : %s",
							imagesFolderPath.toString()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// Enregistre la mis à jour en base de données
		final ImageEntity imageUpdated = imageRepository.save(imageToUpdate);
		final ImageResponse imageResponse = imageMapper.toImageResponse(imageUpdated);
		imageResponse.setFileUrl(this.getImageUrl(customFilename));
		return imageResponse;
	}

	@Transactional
	public void deleteImageByPublicId(String publicId) {
		final ImageEntity existingImage = imageRepository
				.findByPublicId(publicId)
				.orElseThrow(() -> new ImageNotFoundException(
						"Image not found",
						String.format(
								"L'image avec l'identifnat publique %s n'a pas été trouvé.",
								publicId),
						HttpStatus.NOT_FOUND));
		imageRepository.deleteByPublicId(existingImage.getPublicId());
		this.deleteImageInFolder(existingImage);
	}

	private void deleteImageInFolder(ImageEntity imageEntity) {
		try {
			final Path imagePathToDelete = Paths.get(
					imageEntity.getFilePath());
			Files.delete(imagePathToDelete);
		} catch (Exception e) {
			throw new ImageException(
					"Error while deleting image",
					String.format(
							"Une erreur est survenue lors de la suppression de l'image %s",
							imageEntity.getFilePath()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
