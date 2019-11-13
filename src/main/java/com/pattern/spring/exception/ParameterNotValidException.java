package com.pattern.spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Classe {@code Exception} que {@code extends} {@link RuntimeException} para serviços que retornem um
 * {@code HttpStatus.BAD_REQUEST}.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ParameterNotValidException extends RuntimeException {

	private static final long serialVersionUID = 6356009514009465222L;

	/**
	 * Construtor padrão para essa excessão.
	 *
	 * @param message {@code String} com a mensagem desejada
	 */
	public ParameterNotValidException(final String message) {

		super(message);
	}

}
