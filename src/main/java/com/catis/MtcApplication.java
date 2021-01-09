package com.catis;

//import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;



@SpringBootApplication
public class MtcApplication {

	public static void main(String[] args) {
		SpringApplication.run(MtcApplication.class, args);
	}
	
	
	
}
