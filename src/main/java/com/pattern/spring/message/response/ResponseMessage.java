package com.pattern.spring.message.response;

public class ResponseMessage {
	
	private String message;
	
	public ResponseMessage(final String message) {
		
		super();
		this.message = message;
	}
	
	public String getMessage() {
		
		return message;
	}
	
	public void setMessage(final String message) {
		
		this.message = message;
	}
	
}
