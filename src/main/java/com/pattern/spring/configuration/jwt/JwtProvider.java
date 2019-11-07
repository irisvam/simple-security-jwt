package com.pattern.spring.configuration.jwt;

import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Classe responsável a prover os métodos para geração e verificação dos
 * {@code Tokens} da blibioteca do {@code JWT}.
 */
@Component
public class JwtProvider {
	
	@Value("${app.jwtSecret}")
	private String jwtSecret;
	
	@Value("${app.jwtExpiration}")
	private int jwtExpiration;
	
	public String extractUsername(final String token) {
		
		return extractClaim(token, Claims::getSubject);
	}
	
	public <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver) {
		
		final Claims claims = extractAllClaims(token);
		
		return claimsResolver.apply(claims);
	}
	
	private Claims extractAllClaims(final String token) {
		
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
	}
	
	public String generateToken(final String username) {
		
		return Jwts.builder().setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * jwtExpiration)).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}
	
}
