package fr.instalite.token;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

public class TokenNotFoundException extends TokenException implements Serializable {

	private static final long serialVersionUID = 1L;

	public TokenNotFoundException(String errorCode, String message, HttpStatus httpStatus) {
		super(errorCode, message, httpStatus);
	}

}
