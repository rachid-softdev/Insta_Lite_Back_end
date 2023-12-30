package fr.instalite.user;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends UserException implements Serializable {

	private static final long serialVersionUID = 1L;

	public UserNotFoundException(String errorCode, String message, HttpStatus httpStatus) {
		super(errorCode, message, httpStatus);
	}

}
