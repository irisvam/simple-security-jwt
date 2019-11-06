package com.pattern.spring.configuration.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {
	
	@Value("${app.jwtSecret}")
	private String jwtSecret;
	
	@Value("${app.jwtExpiration}")
	private int jwtExpiration;
	
	public String extractUsername(final String token) {
		
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration(final String token) {
		
		return extractClaim(token, Claims::getExpiration);
	}
	
	public <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver) {
		
		final Claims claims = extractAllClaims(token);
		
		return claimsResolver.apply(claims);
	}
	
	private Claims extractAllClaims(final String token) {
		
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
	}
	
	private Boolean isTokenExpired(final String token) {
		
		return extractExpiration(token).before(new Date());
	}
	
	public String generateToken(final UserDetails userDetails) {
		
		final Map<String, Object> claims = new HashMap<String, Object>();
		
		return createToken(claims, userDetails.getUsername());
	}
	
	private String createToken(final Map<String, Object> claims, final String username) {
		
		return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * jwtExpiration)).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}
	
	public Boolean validateToken(final String token, final UserDetails userDetails) {
		
		final String username = extractUsername(token);
		
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
}
