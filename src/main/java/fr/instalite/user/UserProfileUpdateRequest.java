package fr.instalite.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.instalite.media.image.ImageResponse;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserProfileUpdateRequest implements Serializable {

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

	public UserProfileUpdateRequest() {
		this.publicId = "";
		this.firstname = "";
		this.lastname = "";
		this.email = "";
	}

	public UserProfileUpdateRequest(String publicId, String firstname, String lastname, String email) {
		this.publicId = publicId;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
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

	public static UserProfileUpdateRequest toUserUpdateProfileRequest(
			UserProfileUpdateRequest userUpdateProfileRequest) {
		return new UserProfileUpdateRequest(
				userUpdateProfileRequest.getPublicId(),
				userUpdateProfileRequest.getFirstname(), userUpdateProfileRequest.getLastname(),
				userUpdateProfileRequest.getEmail());
	}

	public static UserUpdateProfileRequestBuilder builder() {
		return new UserUpdateProfileRequestBuilder();
	}

	public static class UserUpdateProfileRequestBuilder {

		private String publicId = "";
		private String firstname = "";
		private String lastname = "";
		private String email = "";

		public UserUpdateProfileRequestBuilder publicId(String publicId) {
			this.publicId = publicId;
			return this;
		}

		public UserUpdateProfileRequestBuilder firstname(String firstname) {
			this.firstname = firstname;
			return this;
		}

		public UserUpdateProfileRequestBuilder lastname(String lastname) {
			this.lastname = lastname;
			return this;
		}

		public UserUpdateProfileRequestBuilder email(String email) {
			this.email = email;
			return this;
		}

		public UserProfileUpdateRequest build() {
			UserProfileUpdateRequest userUpdateProfileRequest = new UserProfileUpdateRequest();
			userUpdateProfileRequest.setPublicId(this.publicId);
			userUpdateProfileRequest.setFirstname(this.firstname);
			userUpdateProfileRequest.setLastname(this.lastname);
			userUpdateProfileRequest.setEmail(this.email);
			return userUpdateProfileRequest;
		}
	}

}
