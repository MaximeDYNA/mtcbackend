package com.catis.objectTemporaire;

import javax.servlet.http.HttpServletRequest;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.springframework.beans.factory.annotation.Autowired;

import com.catis.model.Controleur;
import com.catis.service.ControleurService;

public class UserInfoIn {
	@Autowired
	private static HttpServletRequest request;
	private static String serverUrl = "http://192.168.8.113:8180/auth";
	private static String realm = "mtckeycloak";
	@Autowired
	public static ControleurService controleurService;
	
	public static UserDTO getInfosControleur(Long id) {
		
		Controleur controleur = controleurService.findControleurById(id);
		KeycloakSecurityContext context = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
	    Keycloak keycloak = KeycloakBuilder
	        .builder()
	        .serverUrl(serverUrl)
	        .realm(realm)
	        .authorization(context.getTokenString())
	        .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(20).build())
	        .build();
	    UserResource userResource = keycloak.realm(realm).users().get(controleur.getKeycloakId());
	    

	    UserDTO user = new UserDTO();
        user.setNom(userResource.toRepresentation().getLastName());
        user.setPrenom(userResource.toRepresentation().getFirstName());
        user.setLogin(userResource.toRepresentation().getUsername());
        user.setEmail(userResource.toRepresentation().getEmail());       

		return user;
		
	}
}
