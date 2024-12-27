package com.example.auth_service;

import com.example.auth_service.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AuthServiceApplication {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner updatePasswords() {
		return args -> {
			userRepository.findAll().forEach(user -> {
				if (!user.getPassword().startsWith("$2a$")) { // Only encode if not already BCrypt
					user.setPassword(passwordEncoder.encode(user.getPassword()));
					userRepository.save(user);
				}
			});
		};
	}

}
