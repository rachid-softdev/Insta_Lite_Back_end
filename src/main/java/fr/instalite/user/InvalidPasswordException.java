package fr.instalite.user;

import org.springframework.http.HttpStatus;

import fr.instalite.configuration.exception.ApplicationException;

public class InvalidPasswordException extends ApplicationException {

	public InvalidPasswordException(String errorCode, String message, HttpStatus httpStatus) {
		super(errorCode, message, httpStatus);
	}

}
