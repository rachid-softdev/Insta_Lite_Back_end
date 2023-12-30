package fr.instalite.user;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Gestion des utilisateurs", description = "Opérations pour gérer les utilisateurs")
@RestController
@RequestMapping("/api/insta_lite/users")
public class UserController implements Serializable {

	private static final long serialVersionUID = 1L;
	private final UserService userService;
	private final UserMapper userMapper;

	public UserController(@Autowired UserService userService, @Autowired UserMapper userMapper) {
		this.userService = userService;
		this.userMapper = userMapper;
	}

	@Operation(summary = "Récupérer un utilisateur par ID", description = "Récupère un utilisateur par son ID.", tags = {
			"utilisateurs", "get" }, responses = {
					@ApiResponse(responseCode = "200", content = {
							@Content(schema = @Schema(implementation = BaseUserResponse.class), mediaType = "application/json") }),
					@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
					@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
	@GetMapping("/all/{id}")
	public ResponseEntity<BaseUserResponse> getUserById(@PathVariable(name = "id") Long id) {
		final UserEntity userEntity = userService.getUserById(id);
		if (userEntity == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(userMapper.toBaseUserResponse(userEntity));
	}

	@Operation(summary = "Récupérer un utilisateur par ID", description = "Récupère un utilisateur par son ID.", tags = {
			"utilisateurs", "get" }, responses = {
					@ApiResponse(responseCode = "200", content = {
							@Content(schema = @Schema(implementation = BaseUserResponse.class), mediaType = "application/json") }),
					@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
					@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
			})
	@GetMapping("/{public_id}")
	public ResponseEntity<BaseUserResponse> getUserByPublicId(@PathVariable(name = "public_id") String publicId) {
		final UserEntity userEntity = userService.getUserByPublicId(publicId);
		if (userEntity == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(userMapper.toBaseUserResponse(userEntity));
	}

	@Operation(summary = "Récupérer tous les utilisateurs", description = "Récupère la liste de tous les utilisateurs.", tags = {
			"utilisateurs", "get" }, responses = {
					@ApiResponse(responseCode = "200", content = {
							@Content(schema = @Schema(implementation = BaseUserResponse.class), mediaType = "application/json") }),
					@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
			})
	@GetMapping({ "/all" })
	public ResponseEntity<List<BaseUserResponse>> getAllUsers() {
		List<UserEntity> userEntities = userService.getAllUsers();
		List<BaseUserResponse> userResponses = userEntities.stream()
				.map(userEntity -> userMapper.toBaseUserResponse(userEntity))
				.collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(userResponses);
	}

	@Operation(summary = "Récupère tous les utilisateurs avec pagination", description = "Récupère une liste de tous les utilisateurs avec pagination.", tags = {
			"users", "get" }, responses = {
					@ApiResponse(responseCode = "200", content = {
							@Content(schema = @Schema(implementation = BaseUserResponse.class), mediaType = "application/json") }),
					@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
					@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
			})
	@GetMapping({ "", "/" })
	public ResponseEntity<Page<BaseUserResponse>> getAllUsersPaginated(
			Pageable pageable,
			@RequestParam(name = "sortBy", required = false, defaultValue = "createdAt") @Size(min = 1, max = 50, message = "Le paramètre Sort by parameter doit contenir entre 1 and 50 caractères.") String sortBy,
			@RequestParam(name = "sortOrder", required = false, defaultValue = "asc") @Pattern(regexp = "^(asc|desc)$", message = "Le paramètre Sort order doit être 'asc' ou 'desc'.") String sortOrder) {
		final Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		final Page<UserEntity> usersEntities = userService.getAllUsers(pageable);
		Page<BaseUserResponse> usersResponses = usersEntities
				.map(userMapper::toBaseUserResponse);
		return ResponseEntity.status(HttpStatus.OK).body(usersResponses);
	}

	@Operation(summary = "Créer un utilisateur", description = "Crée un nouvel utilisateur.", tags = { "utilisateurs",
			"post" }, responses = {
					@ApiResponse(responseCode = "201", content = {
							@Content(schema = @Schema(implementation = BaseUserResponse.class), mediaType = "application/json")
					}),
					@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
	@PostMapping
	public ResponseEntity<BaseUserResponse> createUser(@RequestBody @Valid UserCreateRequest userCreateRequest) {
		final UserEntity userEntity = userMapper.toUserEntity(userCreateRequest);
		userService.createUser(userEntity);
		final BaseUserResponse userResponse = userMapper.toBaseUserResponse(userEntity);
		return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
	}

	@Operation(summary = "Mettre à jour un utilisateur grâce à son identifiant publique", description = "Met à jour les informations d'un utilisateur grâce à son identifiant publique.", tags = {
			"utilisateurs", "put" }, responses = {
					@ApiResponse(responseCode = "204", content = {
							@Content(schema = @Schema(implementation = BaseUserResponse.class), mediaType = "application/json") }),
					@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
					@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
	@PutMapping("/{public_id}")
	public ResponseEntity<BaseUserResponse> updateUserByPublicId(@PathVariable("public_id") String publicId,
			@RequestBody @Valid UserUpdateRequest userUpdateRequest, Authentication authentication) {
		final UserEntity updatedUserEntity = userService.updateUserByPublicId(publicId, userUpdateRequest,
				authentication);
		final BaseUserResponse userResponse = userMapper.toBaseUserResponse(updatedUserEntity);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userResponse);
	}

	@Operation(summary = "Mettre à jour les informations du profil d'un utilisateur grâce à son identifiant publique", description = "Met à jour les informations du profil d'un utilisateur grâce à son identifiant publique.", tags = {
			"utilisateurs", "put" }, responses = {
					@ApiResponse(responseCode = "201", content = {
							@Content(schema = @Schema(implementation = BaseUserResponse.class), mediaType = "application/json") }),
					@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
					@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
	@PatchMapping("/{public_id}/profile")
	public ResponseEntity<BaseUserResponse> updateUserProfileByPublicId(@PathVariable("public_id") String publicId,
			@RequestBody @Valid UserProfileUpdateRequest userUpdateProfileRequest, Authentication authentication) {
		final UserEntity updatedUserEntity = userService.updateUserProfileByPublicId(publicId, userUpdateProfileRequest,
				authentication);
		final BaseUserResponse userResponse = userMapper.toBaseUserResponse(updatedUserEntity);
		// Une 201 = Created afin d'avoir la réponse, sinon une 204 = NO CONTENT mais pas de réponse 
		return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
	}

	@Operation(summary = "Mettre à jour le mot de passe d'un un utilisateur grâce à son identifiant publique", description = "Mettre à jour le mot de passe d'un un utilisateur d'un utilisateur grâce à son identifiant publique.", tags = {
			"utilisateurs", "put" }, responses = {
					@ApiResponse(responseCode = "200", content = {
							@Content(schema = @Schema(implementation = BaseUserResponse.class), mediaType = "application/json") }),
					@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
					@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
	@PostMapping({ "/{public_id}/change-password" })
	public ResponseEntity<BaseUserResponse> changePassword(
			@PathVariable("public_id") String publicId,
			@RequestBody @Valid UserChangePasswordRequest changePasswordRequest) {
		final UserEntity updatedUserEntity = userService.changeUserPasswordByPublicId(publicId, changePasswordRequest);
		final BaseUserResponse userResponse = userMapper.toBaseUserResponse(updatedUserEntity);
		return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
	}

	@Operation(summary = "Supprimer un utilisateur grâce à son identifiant publique", description = "Supprime un utilisateur par son identifiant publique.", tags = {
			"utilisateurs", "delete" }, responses = {
					@ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) }),
					@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
					@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
			})
	@DeleteMapping("/{public_id}")
	public ResponseEntity<Void> deleteUserByPublicId(@PathVariable(name = "public_id") String publicId) {
		final UserEntity userEntity = userService.getUserByPublicId(publicId);
		if (userEntity == null) {
			return ResponseEntity.notFound().build();
		}
		userService.deleteUserByPublicId(publicId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Supprimer tous les utilisateurs", description = "Supprimer tous les utilisateurs.", tags = {
			"utilisateurs", "delete" }, responses = {
					@ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) }),
					@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
					@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
			})
	@DeleteMapping("/all")
	public ResponseEntity<Void> deleteAllUsers() {
		userService.deleteAllUsers();
		return ResponseEntity.noContent().build();
	}

}
