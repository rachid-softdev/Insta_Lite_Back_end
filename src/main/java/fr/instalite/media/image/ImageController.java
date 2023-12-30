package fr.instalite.media.image;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Source Upload Multipart :
 * https://stackoverflow.com/questions/21329426/spring-mvc-multipart-request-with-json/25183266#25183266
 */

@Tag(name = "Gestion des images", description = "Opérations pour gérer les images")
@RestController
@RequestMapping("/api/insta_lite/images")
public class ImageController implements Serializable {

	private static final long serialVersionUID = 1L;
	private final ImageService imageService;

	public ImageController(@Autowired ImageService imageService) {
		this.imageService = imageService;
	}

	@Operation(summary = "Récupérer une image par son identifiant", description = "Récupère une image par son identifiant.", tags = {
			"jeton", "get" }, responses = {
					@ApiResponse(responseCode = "200", content = {
							@Content(schema = @Schema(implementation = ImageResponse.class), mediaType = "application/json") }),
					@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
					@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
			})
	@GetMapping("/all/{id}")
	public ResponseEntity<ImageResponse> getImageById(@PathVariable(name = "id") Long id) {
		final ImageResponse imageResponse = imageService.getImageById(id);
		return ResponseEntity.status(HttpStatus.OK).body(imageResponse);
	}

	@Operation(summary = "Récupérer une image par son identifiant publique", description = "Récupère une image par son identifiant publique.", tags = {
			"images", "get" }, responses = {
					@ApiResponse(responseCode = "200", content = {
							@Content(schema = @Schema(implementation = ImageResponse.class), mediaType = "application/json") }),
					@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
					@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
			})
	@GetMapping("/{public_id}")
	public ResponseEntity<ImageResponse> getImageByPublicId(@PathVariable(name = "public_id") String publicId) {
		final ImageResponse imageResponse = imageService.getImageByPublicId(publicId);
		return ResponseEntity.status(HttpStatus.OK).body(imageResponse);
	}

	@Operation(summary = "Récupérer tous les images", description = "Récupère la liste de tous les images.", tags = {
			"images", "get" }, responses = {
					@ApiResponse(responseCode = "200", content = {
							@Content(schema = @Schema(implementation = ImageResponse.class), mediaType = "application/json") }),
					@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
			})
	@GetMapping({ "/all" })
	public ResponseEntity<List<ImageResponse>> getAllImages() {
		List<ImageResponse> imageResponses = imageService.getAllImages();
		return ResponseEntity.status(HttpStatus.OK).body(imageResponses);
	}

	@Operation(summary = "Récupérer tous les images", description = "Récupère la liste de tous les images paginées.", tags = {
			"images", "get" }, responses = {
					@ApiResponse(responseCode = "200", content = {
							@Content(schema = @Schema(implementation = ImageResponse.class), mediaType = "application/json") }),
					@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
			})
	@GetMapping({ "" })
	public ResponseEntity<Page<ImageResponse>> getAllImagesPaginated(
			@RequestParam(name = "sortBy", required = false, defaultValue = "createdAt") @Size(min = 1, max = 50, message = "Le paramètre Sort by parameter doit contenir entre 1 and 50 caractères.") String sortBy,
			@RequestParam(name = "sortOrder", required = false, defaultValue = "asc") @Pattern(regexp = "^(asc|desc)$", message = "Le paramètre Sort order doit être 'asc' ou 'desc'.") String sortOrder,
			Authentication authentication,
			Pageable pageable) {
		final Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		final Page<ImageResponse> imageResponses = imageService.getAllImagesPaginated(authentication, pageable);
		return ResponseEntity.status(HttpStatus.OK).body(imageResponses);
	}

	@Operation(summary = "Créer une image", description = "Crée une nouvelle image.", tags = { "images",
			"post" }, responses = {
					@ApiResponse(responseCode = "201", content = {
							@Content(schema = @Schema(implementation = ImageResponse.class), mediaType = "application/json") }),
					@ApiResponse(responseCode = "404", content = {
							@Content(schema = @Schema(implementation = ImageNotFoundException.class)) }),
					@ApiResponse(responseCode = "415", content = {
							@Content(schema = @Schema(implementation = ImageNotAcceptedException.class)) }),
					@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
			})
	@PostMapping(value = { "" })
	public ResponseEntity<ImageResponse> createImage(
			@RequestPart(name = "imageCreateRequest", required = true) @Valid ImageCreateRequest imageCreateRequest,
			@RequestPart(name = "file", required = true) @Valid @NotNull @NotBlank MultipartFile file,
			Authentication authentication) {
		imageCreateRequest.setFile(file);
		final ImageResponse imageResponse = imageService.createImage(imageCreateRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(imageResponse);
	}

	@Operation(summary = "Mettre à jour une image grâce à son identifiant publique", description = "Met à jour les informations d'une image grâce à son identifiant publique.", tags = {
			"images", "put" }, responses = {
					@ApiResponse(responseCode = "204", content = {
							@Content(schema = @Schema(implementation = ImageResponse.class), mediaType = "application/json")
					}),
					@ApiResponse(responseCode = "404", content = {
							@Content(schema = @Schema(implementation = ImageNotFoundException.class)) }),
					@ApiResponse(responseCode = "415", content = {
							@Content(schema = @Schema(implementation = ImageNotAcceptedException.class)) }),
					@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
			})
	@PutMapping(value = { "/{public_id}" })
	public ResponseEntity<ImageResponse> updateImageByPublicId(@PathVariable("public_id") String publicId,
			@RequestPart(name = "imageUpdateRequest", required = true) @Valid ImageUpdateRequest imageUpdateRequest,
			@RequestPart(name = "file", required = true) @Valid @NotNull @NotBlank MultipartFile file) {
		imageUpdateRequest.setFile(file);
		final ImageResponse updatedImageResponse = imageService.updateImageByPublicId(publicId, imageUpdateRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(updatedImageResponse);
	}

	@Operation(summary = "Supprimer une image grâce à son identifiant publique", description = "Supprime un image grâce à son identifiant publique.", tags = {
			"images", "delete" }, responses = {
					@ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) }),
					@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
					@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
			})
	@DeleteMapping("/{public_id}")
	public ResponseEntity<Void> deleteImageByPublicId(@PathVariable(name = "public_id") String publicId) {
		imageService.deleteImageByPublicId(publicId);
		return ResponseEntity.noContent().build();
	}

}
