package fr.instalite.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper, Serializable {

	private static final long serialVersionUID = 1L;

	public UserEntity toUserEntity(UserCreateRequest userCreateRequest) {
		if (userCreateRequest == null) {
			userCreateRequest = new UserCreateRequest();
		}
		final UserEntity userEntity = new UserEntity();
		userEntity.setFirstname(userCreateRequest.getFirstname());
		userEntity.setLastname(userCreateRequest.getLastname());
		userEntity.setEmail(userCreateRequest.getEmail());
		userEntity.setPassword(userCreateRequest.getPassword());
		userEntity.setRole(ERole.fromValue(userCreateRequest.getRole()));
		return userEntity;
	}

	public UserEntity toUserEntity(UserUpdateRequest userUpdateRequest) {
		if (userUpdateRequest == null) {
			userUpdateRequest = new UserUpdateRequest();
		}
		final UserEntity userEntity = new UserEntity();
		userEntity.setPublicId(userUpdateRequest.getPublicId());
		userEntity.setFirstname(userUpdateRequest.getFirstname());
		userEntity.setLastname(userUpdateRequest.getLastname());
		userEntity.setEmail(userUpdateRequest.getEmail());
		userEntity.setPassword(userUpdateRequest.getPassword());
		userEntity.setRole(ERole.fromValue(userUpdateRequest.getRole()));
		return userEntity;
	}

	public UserEntity toUserEntity(UserEntity userEntity, UserProfileUpdateRequest userUpdateProfileRequest) {
		if (userUpdateProfileRequest == null) {
			userUpdateProfileRequest = new UserProfileUpdateRequest();
		}
		userEntity.setPublicId(userUpdateProfileRequest.getPublicId());
		userEntity.setFirstname(userUpdateProfileRequest.getFirstname());
		userEntity.setLastname(userUpdateProfileRequest.getLastname());
		userEntity.setEmail(userUpdateProfileRequest.getEmail());
		return userEntity;
	}

	public BaseUserResponse toBaseUserResponse(UserEntity userEntity) {
		if (userEntity == null) {
			return new BaseUserResponse();
		}
		final BaseUserResponse userResponse = new BaseUserResponse();
		userResponse.setCreatedAt(userEntity.getCreatedAt());
		userResponse.setUpdatedAt(userEntity.getUpdatedAt());
		userResponse.setPublicId(userEntity.getPublicId());
		userResponse.setFirstname(userEntity.getFirstname());
		userResponse.setLastname(userEntity.getLastname());
		userResponse.setEmail(userEntity.getEmail());
		userResponse.setRole(userEntity.getRole() != null ? userEntity.getRole().name() : null);
		return userResponse;
	}

	public List<BaseUserResponse> toBaseUserResponseList(List<UserEntity> userEntities) {
		if (userEntities == null) {
			return new ArrayList<BaseUserResponse>();
		}
		List<BaseUserResponse> userResponses = new ArrayList<>();
		for (UserEntity userEntity : userEntities) {
			userResponses.add(toBaseUserResponse(userEntity));
		}
		return userResponses;
	}

}
