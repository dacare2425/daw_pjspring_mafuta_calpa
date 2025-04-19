package edu.fje.daw2.sm72;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity(prePostEnabled = true)
public class Sm72Application {

	public static void main(String[] args) {
		SpringApplication.run(Sm72Application.class, args);
	}
}