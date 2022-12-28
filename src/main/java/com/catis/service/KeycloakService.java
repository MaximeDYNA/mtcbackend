package com.catis.service;

import com.catis.objectTemporaire.UserDTO;
import com.catis.objectTemporaire.UserKeycloak;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class KeycloakService {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private Environment env;

    //private String clientId = env.getProperty("admin");
    //private String clientSecret = env.getProperty("admin.clientId");

    public List<UserKeycloak> getUserList() {
        String serverUrl = env.getProperty("keycloak.auth-server-url");

        String realm = env.getProperty("keycloak.realm");

        KeycloakSecurityContext context = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .authorization(context.getTokenString())
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(20).build())
                .build();

        UsersResource usersResource = keycloak.realm(realm).users();
        List<UserRepresentation> users = usersResource.list();
        List<UserKeycloak> userDTOs = new ArrayList<>();

        ClientRepresentation clientRepresentation = keycloak.realm(realm).clients()
                .findByClientId(env.getProperty("keycloak.resource")).get(0);

        for (UserRepresentation u : users) {

            UserResource userResource = usersResource.get(u.getId());

            String userRole = userResource.roles()
                    .clientLevel(clientRepresentation.getId())
                    .listAll().size() == 0 ? null : userResource.roles()
                    .clientLevel(clientRepresentation.getId())
                    .listAll()
                    .get(0).getName();

            UserKeycloak userDTO = new UserKeycloak();
            userDTO.setId(u.getId());
            userDTO.setFirstName(u.getFirstName());
            userDTO.setLastName(u.getUsername());
            userDTO.setEmail(u.getEmail());
            userDTO.setRole(userRole);
            userDTO.setOrganisationId(
                    u.getAttributes() == null ?
                    null : u.getAttributes().get("organisationId").get(0));
            if(u.getAttributes() == null ?
                    false : (u.getAttributes().get("ditrosct") == null ? false :  Boolean.valueOf(u.getAttributes().get("ditrosct").get(0))))
            userDTOs.add(userDTO);
        }
        keycloak.close();
        System.out.println("***user dto size ** "+ userDTOs.size());

        return userDTOs;
    }

}
