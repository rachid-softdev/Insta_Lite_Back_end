package fr.instalite.media.image;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.instalite.user.BaseUserResponse;

public class ImageResponse extends BaseImageResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("author")
	private BaseUserResponse author;

	public ImageResponse() {
		this.author = null;
	}

	public ImageResponse(BaseUserResponse author) {
		this.author = author;
	}

	public BaseUserResponse getAuthor() {
		return author;
	}

	public void setAuthor(BaseUserResponse author) {
		this.author = author;
	}

	public static ImageResponse toImageResponse(ImageResponse imageResponse) {
		return new ImageResponse(imageResponse.getAuthor());
	}

	public static ImageResponseBuilder builder() {
		return new ImageResponseBuilder();
	}

	public static class ImageResponseBuilder extends BaseImageResponseBuilder {

		private BaseUserResponse author;

		public ImageResponseBuilder author(BaseUserResponse author) {
			this.author = author;
			return this;
		}

		public ImageResponse build() {
			ImageResponse imageResponse = new ImageResponse();
			imageResponse.setAuthor(author);
			return imageResponse;
		}
	}

}