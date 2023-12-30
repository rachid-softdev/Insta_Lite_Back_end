package fr.instalite.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.instalite.user.BaseUserResponse;

public class AuthenticationResponse {

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("refresh_token")
	private String refreshToken;

	@JsonProperty("user")
	private BaseUserResponse userResponse;

	public AuthenticationResponse() {
		this.accessToken = "";
		this.refreshToken = "";
		this.userResponse = null;
	}

	public AuthenticationResponse(String accessToken, String refreshToken, BaseUserResponse userResponse) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.userResponse = userResponse;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public static AuthenticationResponseBuilder builder() {
		return new AuthenticationResponseBuilder();
	}

	public static class AuthenticationResponseBuilder {

		private String accessToken;

		private String refreshToken;

		private BaseUserResponse userResponse;

		public AuthenticationResponseBuilder accessToken(String accessToken) {
			this.accessToken = accessToken;
			return this;
		}

		public AuthenticationResponseBuilder refreshToken(String refreshToken) {
			this.refreshToken = refreshToken;
			return this;
		}

		public AuthenticationResponseBuilder userResponse(BaseUserResponse userResponse) {
			this.userResponse = userResponse;
			return this;
		}

		public AuthenticationResponse build() {
			return new AuthenticationResponse(accessToken, refreshToken, userResponse);
		}

	}

}
