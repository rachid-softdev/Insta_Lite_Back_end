package fr.instalite.media.image;

import fr.instalite.user.UserEntity;
import fr.instalite.user.BaseUserResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImageCreateRequest {

	@NotBlank(message = "Le titre ne peut pas être vide.")
	@Size(min = 1, max = 32, message = "Le titre doit contenir entre {min} et {max} caractères.")
	@JsonProperty("title")
	private String title;

	@NotBlank(message = "La description ne peut pas être vide.")
	@Size(min = 0, max = 128, message = "La description doit contenir moins de {max} caractères.")
	@JsonProperty("description")
	private String description;

	@Null
	private MultipartFile file;

	@NotNull(message = "La visibilité ne peut pas être nulle.")
	@JsonProperty("visibility")
	private String visibility;

	@NotNull(message = "L'auteur ne peut pas être nul.")
	@JsonProperty("author")
	private BaseUserResponse author;

	public ImageCreateRequest() {
		this.title = "";
		this.description = "";
		this.file = null;
		this.visibility = null;
		this.author = null;
	}

	public ImageCreateRequest(
			String title,
			String description,
			MultipartFile file,
			String visibility,
			BaseUserResponse author) {
		this.title = title;
		this.description = description;
		this.file = file;
		this.visibility = visibility;
		this.author = author;
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

	public MultipartFile getFile() {
		return this.file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
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
		return "ImageCreateRequest{" +
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

		private String title;
		private String description;
		private MultipartFile file;
		private String visibility;
		private BaseUserResponse author;

		public ImageUpdateRequestBuilder title(String title) {
			this.title = title;
			return this;
		}

		public ImageUpdateRequestBuilder description(String description) {
			this.description = description;
			return this;
		}

		public ImageUpdateRequestBuilder file(MultipartFile file) {
			this.file = file;
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

		public ImageCreateRequest build() {
			return new ImageCreateRequest(
					title,
					description,
					file,
					visibility,
					author);
		}
	}
}
