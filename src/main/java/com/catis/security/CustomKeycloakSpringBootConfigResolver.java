package com.catis.security;

import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.catis.model.configuration.AuditorAwareImpl;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * @version 0.0.1
 * @since 0.0.1
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class CustomKeycloakSpringBootConfigResolver extends KeycloakSpringBootConfigResolver {

    private final KeycloakDeployment keycloakDeployment;

    public CustomKeycloakSpringBootConfigResolver(KeycloakSpringBootProperties properties) {
        keycloakDeployment = KeycloakDeploymentBuilder.build(properties);
    }

    @Override
    public KeycloakDeployment resolve(HttpFacade.Request facade) {
        return keycloakDeployment;
    }

    @Bean
    AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

}

