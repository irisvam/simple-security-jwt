package com.pattern.spring.message.request;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.pattern.spring.enums.RoleName;

/**
 * Dados bases para inscrição de novos usuários para Login.
 */
public class SignUpForm {

	@NotBlank
	@Size(min = 3, max = 50)
	private String name;

	@NotBlank
	@Size(min = 3, max = 50)
	private String username;

	@NotBlank
	@Size(max = 60)
	@Email
	private String email;

	private Set<RoleName> role;

	@NotBlank
	@Size(min = 6, max = 40)
	private String password;

	public String getName() {

		return name;
	}

	public void setName(final String name) {

		this.name = name;
	}

	public String getUsername() {

		return username;
	}

	public void setUsername(final String username) {

		this.username = username;
	}

	public String getEmail() {

		return email;
	}

	public void setEmail(final String email) {

		this.email = email;
	}

	public Set<RoleName> getRole() {

		return role;
	}

	public void setRole(final Set<RoleName> role) {

		this.role = role;
	}

	public String getPassword() {

		return password;
	}

	public void setPassword(final String password) {

		this.password = password;
	}

}
