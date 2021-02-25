package com.catis.model.configuration;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.AuditorAware;

import com.catis.model.Utilisateur;
import com.catis.objectTemporaire.UserDTO;
import com.catis.objectTemporaire.UserInfoIn;
import com.catis.service.UtilisateurService;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private Environment environment;

    @Override
    public Optional<String> getCurrentAuditor() {

        //KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        //KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        //KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
        //AccessToken accessToken = session.getToken();

        //String keycloakId = UserInfoIn.getKeycloakId(accessToken.getPreferredUsername(), request,
          //      environment.getProperty("keycloak.auth-server-url"), environment.getProperty("keycloak.realm"));

        //return Optional.ofNullable(keycloakId);
         return Optional.ofNullable("TEST");
    }

}
