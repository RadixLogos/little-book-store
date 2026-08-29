package com.radixlogos.littlebookstore;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class LittlebookstoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(LittlebookstoreApplication.class, args);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String password = encoder.encode("opfsspx2021cwb");
		System.out.println("senha: " + password);
	}

	@PostConstruct
	public void test() {

	}
}
