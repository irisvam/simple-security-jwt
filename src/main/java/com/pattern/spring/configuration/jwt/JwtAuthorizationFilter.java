package com.pattern.spring.configuration.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.pattern.spring.service.UserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * Classe {@code filter} para fazer verificações do {@code Token} e colocar
 * usuáro para fazer validações nos {@code endyPoints}.
 *
 * @see BasicAuthenticationFilter
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
	
	private final JwtProvider jwtProvider;
	
	private final HandlerExceptionResolver resolver;
	
	private final UserDetailsServiceImpl userDetailsService;
	
	/**
	 * Construtor da classe para prover os atributos internos necessários.
	 *
	 * @param authManager uma classe {@link AuthenticationManager} para
	 *            tratamento do usuário autenticado
	 * @param jwtProvider uma classe {@link JwtProvider} com as informações para
	 *            gerar o {@code Token}
	 * @param resolver uma classe {@link HandlerExceptionResolver} para
	 *            tratativa dos erros via {@code JSON}
	 * @param userDetailsService uma classe {@link UserDetailsServiceImpl} para
	 *            consultar o usuário enviado pelo {@code Token}
	 */
	public JwtAuthorizationFilter(final AuthenticationManager authManager, final JwtProvider jwtProvider, final HandlerExceptionResolver resolver,
			final UserDetailsServiceImpl userDetailsService) {
		
		super(authManager);
		this.jwtProvider = jwtProvider;
		this.resolver = resolver;
		this.userDetailsService = userDetailsService;
	}
	
	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		
		final String authHeader = request.getHeader("Authorization");
		
		if (null != authHeader && authHeader.startsWith("Bearer ")) {
			
			final String username = verificarToken(authHeader.replace("Bearer ", ""), request, response);
			
			if (null != username) {
				
				final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				
				final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
						userDetails.getAuthorities());
				
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		
		chain.doFilter(request, response);
	}
	
	/**
	 * Método para recuperar o usuário do {@code Token}
	 * 
	 * @param jwt um {@code String} com o {@code Token}
	 * @param request um {@link HttpServletRequest} com as informações do
	 *            servlet HTTP request
	 * @param response um {@link HttpServletResponse} com as informações do
	 *            servlet HTTP response
	 * @return {@code String} com o login do usuário
	 */
	private String verificarToken(final String jwt, final HttpServletRequest request, final HttpServletResponse response) {
		
		String username = null;
		
		try {
			
			username = jwtProvider.extractUsername(jwt);
			
		} catch (final SignatureException e) {
			resolver.resolveException(request, response, null, new AuthenticationServiceException("Token inválido!", e));
		} catch (final MalformedJwtException e) {
			resolver.resolveException(request, response, null, new AuthenticationServiceException("Token mal formatado!", e));
		} catch (final ExpiredJwtException e) {
			resolver.resolveException(request, response, null, new AuthenticationServiceException("Token expirado!", e));
		} catch (final UnsupportedJwtException e) {
			resolver.resolveException(request, response, null, new AuthenticationServiceException("Token não suportado!", e));
		} catch (final IllegalArgumentException e) {
			resolver.resolveException(request, response, null, new AuthenticationServiceException("Tentativa de ler o Token não finalizado!", e));
		}
		
		return username;
	}
	
}
