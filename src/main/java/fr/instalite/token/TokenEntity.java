package fr.instalite.token;

import java.io.Serializable;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import fr.instalite.user.UserEntity;

@Entity
@Table(name = "token")
public class TokenEntity implements Serializable {

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
	private String publicId;

	@Column(name = "token", unique = true)
	private String token;

	@Enumerated(EnumType.STRING)
	@Column(name = "token_type")
	private TokenType tokenType = TokenType.BEARER;

	private boolean revoked;

	private boolean expired;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserEntity user;

	public TokenEntity() {
	}

	public TokenEntity(Long id, Instant createdAt, Instant updatedAt, String publicId, String token,
			TokenType tokenType, boolean revoked, boolean expired, UserEntity user) {
		this.id = id;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.publicId = publicId;
		this.token = token;
		this.tokenType = tokenType;
		this.revoked = revoked;
		this.expired = expired;
		this.user = user;
	}

	public Long getId() {
		return id;
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

	public static TokenEntity toTokenEntity(TokenEntity tokenEntity) {
		return new TokenEntity(tokenEntity.getId(), tokenEntity.getCreatedAt(), tokenEntity.getUpdatedAt(),
				tokenEntity.getPublicId(), tokenEntity.getToken(), tokenEntity.getTokenType(), tokenEntity.isRevoked(),
				tokenEntity.isExpired(), tokenEntity.getUser());
	}

	public static TokenEntityBuilder builder() {
		return new TokenEntityBuilder();
	}

	public static class TokenEntityBuilder {

		private Long id;

		private Instant createdAt;

		private Instant updatedAt;

		private String publicId;

		private String token;

		private TokenType tokenType;

		private boolean revoked;

		private boolean expired;

		private UserEntity user;

		public TokenEntityBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public TokenEntityBuilder createdAt(Instant createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public TokenEntityBuilder updatedAt(Instant updatedAt) {
			this.updatedAt = updatedAt;
			return this;
		}

		public TokenEntityBuilder publicId(String publicId) {
			this.publicId = publicId;
			return this;
		}

		public TokenEntityBuilder token(String token) {
			this.token = token;
			return this;
		}

		public TokenEntityBuilder tokenType(TokenType tokenType) {
			this.tokenType = tokenType;
			return this;
		}

		public TokenEntityBuilder revoked(boolean revoked) {
			this.revoked = revoked;
			return this;
		}

		public TokenEntityBuilder expired(boolean expired) {
			this.expired = expired;
			return this;
		}

		public TokenEntityBuilder user(UserEntity user) {
			this.user = user;
			return this;
		}

		public TokenEntity build() {
			return new TokenEntity(id, createdAt, updatedAt, publicId, token, tokenType, revoked, expired, user);
		}

	}

}