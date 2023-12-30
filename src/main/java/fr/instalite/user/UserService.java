package fr.instalite.user;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.instalite.common.RandomIDGenerator;

@Service
public class UserService implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Logger logger = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final RandomIDGenerator randomIDGenerator;

	public UserService(@Autowired UserRepository userRepository, @Autowired UserMapper userMapper,
			@Autowired PasswordEncoder passwordEncoder,
			@Autowired RandomIDGenerator randomIDGenerator) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
		this.passwordEncoder = passwordEncoder;
		this.randomIDGenerator = randomIDGenerator;
	}

	public UserEntity getUserById(Long id) {
		logger.trace(String.format("[UserService][getUserById]: Recherche de l'utilisateur par id : {%d}", id));
		return userRepository.findById(id)
				.orElseThrow(() -> {
					logger.error(String
							.format("[UserService][getUserById]: Erreur recherche de l'utilisateur par id : %d", id));
					throw new UsernameNotFoundException(
							String.format("L'utilisateur avec l'id %d n'a pas été trouvé.", id));
				});
	}

	public UserEntity getUserByPublicId(String publicId) {
		logger.trace(String.format("[UserService][getUserByPublicId]: Recherche de l'utilisateur par publicId : %s",
				publicId));
		return userRepository.findByPublicId(publicId)
				.orElseThrow(() -> {
					logger.error(
							String.format(
									"[UserService][getUserByPublicId]: Erreur recherche de l'utilisateur par publicId : %s",
									publicId));
					throw new UsernameNotFoundException(
							String.format("L'utilisateur avec le publicId %s n'a pas été trouvé.", publicId));
				});
	}

	public UserEntity getUserByEmail(String email) {
		logger.trace(String.format("[UserService][getUserByEmail]: Recherche de l'utilisateur par email : %s", email));
		return userRepository.findByEmail(email)
				.orElseThrow(() -> {
					logger.error(String.format(
							"[UserService][getUserByEmail]: Erreur recherche de l'utilisateur par email : %s",
							email));
					throw new UsernameNotFoundException(
							String.format("L'utilisateur avec l'email %s n'a pas été trouvé.", email));
				});
	}

	public List<UserEntity> getAllUsers() {
		logger.trace("[UserService][getAllUsers]: Récupération de tous les utilisateurs");
		return userRepository.findAll();
	}

	public Page<UserEntity> getAllUsers(Pageable pageable) {
		logger.trace("[UserService][getAllUsers]: Récupération de tous les utilisateurs avec pagination");
		return userRepository.findAll(pageable);
	}

	@Transactional
	public UserEntity createUser(UserEntity userEntity) {
		logger.trace("[UserService][createUser]: Création d'un nouvel utilisateur");
		if (userEntity == null) {
			logger.error("[UserService][createUser]: Erreur l'utilisateur à insérer est nul");
			throw new UserException("User not saved",
					String.format("Aucun utilisateur à insérer."),
					HttpStatus.BAD_REQUEST);
		}
		logger.debug("[UserService][createUser]: Vérification que l'utilisateur a une adresse e-mail unique");
		if (userRepository.findByEmail(userEntity.getEmail()).isPresent()) {
			logger.error("[UserService][createUser]: Erreur l'utilisateur a une adresse e-mail déja enregistré");
			throw new UserException("User already exists",
					String.format(
							"L'utilisateur avec l'email %s existe déja.",
							userEntity.getEmail()),
					HttpStatus.CONFLICT);
		}
		userEntity.setPublicId(randomIDGenerator.generateRandomUUID());
		userEntity.setCreatedAt(Instant.now());
		userEntity.setUpdatedAt(Instant.now());
		userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
		return userRepository.save(userEntity);
	}

	@Transactional
	public Iterable<UserEntity> createUsers(Iterable<UserEntity> usersEntities) {
		logger.trace("[UserService][createUsers]: Création de plusieurs utilisateurs");
		for (UserEntity userEntity : usersEntities) {
			logger.debug("[UserService][createUsers]: Vérification que l'utilisateur a une adresse e-mail unique");
			if (userRepository.findByEmail(userEntity.getEmail()).isPresent()) {
				logger.error("[UserService][createUsers]: Erreur l'utilisateur a une adresse e-mail déja enregistré");
				throw new UserException("User already exists",
						String.format(
								"L'utilisateur avec l'email %s existe déja.",
								userEntity.getEmail()),
						HttpStatus.CONFLICT);
			}
			userEntity.setPublicId(randomIDGenerator.generateRandomUUID());
			userEntity.setCreatedAt(Instant.now());
			userEntity.setUpdatedAt(Instant.now());
			userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
		}
		return userRepository.saveAll(usersEntities);
	}

	@Transactional
	public UserEntity updateUserByPublicId(String publicId, UserUpdateRequest userUpdateRequest,
			Authentication authentication) {
		logger.trace(String.format("[UserService][updateUserByPublicId]: Mis à jour de l'utilisateur par publicId : %s",
				publicId));
		final UserEntity userToUpdate = userRepository.findByPublicId(publicId)
				.orElseThrow(() -> {
					logger.error(String.format(
							"[UserService][updateUserByPublicId]: Erreur l'utilisateur n'a pas été trouvé via son publicId : %s",
							publicId));
					throw new UsernameNotFoundException(
							String.format("L'utilisateur avec l'identifiant publique %s n'a pas été trouvé.",
									publicId));
				});
		final UserEntity userEntity = userMapper.toUserEntity(userUpdateRequest);
		// Pour la mis à jour, sinon c'est considérer comme un ajout car y'a pas d'id
		userEntity.setId(userToUpdate.getId());
		userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
		return userRepository.save(userEntity);
	}

	@Transactional
	public UserEntity updateUserProfileByPublicId(String publicId, UserProfileUpdateRequest userUpdateProfileRequest,
			Authentication authentication) {
		logger.trace(String.format(
				"[UserService][updateUserProfileByPublicId]: Mis à jour du profil de l'utilisateur par publicId : %s",
				publicId));
		final UserEntity userToUpdate = userRepository.findByPublicId(publicId)
				.orElseThrow(() -> {
					logger.error(String.format(
							"[UserService][updateUserProfileByPublicId]: Erreur l'utilisateur n'a pas été trouvé via son publicId : %s",
							publicId));
					throw new UsernameNotFoundException(
							String.format("L'utilisateur avec l'identifiant publique %s n'a pas été trouvé.",
									publicId));
				});
		final UserEntity userEntity = userMapper.toUserEntity(userToUpdate, userUpdateProfileRequest);
		/**
		 * Pour la mis à jour, il faut préciser la clé primaire sinon c'est considérer
		 * comme une insertion car il n'y a pas d'id
		 */
		userEntity.setId(userToUpdate.getId());
		return userRepository.save(userToUpdate);
	}

	@Transactional
	public UserEntity changeUserPasswordByPublicId(String publicId, UserChangePasswordRequest changePasswordRequest) {
		final UserEntity userToUpdate = userRepository.findByPublicId(publicId)
				.orElseThrow(() -> {
					logger.error(String.format(
							"[UserService][updateUserByPublicId]: Erreur l'utilisateur n'a pas été trouvé via son publicId : %s",
							publicId));
					throw new UsernameNotFoundException(
							String.format("L'utilisateur avec l'identifiant publique %s n'a pas été trouvé.",
									publicId));
				});
		/**
		 * Le nouveau mot de passe de ne correspond pas à celui qui est au nouveau mot
		 * de passe confirmé
		 */
		if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
			throw new InvalidPasswordException("Bad Request",
					"Le nouveau mot de passe ne correspond pas au mot de passe confirmé.",
					HttpStatus.BAD_REQUEST);
		}
		/** Le mot de passe ne correspond pas à celui enregistré en base de données */
		if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), userToUpdate.getPassword())) {
			throw new InvalidPasswordException("Bad Request",
					"Le mot de passe actuel fourni ne correspond pas au mot de passe enregistré.",
					HttpStatus.BAD_REQUEST);
		}
		userToUpdate.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
		return userRepository.save(userToUpdate);
	}

	@Transactional
	public void deleteUserById(Long id) {
		logger.trace(String.format("[UserService][deleteUserById]: Suppresion de l'utilisateur par id : %d",
				id));
		logger.debug("[UserService][deleteUserById]: Vérification que l'utilisateur existe afin de le supprimer");
		final UserEntity existingUser = userRepository.findById(id)
				.orElseThrow(() -> {
					logger.error(String.format(
							"[UserService][deleteUserById]: Erreur l'utilisateur n'a pas été trouvé via son id : %d",
							id));
					throw new UserNotFoundException("User not found",
							String.format("L'utilisateur avec l'id %d n'a pas été trouvé.", id), HttpStatus.NOT_FOUND);
				});
		userRepository.deleteById(existingUser.getId());
	}

	@Transactional
	public void deleteUserByPublicId(String publicId) {
		logger.trace(String.format("[UserService][deleteUserById]: Suppresion de l'utilisateur par publicId : %s",
				publicId));
		final UserEntity existingUser = userRepository.findByPublicId(publicId)
				.orElseThrow(() -> {
					logger.error(String.format(
							"[UserService][deleteUserByPublicId]: Erreur l'utilisateur n'a pas été trouvé via son publicId : %s",
							publicId));
					throw new UserNotFoundException("User not found",
							String.format("L'utilisateur avec l'identifiant publique %s n'a pas été trouvé.", publicId),
							HttpStatus.NOT_FOUND);
				});
		userRepository.deleteByPublicId(existingUser.getPublicId());
	}

	@Transactional
	public void deleteAllUsers() {
		if (userRepository.count() == 0) {
			throw new UserNotFoundException("User not found", String.format("Aucun utilisateur n'a été trouvé."),
					HttpStatus.NOT_FOUND);
		}
		userRepository.deleteAll();
	}

}
