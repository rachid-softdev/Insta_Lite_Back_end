package fr.instalite.media.image;

import fr.instalite.user.BaseUserResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImageUpdateRequest {

	@NotNull(message = "L'identifiant publique ne peut pas être nul.")
	@JsonProperty("public_id")
	private String publicId;

	@NotNull(message = "La date de publication ne peut pas être nulle.")
	@JsonProperty("published_at")
	private Instant publishedAt;

	@JsonProperty("file")
	private MultipartFile file;

	@NotBlank(message = "Le titre ne peut pas être vide.")
	@Size(min = 1, max = 32, message = "Le titre doit contenir entre {min} et {max} caractères.")
	@JsonProperty("title")
	private String title;

	@NotBlank(message = "La description ne peut pas être vide.")
	@Size(min = 0, max = 128, message = "La description doit contenir moins de {max} caractères.")
	@JsonProperty("description")
	private String description;

	@NotNull(message = "La visibilité ne peut pas être nulle.")
	@JsonProperty("visibility")
	private String visibility;

	@NotNull(message = "L'auteur ne peut pas être nul.")
	@JsonProperty("author")
	private BaseUserResponse author;

	public ImageUpdateRequest() {
		this.publicId = "";
		this.publishedAt = Instant.now();
		this.file = null;
		this.title = "";
		this.description = "";
		this.visibility = null;
		this.author = null;
	}

	public ImageUpdateRequest(
			String publicId,
			Instant publishedAt,
			MultipartFile file,
			String title,
			String description,
			String visibility,
			BaseUserResponse author) {
		this.publicId = publicId;
		this.publishedAt = publishedAt;
		this.file = file;
		this.title = title;
		this.description = description;
		this.visibility = visibility;
		this.author = author;
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

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
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

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public BaseUserResponse getAuthor() {
		return author;
	}

	public void setAuthor(BaseUserResponse author) {
		this.author = author;
	}

	@Override
	public String toString() {
		return "ImageUpdateRequest{" +
				"title='" + title + '\'' +
				", description='" + description + '\'' +
				", file=" + file +
				", visibility='" + visibility + '\'' +
				", author=" + author +
				'}';
	}

	public static ImageUpdateRequestBuilder builder() {
		return new ImageUpdateRequestBuilder();
	}

	public static class ImageUpdateRequestBuilder {

		private String publicId;
		private Instant publishedAt;
		private MultipartFile file;
		private String title;
		private String description;
		private String visibility;
		private BaseUserResponse author;

		public ImageUpdateRequestBuilder publicId(String publicId) {
			this.publicId = publicId;
			return this;
		}

		public ImageUpdateRequestBuilder publishedAt(Instant publishedAt) {
			this.publishedAt = publishedAt;
			return this;
		}

		public ImageUpdateRequestBuilder file(MultipartFile file) {
			this.file = file;
			return this;
		}

		public ImageUpdateRequestBuilder title(String title) {
			this.title = title;
			return this;
		}

		public ImageUpdateRequestBuilder description(String description) {
			this.description = description;
			return this;
		}

		public ImageUpdateRequestBuilder visibility(String visibility) {
			this.visibility = visibility;
			return this;
		}

		public ImageUpdateRequestBuilder author(BaseUserResponse author) {
			this.author = author;
			return this;
		}

		public ImageUpdateRequest build() {
			return new ImageUpdateRequest(
					publicId,
					publishedAt,
					file,
					title,
					description,
					visibility,
					author);
		}
	}
}
