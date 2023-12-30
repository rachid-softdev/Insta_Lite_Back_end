package fr.instalite.authentication;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import fr.instalite.configuration.exception.ApplicationException;

public class AuthenticationException extends ApplicationException implements Serializable {

	private static final long serialVersionUID = 1L;

	public AuthenticationException(String errorCode, String message, HttpStatus httpStatus) {
		super(errorCode, message, httpStatus);
	}

}
