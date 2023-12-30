package fr.instalite.user;

import java.io.Serializable;

public class UserResponse extends BaseUserResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "UserResponse{" +
				super.toString() +
				'}';
	}

}
