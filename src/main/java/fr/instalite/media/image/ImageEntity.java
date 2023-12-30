package fr.instalite.media.image;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import fr.instalite.common.EVisibility;
import fr.instalite.user.UserEntity;

@Entity
@Table(name = "image")
public class ImageEntity implements Serializable {

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
	@NotBlank(message = "Le champ publicId est requis.")
	private String publicId;

	@Column(name = "published_at", nullable = false, updatable = false)
	@CreationTimestamp
	private Instant publishedAt;

	@Column(name = "file_path")
	private String filePath;

	/**
	 * Transient pour ne pas prendre en compte l'attribut file dans la persistence
	 * en base de données
	 */
	@Transient
	private MultipartFile file;

	@Transient
	private String fileUrl;

	@Column(name = "title", length = 32, nullable = false)
	@NotBlank(message = "Le champ title est requis.")
	@Size(min = 1, max = 32, message = "Le champ title doit contenir entre {min} et {max} caractères.")
	private String title;

	@Column(name = "description", length = 128, nullable = true)
	@Size(min = 0, max = 128, message = "Le champ description doit contenir entre {min} et {max} caractères.")
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "visibility")
	private EVisibility visibility;

	@ManyToOne
	@JoinColumn(name = "author_id")
	private UserEntity author;

	public ImageEntity() {
	}

	public ImageEntity(Long id, Instant createdAt, Instant updatedAt, String publicId, Instant publishedAt,
			String filePath, MultipartFile file, String fileUrl, String title, String description,
			EVisibility visibility, UserEntity author) {
		this.id = id;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.publicId = publicId;
		this.publishedAt = publishedAt;
		this.filePath = filePath;
		this.file = file;
		this.fileUrl = fileUrl;
		this.title = title;
		this.description = description;
		this.visibility = visibility;
		this.author = author;
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

	public Instant getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(Instant publishedAt) {
		this.publishedAt = publishedAt;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
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

	public UserEntity getAuthor() {
		return author;
	}

	public void setAuthor(UserEntity author) {
		this.author = author;
	}

	public static ImageEntity toImageEntity(ImageEntity imageEntity) {
		return new ImageEntity(imageEntity.getId(), imageEntity.getCreatedAt(), imageEntity.getUpdatedAt(),
				imageEntity.getPublicId(), imageEntity.getPublishedAt(), imageEntity.getFilePath(),
				imageEntity.getFile(),
				imageEntity.getFileUrl(),
				imageEntity.getTitle(), imageEntity.getDescription(), imageEntity.getVisibility(),
				imageEntity.getAuthor());
	}

	public static ImageEntityBuilder builder() {
		return new ImageEntityBuilder();
	}

	public static class ImageEntityBuilder {

		private Long id;
		private Instant createdAt;
		private Instant updatedAt;
		private String publicId;
		private Instant publishedAt;
		private String filename;
		private MultipartFile file;
		private String fileUrl;
		private String title;
		private String description;
		private EVisibility visibility;
		private UserEntity author;

		public ImageEntityBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public ImageEntityBuilder createdAt(Instant createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public ImageEntityBuilder updatedAt(Instant updatedAt) {
			this.updatedAt = updatedAt;
			return this;
		}

		public ImageEntityBuilder publicId(String publicId) {
			this.publicId = publicId;
			return this;
		}

		public ImageEntityBuilder publishedAt(Instant publishedAt) {
			this.publishedAt = publishedAt;
			return this;
		}

		public ImageEntityBuilder filename(String filename) {
			this.filename = filename;
			return this;
		}

		public ImageEntityBuilder file(MultipartFile file) {
			this.file = file;
			return this;
		}

		public ImageEntityBuilder fileUrl(String fileUrl) {
			this.fileUrl = fileUrl;
			return this;
		}

		public ImageEntityBuilder title(String title) {
			this.title = title;
			return this;
		}

		public ImageEntityBuilder description(String description) {
			this.description = description;
			return this;
		}

		public ImageEntityBuilder visibility(EVisibility visibility) {
			this.visibility = visibility;
			return this;
		}

		public ImageEntityBuilder author(UserEntity author) {
			this.author = author;
			return this;
		}

		public ImageEntity build() {
			return new ImageEntity(id, createdAt, updatedAt, publicId, publishedAt, filename, file, fileUrl, title,
					description,
					visibility, author);
		}

	}

}