package fr.instalite.media.image;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import fr.instalite.configuration.exception.ApplicationException;

public class ImageException extends ApplicationException implements Serializable {

	private static final long serialVersionUID = 1L;

	public ImageException(String errorCode, String message, HttpStatus httpStatus) {
		super(errorCode, message, httpStatus);
	}

}
