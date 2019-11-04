package com.pattern.spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 5617529997351342228L;
	
	public ResourceNotFoundException(final String message) {
		
		super(message);
	}
	
}
