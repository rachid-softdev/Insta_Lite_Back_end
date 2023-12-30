package fr.instalite.media.image;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

public class ImageNotFoundException extends ImageException implements Serializable {

	private static final long serialVersionUID = 1L;

	public ImageNotFoundException(String errorCode, String message, HttpStatus httpStatus) {
		super(errorCode, message, httpStatus);
	}

}
