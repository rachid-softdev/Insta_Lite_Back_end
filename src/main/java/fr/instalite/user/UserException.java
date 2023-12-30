package fr.instalite.user;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import fr.instalite.configuration.exception.ApplicationException;

public class UserException extends ApplicationException implements Serializable {

	private static final long serialVersionUID = 1L;

	public UserException(String errorCode, String message, HttpStatus httpStatus) {
		super(errorCode, message, httpStatus);
	}

}
