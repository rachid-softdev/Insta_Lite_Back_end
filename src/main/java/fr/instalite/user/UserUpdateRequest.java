package fr.instalite.user;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserUpdateRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@Size(min = 1, max = 50, message = "Le publicId doit contenir entre {min} et {max} caractères.")
	@NotNull(message = "Le champ publicId est requis.")
	@JsonProperty("public_id")
	private String publicId;

	@NotBlank(message = "Le champ prénom est requis.")
	@Size(min = 1, max = 50, message = "Le prénom doit contenir entre {min} et {max} caractères.")
	@JsonProperty("firstname")
	private String firstname;

	@NotBlank(message = "Le champ nom est requis.")
	@Size(min = 1, max = 50, message = "Le nom doit contenir entre {min} et {max} caractères.")
	@JsonProperty("lastname")
	private String lastname;

	@NotBlank(message = "Le champ email est requis.")
	@Email(message = "L'adresse email est invalide.")
	@JsonProperty("email")
	private String email;

	@NotBlank(message = "Le champ mot de passe est requis.")
	@Size(min = 6, message = "Le mot de passe doit contenir au moins {min} caractères.")
	@JsonProperty("password")
	private String password;

	@NotBlank(message = "Le champ role est requis.")
	@JsonProperty("role")
	private String role;

	public UserUpdateRequest() {
		this.publicId = "";
		this.firstname = "";
		this.lastname = "";
		this.email = "";
		this.password = "";
		this.role = ERole.NONE.name();
	}

	public UserUpdateRequest(String publicId, String firstname, String lastname, String email, String password,
			String role) {
		this.publicId = publicId;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public static UserUpdateRequest toUserUpdateRequest(UserUpdateRequest userUpdateRequest) {
		return new UserUpdateRequest(
				userUpdateRequest.getPublicId(),
				userUpdateRequest.getFirstname(), userUpdateRequest.getLastname(),
				userUpdateRequest.getEmail(),
				userUpdateRequest.getPassword(), userUpdateRequest.getRole());
	}

	public static UserUpdateRequestBuilder builder() {
		return new UserUpdateRequestBuilder();
	}

	public static class UserUpdateRequestBuilder {

		private String publicId = "";
		private String firstname = "";
		private String lastname = "";
		private String email = "";
		private String password = "";
		private String role = ERole.NONE.name();

		public UserUpdateRequestBuilder publicId(String publicId) {
			this.publicId = publicId;
			return this;
		}

		public UserUpdateRequestBuilder firstname(String firstname) {
			this.firstname = firstname;
			return this;
		}

		public UserUpdateRequestBuilder lastname(String lastname) {
			this.lastname = lastname;
			return this;
		}

		public UserUpdateRequestBuilder email(String email) {
			this.email = email;
			return this;
		}

		public UserUpdateRequestBuilder password(String password) {
			this.password = password;
			return this;
		}

		public UserUpdateRequestBuilder role(String role) {
			this.role = role;
			return this;
		}

		public UserUpdateRequest build() {
			UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
			userUpdateRequest.setFirstname(this.firstname);
			userUpdateRequest.setLastname(this.lastname);
			userUpdateRequest.setEmail(this.email);
			userUpdateRequest.setPassword(this.password);
			userUpdateRequest.setRole(this.role);
			return userUpdateRequest;
		}
	}

}
