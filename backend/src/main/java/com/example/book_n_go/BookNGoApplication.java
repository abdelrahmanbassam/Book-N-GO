package com.example.book_n_go;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import tech.ailef.snapadmin.external.SnapAdminAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

@ImportAutoConfiguration(SnapAdminAutoConfiguration.class)
public class BookNGoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookNGoApplication.class, args);
	}

}
