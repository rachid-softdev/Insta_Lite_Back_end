package fr.instalite.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthenticationRequest {

	@NotBlank(message = "Le champ email est requis.")
	@Email(message = "L'adresse email est invalide.")
	@JsonProperty("email")
	private String email;

	@NotBlank(message = "Le champ mot de passe est requis.")
	@Size(min = 8, message = "Le mot de passe doit contenir au moins {min} caractères.")
	@JsonProperty("password")
	private String password;

	public AuthenticationRequest() {
	}

	public AuthenticationRequest(String email, String password) {
		this.email = email;
		this.password = password;
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

	public static AuthenticationRequestBuilder builder() {
		return new AuthenticationRequestBuilder();
	}

	public static class AuthenticationRequestBuilder {

		private String email;

		private String password;

		public AuthenticationRequestBuilder email(String email) {
			this.email = email;
			return this;
		}

		public AuthenticationRequestBuilder password(String password) {
			this.password = password;
			return this;
		}

		public AuthenticationRequest build() {
			return new AuthenticationRequest(email, password);
		}

	}

}