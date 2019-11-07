package com.pattern.spring.configuration.jwt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * Classe para tratar erros de Acesso Negado passados pelos filtros após
 * validação de {@code Token}.
 *
 * @see AccessDeniedHandler
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
	
	@Autowired
	private HandlerExceptionResolver resolver;
	
	@Override
	public void handle(final HttpServletRequest request, final HttpServletResponse response, final AccessDeniedException accessDeniedException)
			throws IOException, ServletException {
		
		resolver.resolveException(request, response, null, accessDeniedException);
	}
	
}
