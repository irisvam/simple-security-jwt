package com.pattern.spring.controller;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pattern.spring.enums.MessageProperties;
import com.pattern.spring.enums.RoleName;
import com.pattern.spring.exception.ParameterNotValidException;
import com.pattern.spring.message.request.SignUpForm;
import com.pattern.spring.message.response.ResponseMessage;
import com.pattern.spring.model.Role;
import com.pattern.spring.model.User;
import com.pattern.spring.repositories.RoleRepository;
import com.pattern.spring.repositories.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPIs {

	@Autowired
	private MessageSource sourceMessage;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder encoder;

	private final Locale locale = new Locale("pt_br");

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody final SignUpForm signUpRequest) {

		if (userRepository.existsByUsername(signUpRequest.getUsername())) {

			throw new ParameterNotValidException(sourceMessage.getMessage(MessageProperties.POST_BAD_REQUEST.getDescricao(),
					new Object[] {"username", signUpRequest.getUsername()}, locale));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {

			throw new ParameterNotValidException(sourceMessage.getMessage(MessageProperties.POST_BAD_REQUEST.getDescricao(),
					new Object[] {"email", signUpRequest.getEmail()}, locale));
		}

		// Creating user's account
		final User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));

		final Set<Role> roles = new HashSet<Role>();

		signUpRequest.getRole().forEach(role -> {
			switch (role) {
				case ROLE_ADMIN:
					final Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
					roles.add(adminRole);

					break;
				default:
					final Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
					roles.add(userRole);
			}
		});

		user.setRoles(roles);
		userRepository.save(user);

		return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
	}

	@GetMapping("/")
	public String access() {

		return ">>> Autorized without token";
	}

}
