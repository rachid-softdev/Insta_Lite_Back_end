package fr.instalite.token;

import java.io.Serializable;

import fr.instalite.user.UserEntity;

public class TokenResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String token;
	private TokenType tokenType;
	private boolean revoked;
	private boolean expired;
	private UserEntity user;

	public TokenResponse() {
		this.token = "";
		this.tokenType = null;
		this.revoked = false;
		this.expired = false;
		this.user = null;
	}

	public TokenResponse(String token, TokenType tokenType, boolean revoked, boolean expired, UserEntity user) {
		this.token = token;
		this.tokenType = tokenType;
		this.revoked = revoked;
		this.expired = expired;
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public TokenType getTokenType() {
		return tokenType;
	}

	public void setTokenType(TokenType tokenType) {
		this.tokenType = tokenType;
	}

	public boolean isRevoked() {
		return revoked;
	}

	public void setRevoked(boolean revoked) {
		this.revoked = revoked;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public static TokenResponseBuilder builder() {
		return new TokenResponseBuilder();
	}

	public static class TokenResponseBuilder {
		private String token;
		private TokenType tokenType;
		private boolean revoked;
		private boolean expired;
		private UserEntity user;

		public TokenResponseBuilder token(String token) {
			this.token = token;
			return this;
		}

		public TokenResponseBuilder tokenType(TokenType tokenType) {
			this.tokenType = tokenType;
			return this;
		}

		public TokenResponseBuilder revoked(boolean revoked) {
			this.revoked = revoked;
			return this;
		}

		public TokenResponseBuilder expired(boolean expired) {
			this.expired = expired;
			return this;
		}

		public TokenResponseBuilder user(UserEntity user) {
			this.user = user;
			return this;
		}

		public TokenResponse build() {
			return new TokenResponse(token, tokenType, revoked, expired, user);
		}
	}

}
