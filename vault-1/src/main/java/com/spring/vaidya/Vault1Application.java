package com.spring.vaidya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Vault1Application {

	public static void main(String[] args) {
		SpringApplication.run(Vault1Application.class, args);
	}

}
