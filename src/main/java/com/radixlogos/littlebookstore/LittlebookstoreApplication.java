package com.radixlogos.littlebookstore;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LittlebookstoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(LittlebookstoreApplication.class, args);
	}

	@PostConstruct
	public void test() {

	}
}
