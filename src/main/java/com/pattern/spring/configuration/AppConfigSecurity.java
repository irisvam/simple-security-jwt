package com.pattern.spring.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.pattern.spring.configuration.jwt.JwtAccessDeniedHandler;
import com.pattern.spring.configuration.jwt.JwtAuthEntryPoint;
import com.pattern.spring.configuration.jwt.JwtAuthenticationFilter;
import com.pattern.spring.configuration.jwt.JwtAuthorizationFilter;
import com.pattern.spring.configuration.jwt.JwtProvider;
import com.pattern.spring.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppConfigSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	private JwtAuthEntryPoint authEntryHandler;
	
	@Autowired
	private JwtAccessDeniedHandler accessDeniedHandler;
	
	@Autowired
	JwtProvider jwtProvider;
	
	@Autowired
	private HandlerExceptionResolver resolver;
	
	@Override
	public void configure(final AuthenticationManagerBuilder authBuilder) throws Exception {
		
		authBuilder.authenticationProvider(authenticationProvider());
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		
		final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setPasswordEncoder(passwordEncoder());
		authProvider.setUserDetailsService(userDetailsService);
		
		return authProvider;
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		
		return super.authenticationManagerBean();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		
		http.cors().and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.antMatchers("/api/auth/**").permitAll().anyRequest().authenticated().and().formLogin().permitAll().and().logout().permitAll().and()
				.addFilter(jwtAuthenticationFilter()).addFilter(jwtAuthorizationFilter()).exceptionHandling()
				.authenticationEntryPoint(authEntryHandler).and().exceptionHandling().accessDeniedHandler(accessDeniedHandler);
	}
	
	/**
	 * Criação do filter de Autenticação.
	 *
	 * @return {@link JwtAuthenticationFilter}
	 * @throws Exception para tratamento de erro
	 */
	private JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
		
		final JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtProvider, resolver);
		
		filter.setAuthenticationManager(authenticationManagerBean());
		
		return filter;
	}
	
	/**
	 * Criação do filter de Autorização.
	 *
	 * @return {@link JwtAuthorizationFilter}
	 * @throws Exception para tratamento de erro
	 */
	private JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
		
		return new JwtAuthorizationFilter(authenticationManagerBean(), jwtProvider, resolver, userDetailsService);
	}
	
}
