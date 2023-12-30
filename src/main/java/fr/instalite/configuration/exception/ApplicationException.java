package fr.instalite.configuration.exception;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException {

	private final String errorCode;

	private final String message;

	private final HttpStatus httpStatus;

	public ApplicationException(String errorCode, String message, HttpStatus httpStatus) {
		this.errorCode = errorCode;
		this.message = message;
		this.httpStatus = httpStatus;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		return message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
