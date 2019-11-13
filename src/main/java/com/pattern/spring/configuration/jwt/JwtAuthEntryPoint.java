package com.pattern.spring.configuration.jwt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * Classe para tratar erros as tentativas de acesso passados pelos filtros após validação de {@code Token}.
 *
 * @see AuthenticationEntryPoint
 */
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

	@Autowired
	private HandlerExceptionResolver resolver;

	@Override
	public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException)
			throws IOException, ServletException {

		resolver.resolveException(request, response, null, authException);
	}

}
