package com.catis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class MtcApplication {

	public static void main(String[] args) {
		SpringApplication.run(MtcApplication.class, args);
	}

}
