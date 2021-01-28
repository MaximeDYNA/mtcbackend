package com.catis.objectTemporaire;

import javax.servlet.http.HttpServletRequest;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;

import com.catis.model.Controleur;
import com.catis.service.ControleurService;

public class UserInfoIn {
	@Autowired
	HttpServletRequest request;
	
	
		
	
	public static UserDTO getInfosControleur(Controleur controleur, HttpServletRequest request, String serverUrl, String realm ) {
	
		
		KeycloakSecurityContext context = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
	    Keycloak keycloak = KeycloakBuilder
	        .builder()
	        .clientId("admin-cli")
	        .serverUrl(serverUrl)
	        .realm(realm)
	        .username("tchoko")
	        .password("tchoko")
	        
	        
	        .build();
	    UserResource userResource = keycloak.realm(realm).users().get(controleur.getKeycloakId());
	    

	    UserDTO user = new UserDTO();
	    user.setId(userResource.toRepresentation().getId());
        user.setNom(userResource.toRepresentation().getLastName());
        user.setPrenom(userResource.toRepresentation().getFirstName());
        user.setLogin(userResource.toRepresentation().getUsername());
        user.setEmail(userResource.toRepresentation().getEmail());       

		return user;
		
	}
	public static UserDTO getInfosbyName(String name, HttpServletRequest request, String serverUrl, String realm) {
		
		
		KeycloakSecurityContext context = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
	    Keycloak keycloak = KeycloakBuilder
	        .builder()
	        .serverUrl(serverUrl)
	        .realm(realm)
	        .authorization(context.getTokenString())
	        .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(20).build())
	        .build();
	    UserRepresentation userResource = keycloak.realm(realm).users().search(name).get(0);
	    

	    UserDTO user = new UserDTO();
        user.setNom(userResource.getLastName());
        user.setPrenom(userResource.getFirstName());
        user.setLogin(userResource.getUsername());
        user.setEmail(userResource.getEmail());       

		return user;
		
	}
public static String getKeycloakId(String name, HttpServletRequest request, String serverUrl, String realm) {
		
		
		KeycloakSecurityContext context = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
	    Keycloak keycloak = KeycloakBuilder
	        .builder()
	        .serverUrl(serverUrl)
	        .realm(realm)
	        .authorization(context.getTokenString())
	        .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(20).build())
	        .build();
	    UserRepresentation userResource = keycloak.realm(realm).users().search(name).get(0);

		return userResource.getId();
		
	}

	
}
