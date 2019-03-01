package com.example.rest.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableEurekaClient
@EnableJpaRepositories(basePackages = "com.example.rest.service.repository")
@SpringBootApplication
public class TechprimersStockDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechprimersStockDbApplication.class, args);
	}
}
