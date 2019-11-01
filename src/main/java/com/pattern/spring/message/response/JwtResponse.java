package com.pattern.spring.message.response;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class JwtResponse {
	
	private String token;
	private String type = "Bearer";
	private String username;
	private Collection<? extends GrantedAuthority> authorities;
	
	public JwtResponse(final String token, final String username, final Collection<? extends GrantedAuthority> authorities) {
		
		super();
		this.token = token;
		this.username = username;
		this.authorities = authorities;
	}
	
	public String getToken() {
		
		return token;
	}
	
	public void setToken(final String token) {
		
		this.token = token;
	}
	
	public String getType() {
		
		return type;
	}
	
	public void setType(final String type) {
		
		this.type = type;
	}
	
	public String getUsername() {
		
		return username;
	}
	
	public void setUsername(final String username) {
		
		this.username = username;
	}
	
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return authorities;
	}
	
	public void setAuthorities(final Collection<? extends GrantedAuthority> authorities) {
		
		this.authorities = authorities;
	}
	
}
