package fr.instalite.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserChangePasswordRequest {

	@NotBlank(message = "Le champ publicId est requis.")
	@JsonProperty("public_id")
	private String publicId;

	@NotBlank(message = "Le champ currentPassword est requis.")
	@Size(min = 6, message = "Le mot de passe actuel doit contenir au moins {min} caractères.")
	@JsonProperty("current_password")
	private String currentPassword;

	@NotBlank(message = "Le champ newPassword est requis.")
	@Size(min = 6, message = "Le nouveau mot de passe doit contenir au moins {min} caractères.")
	@JsonProperty("new_password")
	private String newPassword;

	@NotBlank(message = "Le champ newPassword est requis.")
	@Size(min = 6, message = "Le nouveau mot de passe doit contenir au moins {min} caractères.")
	@JsonProperty("confirm_password")
	private String confirmPassword;

	public UserChangePasswordRequest() {
	}

	public UserChangePasswordRequest(String currentPassword, String newPassword, String confirmPassword) {
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
		this.confirmPassword = confirmPassword;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public static ChangePasswordRequestBuilder builder() {
		return new ChangePasswordRequestBuilder();
	}

	public static class ChangePasswordRequestBuilder {

		private String currentPassword = "";
		private String newPassword = "";
		private String confirmPassword = "";

		public ChangePasswordRequestBuilder currentPassword(String currentPassword) {
			this.currentPassword = currentPassword;
			return this;
		}

		public ChangePasswordRequestBuilder newPassword(String newPassword) {
			this.newPassword = newPassword;
			return this;
		}

		public ChangePasswordRequestBuilder confirmPassword(String confirmPassword) {
			this.confirmPassword = confirmPassword;
			return this;
		}

		public UserChangePasswordRequest build() {
			UserChangePasswordRequest changePasswordRequest = new UserChangePasswordRequest();
			changePasswordRequest.setCurrentPassword(this.currentPassword);
			changePasswordRequest.setNewPassword(this.newPassword);
			changePasswordRequest.setConfirmPassword(this.confirmPassword);
			return changePasswordRequest;
		}
	}
}
