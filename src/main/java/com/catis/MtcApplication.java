package com.catis;

import java.util.Collections;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableJpaAuditing
@EnableSwagger2

public class MtcApplication {

	public static void main(String[] args) {
		SpringApplication.run(MtcApplication.class, args);
	}
	
	@Bean
	public Docket swaggerConfiguration() {
		
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.paths(PathSelectors.ant("/api/v1/**"))
				.apis(RequestHandlerSelectors.basePackage("com.catis"))
				.build()
				.apiInfo(apiDetails());
	}
	private ApiInfo apiDetails() {
		return new ApiInfo(
				"MTC API",
				"API de l'application Management Tool Catis",
				"0.0.1",
				"Tous droits réservés",
				new springfox.documentation.service.Contact("Prooftag Catis", "www.prooftagcatis.com", "contact@prooftagcatis.com"),
				"API license",
				"www.prooftagcatis.com",
				Collections.emptyList());
				
	}
}
