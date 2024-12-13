package com.example.book_n_go;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.book_n_go.config.AuthService;
import com.example.book_n_go.dto.SignupRequest;
import com.example.book_n_go.enums.Role;

@SpringBootApplication
public class BookNGoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookNGoApplication.class, args);
	}

	@Bean
	public CommandLineRunner clr(
		AuthService authService
	){
		return (args) -> {
			var admin = SignupRequest.builder()
				.email("admin")
				.password("admin")
				.name("admin")
				.phone("admin")
				.role(Role.ADMIN)
				.build();
			System.out.println("Admin created: " + authService.signup(admin).getToken() + "\n");

			var provider = SignupRequest.builder()
				.email("electra")
				.password("electra")
				.name("electra")
				.phone("electra")
				.role(Role.PROVIDER)
				.build();
			System.out.println("Provider created: " + authService.signup(provider).getToken() + "\n");

			var client = SignupRequest.builder()
				.email("client")
				.password("client")
				.name("client")
				.phone("client")
				.role(Role.CLIENT)
				.build();
			System.out.println("Client created: " + authService.signup(client).getToken() + "\n");
		};
	}
}
