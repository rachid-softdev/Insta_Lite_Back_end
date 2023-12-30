package fr.instalite.user;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import fr.instalite.media.image.ImageEntity;
import fr.instalite.token.TokenEntity;

@Entity
@Table(name = "_user")
public class UserEntity implements UserDetails, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(name = "created_at", nullable = false, updatable = false)
	@CreationTimestamp
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false, updatable = true)
	@UpdateTimestamp
	private Instant updatedAt;

	@Column(name = "public_id", nullable = false, unique = true)
	@Size(min = 1, max = 50, message = "Le publicId doit contenir entre {min} et {max} caractères.")
	@NotNull(message = "Le champ publicId est requis.")
	private String publicId;

	@NotBlank(message = "Le champ prénom est requis.")
	@Size(min = 1, max = 50, message = "Le prénom doit contenir entre {min} et {max} caractères.")
	@Column(name = "firstname")
	private String firstname;

	@NotBlank(message = "Le champ nom est requis.")
	@Size(min = 1, max = 50, message = "Le nom doit contenir entre {min} et {max} caractères.")
	@Column(name = "lastname")
	private String lastname;

	@NotBlank(message = "Le champ email est requis.")
	@Email(message = "L'adresse email est invalide.")
	@Column(name = "email", unique = true)
	private String email;

	@NotBlank(message = "Le champ mot de passe est requis.")
	@Size(min = 6, message = "Le mot de passe doit contenir au moins {min} caractères.")
	@Column(name = "password")
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private ERole role;

	@OneToMany(mappedBy = "user")
	private List<TokenEntity> tokens;

	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
	private List<ImageEntity> uploadedImages;

	public UserEntity() {
		this.id = 0L;
		this.createdAt = Instant.now();
		this.updatedAt = Instant.now();
		this.publicId = "";
		this.firstname = "";
		this.lastname = "";
		this.email = "";
		this.password = "";
		this.role = ERole.NONE;
		this.tokens = new ArrayList<TokenEntity>();
		this.uploadedImages = new ArrayList<ImageEntity>();
	}

	public UserEntity(Long id, Instant createdAt, Instant updatedAt, String publicId, String firstname, String lastname,
			String email, String password, ERole role, List<TokenEntity> tokens, List<ImageEntity> uploadedImages) {
		this.id = id;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.publicId = publicId;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
		this.role = role;
		this.tokens = tokens;
		this.uploadedImages = uploadedImages;
	}

	public UserEntity(UserEntity userEntity) {
		this(
				userEntity.getId(),
				userEntity.getCreatedAt(),
				userEntity.getUpdatedAt(),
				userEntity.getPublicId(),
				userEntity.getFirstname(),
				userEntity.getLastname(),
				userEntity.getEmail(),
				userEntity.getPassword(),
				userEntity.getRole(),
				userEntity.getTokens(),
				userEntity.getUploadedImages());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return role.getAuthorities();
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ERole getRole() {
		return this.role;
	}

	public void setRole(ERole role) {
		this.role = role;
	}

	public List<TokenEntity> getTokens() {
		return this.tokens;
	}

	public void setTokens(List<TokenEntity> tokens) {
		this.tokens = tokens;
	}

	public List<ImageEntity> getUploadedImages() {
		return this.uploadedImages;
	}

	public void setUploadedImages(List<ImageEntity> uploadedImages) {
		this.uploadedImages = uploadedImages;
	}

	public static UserEntity toUserEntity(UserEntity userEntity) {
		return new UserEntity(userEntity.getId(), userEntity.getCreatedAt(), userEntity.getUpdatedAt(),
				userEntity.getPublicId(), userEntity.getFirstname(), userEntity.getLastname(), userEntity.getEmail(),
				userEntity.getPassword(), userEntity.getRole(), userEntity.getTokens(),
				userEntity.getUploadedImages());
	}

	@Override
	public String toString() {
		return "UserEntity{" +
				"id=" + id +
				", createdAt=" + createdAt +
				", updatedAt=" + updatedAt +
				", publicId='" + publicId + '\'' +
				", firstname='" + firstname + '\'' +
				", lastname='" + lastname + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", role=" + role +
				'}';
	}

	public static UserEntityBuilder builder() {
		return new UserEntityBuilder();
	}

	public static class UserEntityBuilder {

		private Long id;

		private Instant createdAt;

		private Instant updatedAt;

		private String publicId;

		private String firstname;

		private String lastname;

		private String email;

		private String password;

		private ERole role;

		private List<TokenEntity> tokens;

		private List<ImageEntity> uploadedImages;

		public UserEntityBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public UserEntityBuilder createdAt(Instant createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public UserEntityBuilder updatedAt(Instant updatedAt) {
			this.updatedAt = updatedAt;
			return this;
		}

		public UserEntityBuilder publicId(String publicId) {
			this.publicId = publicId;
			return this;
		}

		public UserEntityBuilder firstname(String firstname) {
			this.firstname = firstname;
			return this;
		}

		public UserEntityBuilder lastname(String lastname) {
			this.lastname = lastname;
			return this;
		}

		public UserEntityBuilder email(String email) {
			this.email = email;
			return this;
		}

		public UserEntityBuilder password(String password) {
			this.password = password;
			return this;
		}

		public UserEntityBuilder role(ERole role) {
			this.role = role;
			return this;
		}

		public UserEntityBuilder tokens(List<TokenEntity> tokens) {
			this.tokens = tokens;
			return this;
		}

		public UserEntityBuilder uploadedImages(List<ImageEntity> uploadedImages) {
			this.uploadedImages = uploadedImages;
			return this;
		}

		public UserEntity build() {
			return new UserEntity(id, createdAt, updatedAt, publicId, firstname, lastname, email, password, role,
					tokens, uploadedImages);
		}

	}

}