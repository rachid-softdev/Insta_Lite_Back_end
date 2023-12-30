package fr.instalite.media.image;

import org.springframework.http.HttpStatus;

public class ImageAlreadyExistsException extends ImageException {

	public ImageAlreadyExistsException(String errorCode, String message, HttpStatus httpStatus) {
		super(errorCode, message, httpStatus);
	}
	
}
