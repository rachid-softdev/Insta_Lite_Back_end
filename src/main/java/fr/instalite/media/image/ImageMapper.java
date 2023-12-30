package fr.instalite.media.image;

import java.util.List;

public interface ImageMapper {

	public ImageResponse toImageResponse(ImageCreateRequest imageCreateRequest);

	public ImageResponse toImageResponse(ImageUpdateRequest imageUpdateRequest);

	public ImageResponse toImageResponse(ImageEntity imageEntity);

	public List<ImageResponse> toImageResponseList(List<ImageEntity> imageEntities);

}
