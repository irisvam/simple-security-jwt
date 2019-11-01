package com.pattern.spring.message.response;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class JwtResponse {
	
	private String name;
	private String email;
	private Collection<? extends GrantedAuthority> authorities;
	private String token;
	private String type = "Bearer";
	
	public JwtResponse(final String token, final String name, final String email, final Collection<? extends GrantedAuthority> authorities) {
		
		super();
		this.token = token;
		this.name = name;
		this.email = email;
		this.authorities = authorities;
	}
	
	public String getName() {
		
		return name;
	}
	
	public void setName(final String name) {
		
		this.name = name;
	}
	
	public String getEmail() {
		
		return email;
	}
	
	public void setEmail(final String email) {
		
		this.email = email;
	}
	
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return authorities;
	}
	
	public void setAuthorities(final Collection<? extends GrantedAuthority> authorities) {
		
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
	
}
