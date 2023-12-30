package fr.instalite.media.image;

import java.io.Serializable;
import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.instalite.common.EVisibility;

public class BaseImageResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("created_at")
	private Instant createdAt;

	@JsonProperty("updated_at")
	private Instant updatedAt;

	@JsonProperty("public_id")
	private String publicId;

	@JsonProperty("published_at")
	private Instant publishedAt;

	@JsonProperty("file_url")
	private String fileUrl;

	@JsonProperty("title")
	private String title;

	@JsonProperty("description")
	private String description;

	@JsonProperty("visibility")
	private EVisibility visibility;

	public BaseImageResponse() {
		this.createdAt = null;
		this.updatedAt = null;
		this.publicId = "";
		this.publishedAt = null;
		this.title = "";
		this.description = "";
		this.fileUrl = "";
		this.visibility = null;
	}

	public BaseImageResponse(
			Instant createdAt,
			Instant updatedAt,
			String publicId, Instant publishedAt, String fileUrl, String title, String description,
			EVisibility visibility) {
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.publicId = publicId;
		this.publishedAt = publishedAt;
		this.fileUrl = fileUrl;
		this.title = title;
		this.description = description;
		this.visibility = visibility;
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

	public Instant getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(Instant publishedAt) {
		this.publishedAt = publishedAt;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public EVisibility getVisibility() {
		return visibility;
	}

	public void setVisibility(EVisibility visibility) {
		this.visibility = visibility;
	}

	public static BaseImageResponse toBaseImageResponse(BaseImageResponse baseImageResponse) {
		return new BaseImageResponse(
				baseImageResponse.getCreatedAt(),
				baseImageResponse.getUpdatedAt(),
				baseImageResponse.publicId, baseImageResponse.getPublishedAt(), baseImageResponse.getFileUrl(),
				baseImageResponse.getTitle(),
				baseImageResponse.getDescription(), baseImageResponse.getVisibility());
	}

	public static BaseImageResponseBuilder builder() {
		return new BaseImageResponseBuilder();
	}

	public static class BaseImageResponseBuilder {

		private Instant createdAt;
		private Instant updatedAt;
		private String publicId;
		private Instant publishedAt;
		private String fileUrl;
		private String title;
		private String description;
		private EVisibility visibility;

		public BaseImageResponseBuilder createdAt(Instant createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public BaseImageResponseBuilder updatedAt(Instant updatedAt) {
			this.updatedAt = updatedAt;
			return this;
		}

		public BaseImageResponseBuilder publicId(String publicId) {
			this.publicId = publicId;
			return this;
		}

		public BaseImageResponseBuilder publishedAt(Instant publishedAt) {
			this.publishedAt = publishedAt;
			return this;
		}

		public BaseImageResponseBuilder fileUrl(String fileUrl) {
			this.fileUrl = fileUrl;
			return this;
		}

		public BaseImageResponseBuilder title(String title) {
			this.title = title;
			return this;
		}

		public BaseImageResponseBuilder description(String description) {
			this.description = description;
			return this;
		}

		public BaseImageResponseBuilder visibility(EVisibility visibility) {
			this.visibility = visibility;
			return this;
		}

		public BaseImageResponse build() {
			BaseImageResponse baseImageResponse = new BaseImageResponse();
			baseImageResponse.setCreatedAt(createdAt);
			baseImageResponse.setUpdatedAt(updatedAt);
			baseImageResponse.setPublicId(publicId);
			baseImageResponse.setPublishedAt(publishedAt);
			baseImageResponse.setFileUrl(fileUrl);
			baseImageResponse.setTitle(title);
			baseImageResponse.setDescription(description);
			baseImageResponse.setVisibility(visibility);
			return baseImageResponse;
		}
	}

}
