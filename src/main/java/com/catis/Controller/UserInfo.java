package com.catis.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessToken.Access;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catis.objectTemporaire.ControleurDTO;
import com.catis.objectTemporaire.UserDTO;


@RestController
@CrossOrigin
public class UserInfo {
    @Autowired
    private HttpServletRequest request;
    private static String serverUrl = "http://192.168.8.113:8180/auth";
    private static String realm = "mtckeycloak";
    private String clientId = "realm-management";
    private String clientSecret = "380ca94c-1909-4b8c-9754-4846d647cc09";

    @GetMapping("/api/v1/controleurs")
    public List<ControleurDTO> userInfoController() {

        KeycloakSecurityContext context = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
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
        for (UserRepresentation u : usersOfRole) {
            cDTO = new ControleurDTO();
            cDTO.setId(u.getId());
            cDTO.setNom(u.getFirstName());
            cDTO.setPrenom(u.getLastName());
            cDTO.setEmail(u.getEmail());
            System.out.println("Attributes-----" + u.getAttributes());
//	        	cDTO.setOrganisationId(
//	        			u.getAttributes()
//	        			.get("organisationId")
//	        			.get(0)==null?
//	        									null:u.getAttributes().get("organisationId").get(0));
            controleurs.add(cDTO);
        }

        return controleurs;
    }

    @GetMapping("/api/v1/userconnected")
    public UserDTO handleUserInfoRequest() {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        UserDTO user = new UserDTO();
        if (principal instanceof KeycloakPrincipal) {

            KeycloakPrincipal<KeycloakSecurityContext> kp = (KeycloakPrincipal<KeycloakSecurityContext>) principal;
            AccessToken accessToken = kp.getKeycloakSecurityContext().getToken();
            user.setId(accessToken.getId());
            user.setNom(accessToken.getName());
            user.setPrenom(accessToken.getNickName());
            user.setLogin(accessToken.getPreferredUsername());
            user.setEmail(accessToken.getEmail());
            Access realmAccess = accessToken.getRealmAccess();
            user.setRoles(realmAccess.getRoles());
            user.setOrganisanionId(accessToken.getOtherClaims().get("organisationId").toString());
        }
        return  user;
    }
   /* public UserDTO userconnectedInfo() {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();
        UserDTO user = new UserDTO();
        user.setNom(accessToken.getName());
        user.setPrenom(accessToken.getNickName());
        user.setLogin(accessToken.getPreferredUsername());
        user.setEmail(accessToken.getEmail());
        Access realmAccess = accessToken.getRealmAccess();
        user.setRoles(realmAccess.getRoles());

        user.setOrganisanionId(accessToken.getOtherClaims());
        return user;
    }*/

    public UserDTO userInfo(String KeycloakId) {
        KeycloakSecurityContext context = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
        Keycloak keycloak = KeycloakBuilder
                .builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .authorization(context.getTokenString())
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(20).build())
                .build();
        UserResource userResource = keycloak.realm(realm).users().get(KeycloakId);


        UserDTO user = new UserDTO();
        user.setNom(userResource.toRepresentation().getLastName());
        user.setPrenom(userResource.toRepresentation().getFirstName());
        user.setLogin(userResource.toRepresentation().getUsername());
        user.setEmail(userResource.toRepresentation().getEmail());

        user.setRoles(userResource.toRepresentation().getClientRoles().keySet());
        return user;

    }
}

