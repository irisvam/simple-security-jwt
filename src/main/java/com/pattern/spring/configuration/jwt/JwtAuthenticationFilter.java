package com.pattern.spring.configuration.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pattern.spring.message.request.LoginForm;
import com.pattern.spring.service.UserPrinciple;

/**
 * Classe {@code filter} para tratar o login de usuários na {@code URI:/login}.
 *
 * @see UsernamePasswordAuthenticationFilter
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final JwtProvider jwtProvider;

	private final HandlerExceptionResolver resolver;

	/**
	 * Construtor da classe para prover os atributos internos necessários.
	 *
	 * @param jwtProvider uma classe {@link JwtProvider} com as informações para gerar o {@code Token}
	 * @param resolver uma classe {@link HandlerExceptionResolver} para tratativa dos erros via {@code JSON}
	 */
	public JwtAuthenticationFilter(final JwtProvider jwtProvider, final HandlerExceptionResolver resolver) {

		this.jwtProvider = jwtProvider;
		this.resolver = resolver;
	}

	@Override
	public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) throws AuthenticationException {

		try {

			final LoginForm credentials = new ObjectMapper().readValue(request.getInputStream(), LoginForm.class);

			return getAuthenticationManager()
					.authenticate(new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));

		} catch (final JsonParseException e) {
			throw new AuthenticationServiceException("Erro no parseamento dos dados!", e);
		} catch (final JsonMappingException e) {
			throw new AuthenticationServiceException("Erro no mapeamento dos dados!", e);
		} catch (final IOException e) {
			throw new AuthenticationServiceException("Erro na leitura dos dados!", e);
		}
	}

	@Override
	protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain,
			final Authentication authResult) throws IOException, ServletException {

		final String jwt = jwtProvider.generateToken((UserPrinciple) authResult.getPrincipal());

		response.addHeader("Authorization", "Bearer " + jwt);
	}

	@Override
	protected void unsuccessfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException failed) throws IOException, ServletException {

		resolver.resolveException(request, response, null, failed);
	}

}
