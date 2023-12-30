package fr.instalite.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

	@NotBlank(message = "Le champ prénom est requis.")
	@Size(min = 2, max = 50, message = "Le prénom doit contenir entre {min} et {max} caractères.")
	@JsonProperty("firstname")
	private String firstname;

	@NotBlank(message = "Le champ nom est requis.")
	@Size(min = 2, max = 50, message = "Le nom doit contenir entre {min} et {max} caractères.")
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

	@NotNull(message = "Le champ role est requis.")
	@JsonProperty("role")
	private String role;

	public RegisterRequest() {
	}

	public RegisterRequest(String firstname, String lastname, String email, String password, String role) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
		this.role = role;
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
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public static RegisterRequestBuilder builder() {
		return new RegisterRequestBuilder();
	}

	public static class RegisterRequestBuilder {

		private String firstname;
		private String lastname;
		private String email;
		private String password;
		private String role;

		public RegisterRequestBuilder firstname(String firstname) {
			this.firstname = firstname;
			return this;
		}

		public RegisterRequestBuilder lastname(String lastname) {
			this.lastname = lastname;
			return this;
		}

		public RegisterRequestBuilder email(String email) {
			this.email = email;
			return this;
		}

		public RegisterRequestBuilder password(String password) {
			this.password = password;
			return this;
		}

		public RegisterRequestBuilder role(String role) {
			this.role = role;
			return this;
		}

		public RegisterRequest build() {
			return new RegisterRequest(firstname, lastname, email, password, role);
		}

	}

}
