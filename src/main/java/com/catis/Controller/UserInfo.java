package com.catis.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.junit.platform.commons.util.CollectionUtils;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catis.objectTemporaire.ControleurDTO;


@RestController
@CrossOrigin
public class UserInfo {
	@Autowired
	 private HttpServletRequest request;

	@GetMapping("/api/v1/controleurs")
	public List<ControleurDTO> userInfoController() {
    
		KeycloakSecurityContext context = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
	     String serverUrl = "http://192.168.8.109:8180/auth";
		    String realm = "mtckeycloak";
		    String clientId = "realm-management";
		    String clientSecret = "380ca94c-1909-4b8c-9754-4846d647cc09";
		
	    Keycloak keycloak = KeycloakBuilder.builder() //
	      .serverUrl(serverUrl)
	      .realm(realm)
	      .authorization(context.getTokenString())
	      .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(20).build())
	      .build();

	    RealmResource realmResource = keycloak.realm(realm);

	    String role = "controleur";
	    Set<UserRepresentation> usersOfRole = realmResource.roles().get(role).getRoleUserMembers();
	    List<ControleurDTO> controleurs = new ArrayList<>();  
	    ControleurDTO cDTO;
	        for(UserRepresentation u : usersOfRole) {
	        	cDTO = new ControleurDTO();
	        	cDTO.setId(u.getId());
	        	cDTO.setNom(u.getFirstName());
	        	cDTO.setPrenom(u.getLastName());
	        	cDTO.setEmail(u.getEmail());
	        	//cDTO.setOrganisationId(u.getAttributes().get("organisationId").get(0)==null?
	        	//									null:u.getAttributes().get("organisationId").get(0));
	        	controleurs.add(cDTO);
	        }

	        return controleurs;
	}
}

