package fr.instalite.token;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import fr.instalite.configuration.exception.ApplicationException;

public class TokenException extends ApplicationException implements Serializable {

	private static final long serialVersionUID = 1L;

	public TokenException(String errorCode, String message, HttpStatus httpStatus) {
		super(errorCode, message, httpStatus);
	}

}
