package fr.instalite.dataloader;

import com.github.javafaker.Faker;

import fr.instalite.common.EVisibility;
import fr.instalite.common.FileUtil;
import fr.instalite.common.RandomIDGenerator;
import fr.instalite.dataloader.util.DataLoaderUtil;
import fr.instalite.dataloader.util.FractalGenerator;
import fr.instalite.media.image.ImageEntity;
import fr.instalite.media.image.ImageException;
import fr.instalite.media.image.ImageRepository;
import fr.instalite.token.TokenEntity;
import fr.instalite.token.TokenRepository;
import fr.instalite.token.TokenType;
import fr.instalite.user.ERole;
import fr.instalite.user.UserEntity;
import fr.instalite.user.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements Serializable {

	private static final long serialVersionUID = 1L;

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ImageRepository imageRepository;
	private final TokenRepository tokenRepository;
	private final Environment environment;
	private final RandomIDGenerator randomIDGenerator;
	private final Path FOLDER_PATH = Paths.get(
			"src/main/resources/static/images").toAbsolutePath();
	private static final String DEFAULT_EXTENSION = "png";

	public DataLoader(
			@Autowired Environment environment,
			@Autowired UserRepository userRepository, @Autowired PasswordEncoder passwordEncoder,
			@Autowired ImageRepository imageRepository, @Autowired TokenRepository tokenRepository,
			@Autowired RandomIDGenerator randomIDGenerator) {
		this.environment = environment;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.imageRepository = imageRepository;
		this.tokenRepository = tokenRepository;
		this.randomIDGenerator = randomIDGenerator;
	}

	@PostConstruct
	public void loadData() {
		this.loadUsers();
		this.loadImages();
	}

	@PreDestroy
	public void removeData() {
		tokenRepository.deleteAll();
		userRepository.deleteAll();
		imageRepository.deleteAll();
		FileUtil.deleteAllFiles(FOLDER_PATH);
	}

	private void loadUsers() {
		final Faker faker = new Faker(new Locale("fr"));
		final List<UserEntity> users = new ArrayList<>();
		final int numberOfUsers = 5;
		UserEntity user = null;
		for (int i = 0; i < numberOfUsers; i++) {
			user = new UserEntity();
			user.setPublicId(randomIDGenerator.generateRandomUUID());
			user.setFirstname(faker.name().firstName());
			user.setLastname(faker.name().lastName());
			user.setEmail(faker.internet().emailAddress());
			user.setPassword(passwordEncoder.encode(faker.internet().password()));
			users.add(user);
		}
		userRepository.saveAll(users);
		// DataLoaderUser.this.createTestUsers();
	}

	private void loadImages() {
		FileUtil.deleteAllFiles(FOLDER_PATH);
		String imagesFolder = this.environment.getProperty("image.upload.path");
		final Path imagesFolderPath = Paths.get(imagesFolder);
		// Vérifie si le répertoire existe, sinon le crée
		if (!Files.exists(imagesFolderPath)) {
			try {
				Files.createDirectories(imagesFolderPath);
			} catch (IOException e) {
				throw new ImageException(
						"Une erreur est survenue pendant la création du répertoire",
						String.format(
								"Impossible de créer le répertoire : %s",
								imagesFolderPath.toString()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		final Faker faker = new Faker(new Locale("fr"));
		final List<ImageEntity> images = new ArrayList<>();
		final int numberOfImages = 5;
		final EVisibility[] possibleVisibilities = EVisibility.values();
		final Random random = new Random();
		String fileName = "";
		Path filePath = null;
		ImageEntity image = null;
		for (int i = 0; i < numberOfImages; i++) {
			image = new ImageEntity();
			image.setPublicId(randomIDGenerator.generateRandomUUID());
			image.setPublishedAt(Instant.now());
			fileName = image.getPublicId() + "." + DEFAULT_EXTENSION;
			filePath = imagesFolderPath.resolve(fileName);
			image.setFilePath(filePath.toString());
			image.setTitle(DataLoaderUtil.generateRandomText(faker, 1, 32));
			image.setDescription(DataLoaderUtil.generateRandomText(faker, 0, 128));
			image.setVisibility(
					possibleVisibilities[random.nextInt(possibleVisibilities.length)]);
			image.setAuthor(this.getRandomUser(userRepository.findAll()));
			images.add(image);
		}
		imageRepository.saveAll(images);
		FileUtil.createFolderIfNotExists(FOLDER_PATH);
		for (ImageEntity imageToGenerate : images) {
			generateAndSaveImage(imageToGenerate);
		}
	}

	private void loadTokens() {
		final Faker faker = new Faker(new Locale("fr"));
		final List<TokenEntity> tokens = new ArrayList<>();
		final int numberOfTokens = 5;
		TokenEntity token = null;
		for (int i = 0; i < numberOfTokens; i++) {
			token = new TokenEntity();
			token.setPublicId(randomIDGenerator.generateRandomUUID());
			token.setCreatedAt(Instant.now());
			token.setUpdatedAt(Instant.now());
			token.setToken(faker.internet().password());
			token.setTokenType(TokenType.BEARER);
			token.setRevoked(false);
			token.setExpired(false);
			// Assurez-vous que l'utilisateur est défini correctement selon vos besoins
			// token.setUser(userEntity);
			tokens.add(token);
		}
		tokenRepository.saveAll(tokens);
	}

	private void generateAndSaveImage(ImageEntity image) {
		final File file = new File(String.format("%s", image.getFilePath()));
		final Random random = new Random();
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = random.nextBoolean() ? generateRandomPixelBufferedImage()
					: new FractalGenerator().generateFractalBufferedImage();
			ImageIO.write(bufferedImage, DEFAULT_EXTENSION, file);
		} catch (IOException e) {
			System.err.println("Error: " + e);
		}
	}

	/**
	 * Source :
	 * https://dyclassroom.com/image-processing-project/how-to-create-a-random-pixel-image-in-java
	 */
	private static BufferedImage generateRandomPixelBufferedImage() {
		int width = 640;
		int height = 320;
		final BufferedImage image = new BufferedImage(
				width,
				height,
				BufferedImage.TYPE_INT_ARGB);
		// create random image pixel by pixel
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int a = (int) (Math.random() * 256); // alpha
				int r = (int) (Math.random() * 256); // red
				int g = (int) (Math.random() * 256); // green
				int b = (int) (Math.random() * 256); // blue
				int p = (a << 24) | (r << 16) | (g << 8) | b; // pixel
				image.setRGB(x, y, p);
			}
		}
		return image;
	}

	private UserEntity getRandomUser(List<UserEntity> users) {
		if (users.isEmpty()) {
			return null;
		}
		final Random random = new Random();
		return users.get(random.nextInt(users.size()));
	}

	private void createTestUsers() {
		// Création de l'utilisateur admin de test
		final UserEntity admin = new UserEntity();
		admin.setPublicId(randomIDGenerator.generateRandomUUID());
		admin.setFirstname("Admin");
		admin.setLastname("Admin");
		admin.setEmail("admin@mail.com");
		admin.setPassword(passwordEncoder.encode("Admin123456$"));
		admin.setRole(ERole.ADMIN);
		userRepository.save(admin);
		// Création de l'utilisateur de test
		final UserEntity user = new UserEntity();
		user.setPublicId(randomIDGenerator.generateRandomUUID());
		user.setFirstname("User");
		user.setLastname("User");
		user.setEmail("user@mail.com");
		user.setPassword(passwordEncoder.encode("User123456$"));
		user.setRole(ERole.USER);
		userRepository.save(user);
	}

}
