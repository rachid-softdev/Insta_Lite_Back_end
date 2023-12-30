package fr.instalite.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.instalite.media.image.ImageResponse;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserCreateRequest implements Serializable {

	private static final long serialVersionUID = 1L;

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

	@JsonProperty("uploaded_images")
	private List<ImageResponse> uploadedImages;

	public UserCreateRequest() {
		this.firstname = "";
		this.lastname = "";
		this.email = "";
		this.password = "";
		this.role = ERole.NONE.name();
		this.uploadedImages = new ArrayList<ImageResponse>();
	}

	public UserCreateRequest(String firstname, String lastname, String email, String password, String role,
			List<ImageResponse> uploadedImages) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
		this.role = role;
		this.uploadedImages = uploadedImages;
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

	public List<ImageResponse> getUploadedImages() {
		return this.uploadedImages;
	}

	public void setUploadedImages(List<ImageResponse> uploadedImages) {
		this.uploadedImages = uploadedImages;
	}

	public static UserCreateRequest toUserCreateRequest(UserCreateRequest userCreateRequest) {
		return new UserCreateRequest(userCreateRequest.getFirstname(), userCreateRequest.getLastname(),
				userCreateRequest.getEmail(),
				userCreateRequest.getPassword(), userCreateRequest.getRole(), userCreateRequest.getUploadedImages());
	}

	public static UserCreateRequestBuilder builder() {
		return new UserCreateRequestBuilder();
	}

	public static class UserCreateRequestBuilder {

		private String firstname = "";
		private String lastname = "";
		private String email = "";
		private String password = "";
		private String role = ERole.NONE.name();
		private List<ImageResponse> uploadedImages = new ArrayList<ImageResponse>();

		public UserCreateRequestBuilder firstname(String firstname) {
			this.firstname = firstname;
			return this;
		}

		public UserCreateRequestBuilder lastname(String lastname) {
			this.lastname = lastname;
			return this;
		}

		public UserCreateRequestBuilder email(String email) {
			this.email = email;
			return this;
		}

		public UserCreateRequestBuilder password(String password) {
			this.password = password;
			return this;
		}

		public UserCreateRequestBuilder role(String role) {
			this.role = role;
			return this;
		}

		public UserCreateRequestBuilder uploadedImages(List<ImageResponse> uploadedImages) {
			this.uploadedImages = uploadedImages;
			return this;
		}

		public UserCreateRequest build() {
			UserCreateRequest userCreateRequest = new UserCreateRequest();
			userCreateRequest.setFirstname(this.firstname);
			userCreateRequest.setLastname(this.lastname);
			userCreateRequest.setEmail(this.email);
			userCreateRequest.setPassword(this.password);
			userCreateRequest.setRole(this.role);
			userCreateRequest.setUploadedImages(this.uploadedImages);
			return userCreateRequest;
		}
	}

}