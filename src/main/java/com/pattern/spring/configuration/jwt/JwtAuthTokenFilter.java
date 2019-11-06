package com.pattern.spring.configuration.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pattern.spring.service.UserDetailsServiceImpl;

@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
			throws ServletException, IOException {
		
		final String authHeader = request.getHeader("Authorization");
		
		if (null != authHeader && authHeader.startsWith("Bearer ")) {
			
			final String jwt = authHeader.replace("Bearer ", "");
			final String username = jwtProvider.extractUsername(jwt);
			
			if (null != username && null == SecurityContextHolder.getContext().getAuthentication()) {
				
				final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				
				if (jwtProvider.validateToken(jwt, userDetails)) {
					
					final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
							userDetails.getAuthorities());
					
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}
		
		filterChain.doFilter(request, response);
	}
	
}
