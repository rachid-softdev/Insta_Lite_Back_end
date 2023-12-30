package fr.instalite.media.image;

import fr.instalite.common.EVisibility;
import fr.instalite.user.UserEntity;
import fr.instalite.user.UserMapper;
import fr.instalite.user.BaseUserResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImageMapperImpl implements ImageMapper, Serializable {

	private static final long serialVersionUID = 1L;

	private final UserMapper userMapper;

	public ImageMapperImpl(@Autowired UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	public ImageResponse toImageResponse(ImageCreateRequest imageCreateRequest) {
		if (imageCreateRequest == null) {
			return new ImageResponse();
		}
		final ImageResponse imageResponse = new ImageResponse();
		imageResponse.setTitle(imageCreateRequest.getTitle());
		imageResponse.setDescription(imageCreateRequest.getDescription());
		imageResponse.setVisibility(
				EVisibility.fromValue(imageCreateRequest.getVisibility()));
		imageResponse.setFileUrl(null);
		imageResponse.setAuthor(imageCreateRequest.getAuthor());
		return imageResponse;
	}

	public ImageResponse toImageResponse(ImageUpdateRequest imageUpdateRequest) {
		if (imageUpdateRequest == null) {
			return new ImageResponse();
		}
		final ImageResponse imageResponse = new ImageResponse();
		imageResponse.setPublishedAt(imageUpdateRequest.getPublishedAt());
		imageResponse.setPublicId(imageUpdateRequest.getPublicId());
		imageResponse.setPublishedAt(imageUpdateRequest.getPublishedAt());
		imageResponse.setTitle(imageUpdateRequest.getTitle());
		imageResponse.setDescription(imageUpdateRequest.getDescription());
		imageResponse.setVisibility(
				EVisibility.fromValue(imageUpdateRequest.getVisibility()));
		imageResponse.setAuthor(imageUpdateRequest.getAuthor());
		imageResponse.setFileUrl(null);
		return imageResponse;
	}

	public ImageResponse toImageResponse(ImageEntity imageEntity) {
		if (imageEntity == null) {
			return new ImageResponse();
		}
		final ImageResponse imageResponse = new ImageResponse();
		imageResponse.setCreatedAt(imageEntity.getCreatedAt());
		imageResponse.setUpdatedAt(imageEntity.getUpdatedAt());
		imageResponse.setPublicId(imageEntity.getPublicId());
		imageResponse.setPublishedAt(imageEntity.getPublishedAt());
		imageResponse.setFileUrl(imageEntity.getFilePath());
		imageResponse.setTitle(imageEntity.getTitle());
		imageResponse.setDescription(imageEntity.getDescription());
		imageResponse.setVisibility(imageEntity.getVisibility());
		imageResponse.setFileUrl(imageEntity.getFileUrl());
		imageResponse.setAuthor(userMapper.toBaseUserResponse(imageEntity.getAuthor()));
		return imageResponse;
	}

	public List<ImageResponse> toImageResponseList(
			List<ImageEntity> imageEntities) {
		if (imageEntities == null) {
			return new ArrayList<ImageResponse>();
		}
		List<ImageResponse> imageResponses = new ArrayList<>();
		for (ImageEntity imageEntity : imageEntities) {
			imageResponses.add(toImageResponse(imageEntity));
		}
		return imageResponses;
	}

	private class ImageMapperHelper {

		public BaseUserResponse toUserResponse(UserEntity userEntity) {
			if (userEntity == null) {
				return null;
			}
			final BaseUserResponse userResponse = new BaseUserResponse();
			userResponse.setCreatedAt(userEntity.getCreatedAt());
			userResponse.setUpdatedAt(userEntity.getUpdatedAt());
			userResponse.setPublicId(userEntity.getPublicId());
			userResponse.setFirstname(userEntity.getFirstname());
			userResponse.setLastname(userEntity.getLastname());
			userResponse.setEmail(userEntity.getEmail());
			userResponse.setRole(userEntity.getRole() != null ? userEntity.getRole().name() : "");
			return userResponse;
		}
	}

}
