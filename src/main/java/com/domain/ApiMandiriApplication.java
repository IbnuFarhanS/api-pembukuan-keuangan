package com.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;

import com.domain.helpers.password.PasswordEncoderExample;

@SpringBootApplication
public class ApiMandiriApplication {
    // ...

    @Bean
	public PasswordEncoderExample passwordEncoder() {
		return new PasswordEncoderExample();
	}

    public static void main(String[] args) {
        SpringApplication.run(ApiMandiriApplication.class, args);
    }

}

