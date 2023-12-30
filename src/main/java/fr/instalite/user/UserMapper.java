package fr.instalite.user;

import java.util.List;

public interface UserMapper {

	public UserEntity toUserEntity(UserCreateRequest userCreateRequest);

	public UserEntity toUserEntity(UserUpdateRequest userUpdateRequest);

	public UserEntity toUserEntity(UserEntity userEntity, UserProfileUpdateRequest userUpdateProfileRequest);

	public BaseUserResponse toBaseUserResponse(UserEntity userEntity);

	public List<BaseUserResponse> toBaseUserResponseList(List<UserEntity> userEntities);

}
