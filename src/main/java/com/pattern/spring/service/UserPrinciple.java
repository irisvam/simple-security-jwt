package com.pattern.spring.service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pattern.spring.model.User;

public class UserPrinciple implements UserDetails {
	
	private static final long serialVersionUID = 5026356956932846235L;
	
	private final Long id;
	
	private final String name;
	
	private final String username;
	
	private final String email;
	
	@JsonIgnore
	private final String password;
	
	private final Collection<? extends GrantedAuthority> authorities;
	
	public UserPrinciple(final Long id, final String name, final String username, final String email, final String password,
			final Collection<? extends GrantedAuthority> authorities) {
		
		this.id = id;
		this.name = name;
		this.username = username;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
	}
	
	/**
	 * Método {@code build} para geração do usuário do tipo {@link UserDetails}
	 * para reconhecimento do {@ code Spring Security} vindo da base de dados.
	 * 
	 * @param user um {@link User} com as informações do usuário na base de
	 *            dados
	 * @return {@link UserPrinciple} do tipo {@link UserDetails}
	 */
	public static UserPrinciple build(final User user) {
		
		final List<GrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName().name()))
				.collect(Collectors.toList());
		
		return new UserPrinciple(user.getId(), user.getName(), user.getUsername(), user.getEmail(), user.getPassword(), authorities);
	}
	
	/**
	 * Método {@code build} para geração do usuário do tipo {@link UserDetails}
	 * para reconhecimento do {@ code Spring Security} vindo do {@code Token}.
	 * 
	 * @param id um {@code Long} com o {@code ID} do usuário
	 * @param name um {@code String} com o {@code nome} do usuário
	 * @param username um {@code String} com o {@code username} do usuário
	 * @param email um {@code String} com o {@code e-mail} do usuário
	 * @param lista um {@code List<String>} com a lista dos {@code Roles}
	 * @return {@link UserPrinciple} do tipo {@link UserDetails}
	 */
	public static UserPrinciple build(final Long id, final String name, final String username, final String email, final List<String> lista) {
		
		if (null == id || null == name || null == email || null == lista) {
			
			throw new IllegalArgumentException();
		}
		
		return new UserPrinciple(id, name, username, email, null, lista.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
	}
	
	public Long getId() {
		
		return id;
	}
	
	public String getName() {
		
		return name;
	}
	
	public String getEmail() {
		
		return email;
	}
	
	@Override
	public String getUsername() {
		
		return username;
	}
	
	@Override
	public String getPassword() {
		
		return password;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return authorities;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		
		return true;
	}
	
	@Override
	public boolean equals(final Object o) {
		
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		
		final UserPrinciple user = (UserPrinciple) o;
		return Objects.equals(id, user.id);
	}
	
}
