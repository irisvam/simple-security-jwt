package com.pattern.spring.configuration.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.pattern.spring.service.UserPrinciple;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtProvider {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
	
	@Value("${app.jwtSecret}")
	private String jwtSecret;
	
	@Value("${app.jwtExpiration}")
	private int jwtExpiration;
	
	public String generateJwtToken(final Authentication authentication) {
		
		final UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();
		
		return Jwts.builder().setSubject((userPrincipal.getUsername())).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpiration * 1000)).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}
	
	public boolean validateJwtToken(final String authToken) {
		
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (final SignatureException e) {
			logger.error("Invalid JWT signature -> Message: {} ", e);
		} catch (final MalformedJwtException e) {
			logger.error("Invalid JWT token -> Message: {}", e);
		} catch (final ExpiredJwtException e) {
			logger.error("Expired JWT token -> Message: {}", e);
		} catch (final UnsupportedJwtException e) {
			logger.error("Unsupported JWT token -> Message: {}", e);
		} catch (final IllegalArgumentException e) {
			logger.error("JWT claims string is empty -> Message: {}", e);
		}
		return false;
	}
	
	public String getUserNameFromJwtToken(final String token) {
		
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}
	
}
