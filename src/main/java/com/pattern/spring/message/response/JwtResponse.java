package com.pattern.spring.message.response;

public class JwtResponse {
	
	private String token;
	private String type = "Bearer";
	
	public JwtResponse(final String token) {
		
		super();
		this.token = token;
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
