package com.neobank.neobankapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = "com.neobank")
@EnableJpaRepositories(basePackages = "com.neobank")
@EntityScan(basePackages = "com.neobank")
@SpringBootApplication
public class NeobankAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(NeobankAppApplication.class, args);
	}
}
