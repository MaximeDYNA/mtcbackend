package com.catis.objectTemporaire;

import javax.servlet.http.HttpServletRequest;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;


import com.catis.model.entity.Controleur;
import org.springframework.core.env.Environment;

import java.util.Optional;


public class UserInfoIn {

    @Autowired
    private Environment env;

    public static UserDTO getInfosControleur(Controleur controleur, HttpServletRequest request, Environment env) {

        KeycloakSecurityContext context = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
        Keycloak keycloak = KeycloakBuilder
                .builder()
                .clientId(env.getProperty("admin.keycloak"))
                .serverUrl(env.getProperty("keycloak.auth-server-url"))
                .realm(env.getProperty("keycloak.realm"))

                .username(env.getProperty("admin.keycloak.login"))
                .password(env.getProperty("admin.keycloak.password"))

                .build();
        UserResource userResource = keycloak
                .realm(env.getProperty("keycloak.realm"))
                .users()
                .get(controleur
                        .getUtilisateur()
                        .getKeycloakId());

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

    public static String getUserName(String name, HttpServletRequest request, String serverUrl, String realm) {

        KeycloakSecurityContext context ;
        Keycloak keycloak;
        Optional<UserRepresentation> userResource;
        if(request != null){
            context = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
            keycloak= KeycloakBuilder
                    .builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .authorization(context.getTokenString())
                    .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(20).build())
                    .build();
            userResource = keycloak.realm(realm).users().search(name).stream().findFirst();
            if(userResource.isPresent())
                return userResource.get().getUsername();
            else
                return "";
        }
        else
            return "Yvan's Job";
    }



    public static UserDTO getUserInfo(HttpServletRequest request) {
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
            AccessToken.Access realmAccess = accessToken.getRealmAccess();
            user.setRoles(realmAccess.getRoles());
            user.setOrganisanionId(accessToken.getOtherClaims().get("organisationId").toString());
        }
        return  user;
    }


}
