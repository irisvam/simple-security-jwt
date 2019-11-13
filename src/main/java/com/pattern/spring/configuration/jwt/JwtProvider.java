package com.pattern.spring.configuration.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pattern.spring.service.UserPrinciple;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Classe responsável a prover os métodos para geração e verificação dos {@code Tokens} da blibioteca do {@code JWT}.
 */
@Component
public class JwtProvider {

	@Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwtExpiration}")
	private int jwtExpiration;

	/**
	 * Método que verifica se o {@code Token} está ativo e se é válido.
	 *
	 * @param token um {@link String} com o {@code Token}
	 * @return {@link Jws}{@code <}{@link Claims}{@code >} quando autenticado
	 */
	public Jws<Claims> autenticarToken(final String token) {

		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
	}

	/**
	 * Método para desmembrar os dados do {@code Token} em um usuário para Autenticação no webservice.
	 *
	 * @param token um {@link String} com o {@code Token}
	 * @return {@link UserPrinciple} com o {@code UserDetails}
	 */
	@SuppressWarnings("unchecked")
	public UserPrinciple extractUser(final String token) {

		final Claims claims = autenticarToken(token).getBody();

		return UserPrinciple.build(claims.get("identify", Long.class), claims.get("name", String.class), claims.getSubject(),
				claims.get("email", String.class), claims.get("autorities", List.class));
	}

	/**
	 * Método chamado para gerar o {@code Token} com os dados do usuário.
	 *
	 * @param userDetail um {@link UserPrinciple} com o {@code UserDetails}
	 * @return {@link String} com o {@code Token}
	 */
	public String generateToken(final UserPrinciple userDetail) {

		final Map<String, Object> claims = new HashMap<String, Object>();

		claims.put("identify", userDetail.getId());
		claims.put("name", userDetail.getName());
		claims.put("email", userDetail.getEmail());
		claims.put("autorities", userDetail.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.toList()));

		return Jwts.builder().addClaims(claims).setSubject(userDetail.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * jwtExpiration)).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

}
