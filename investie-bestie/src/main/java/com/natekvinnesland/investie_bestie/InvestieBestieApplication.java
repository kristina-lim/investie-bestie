package com.natekvinnesland.investie_bestie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class InvestieBestieApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvestieBestieApplication.class, args);
	}
}
