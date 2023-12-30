package fr.instalite.user;

import java.io.Serializable;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseUserResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("created_at")
	private Instant createdAt;

	@JsonProperty("updated_at")
	private Instant updatedAt;

	@JsonProperty("public_id")
	private String publicId;

	@JsonProperty("firstname")
	private String firstname;

	@JsonProperty("lastname")
	private String lastname;

	@JsonProperty("email")
	private String email;

	@JsonProperty("role")
	private String role;

	public BaseUserResponse() {
		this.createdAt = null;
		this.updatedAt = null;
		this.publicId = "";
		this.firstname = "";
		this.lastname = "";
		this.email = "";
		this.role = null;
	}

	public BaseUserResponse(Instant createdAt, Instant updatedAt, String publicId, String firstname, String lastname,
			String email, String role) {
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.publicId = publicId;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.role = role;
	}

	public Instant getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getPublicId() {
		return this.publicId;
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

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public static BaseUserResponse toUserResponse(BaseUserResponse userResponse) {
		return new BaseUserResponse(userResponse.getCreatedAt(), userResponse.getUpdatedAt(),
				userResponse.getPublicId(),
				userResponse.getFirstname(), userResponse.getLastname(),
				userResponse.getEmail(), userResponse.getRole());
	}

	public static UserResponseBuilder builder() {
		return new UserResponseBuilder();
	}

	public static class UserResponseBuilder {

		private Instant createdAt;
		private Instant updatedAt;
		private String publicId = "";
		private String firstname = "";
		private String lastname = "";
		private String email = "";
		private String role = null;

		public UserResponseBuilder createdAt(Instant createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public UserResponseBuilder updatedAt(Instant updatedAt) {
			this.updatedAt = updatedAt;
			return this;
		}

		public UserResponseBuilder publicId(String publicId) {
			this.publicId = publicId;
			return this;
		}

		public UserResponseBuilder firstname(String firstname) {
			this.firstname = firstname;
			return this;
		}

		public UserResponseBuilder lastname(String lastname) {
			this.lastname = lastname;
			return this;
		}

		public UserResponseBuilder email(String email) {
			this.email = email;
			return this;
		}

		public UserResponseBuilder role(String role) {
			this.role = role;
			return this;
		}

		public BaseUserResponse build() {
			final BaseUserResponse userResponse = new BaseUserResponse();
			userResponse.setCreatedAt(this.createdAt);
			userResponse.setUpdatedAt(this.updatedAt);
			userResponse.setPublicId(this.publicId);
			userResponse.setFirstname(this.firstname);
			userResponse.setLastname(this.lastname);
			userResponse.setEmail(this.email);
			userResponse.setRole(this.role);
			return userResponse;
		}
	}

}
