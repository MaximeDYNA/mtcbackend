/*package com.catis.Controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.spi.HttpFacade.Response;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessToken.Access;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;

@RestController
@CrossOrigin
public class UserInfo {
	@Autowired
	 private HttpServletRequest request;
	@Autowired
	private RestTemplate restTemplate;
	
	@GetMapping("/api/v1/userinfo")
	public Set<UserRepresentation> userInfoController() {

	
		KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();        
	    KeycloakPrincipal principal=(KeycloakPrincipal)token.getPrincipal();
	    KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
	    AccessToken accessToken = session.getToken();
	    String username = accessToken.getPreferredUsername();
	    String emailID = accessToken.getEmail();
	    String lastname = accessToken.getFamilyName();
	    String firstname = accessToken.getGivenName();
	    String realmName = accessToken.getIssuer();
	    
	    /*
	     GET /auth/admin/realms/{realm}/users/{user-uuid}/role-mappings/clients/{client-uuid}
	     * 
	     
	    Access realmAccess = accessToken.getRealmAccess();
	    
	    Set<String> roles = realmAccess.getRoles();
	    
	  //  String url = "${keycloak.auth-server-url}/${keycloak.realm}/clients/${keycloak.resource}/roles/controleur/users";
	    
	 //   ResponseEntity <List<UserRepresentation>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<UserRepresentation>>() {});
	   // List<UserRepresentation> data = response.getBody();
	    
	    
	    
	    
	     String serverUrl = "http://192.168.88.241:8180/auth";
		    String realm = "mtckeycloak";
		    String clientId = "realm-management";
		    String clientSecret = "380ca94c-1909-4b8c-9754-4846d647cc09";
		
	   /* Keycloak keycloak = KeycloakBuilder.builder() //
	      .serverUrl(serverUrl)
	      .realm(realm)
	      .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
	      .clientId(clientId)
	      .clientSecret(clientSecret)
	      .build();
	    
	    Keycloak keycloak = Keycloak.getInstance(
	    		serverUrl,
	    		realm,
	            "tchoko",
	            "tchoko",
	            "realm-management",
	            clientSecret);

	    UserRepresentation user = new UserRepresentation();
	    user.setEnabled(true);
	    user.setUsername("tester1");
	    user.setEmail("tom+tester1 at localhost");
	    user.setAttributes(Collections.singletonMap("origin",
	Arrays.asList("demo")));

	    RealmResource realmResource = keycloak.realm(realm);
	    UsersResource userRessource = realmResource.users();
	    //System.out.println("******"+realmResource.toRepresentation().getUsers());
	    String role = "controleur";

	    Set<UserRepresentation> usersOfRole = realmResource.roles().get(role).getRoleUserMembers();
	        //KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) principal;
	        //AccessToken accessToken = keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext().getToken();

		 //Keycloak keycloak = Keycloak.getInstance("http://localhost/auth", "${keycloak.realm}", "${keycloak.resource}", "");

		  // RoleRepresentation role = keycloak.realm("${keycloak.realm}").clients().get("${keycloak.resource}").roles().get("controleur").toRepresentation();
	        return usersOfRole;
	}
	
	
	
	
   
   // Keycloak keycloak = Keycloak.getInstance("http://localhost/auth", "${keycloak.realm}", "${keycloak.resource}", "");

   // RoleRepresentation role = keycloak.realm("${keycloak.realm}").clients().get("${keycloak.resource}").roles().get("controleur").toRepresentation();
}

*/