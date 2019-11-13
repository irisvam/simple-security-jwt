package com.pattern.spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Classe {@code Exception} que {@code extends} {@link RuntimeException} para serviços que retornem um
 * {@code HttpStatus.NOT_FOUND}.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5617529997351342228L;

	/**
	 * Construtor padrão para essa excessão.
	 *
	 * @param message {@code String} com a mensagem desejada
	 */
	public ResourceNotFoundException(final String message) {

		super(message);
	}

}
