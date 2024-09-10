package com.catis.model.configuration;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.core.env.Environment;
import org.springframework.data.domain.AuditorAware;
// import com.catis.objectTemporaire.UserInfoIn;


public class AuditorAwareImpl implements AuditorAware<String> {

    @Autowired
    private HttpServletRequest request;

    @Override
    public Optional<String> getCurrentAuditor() {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal<KeycloakSecurityContext> principal = (KeycloakPrincipal<KeycloakSecurityContext>) token.getPrincipal();
        AccessToken accessToken = principal.getKeycloakSecurityContext().getToken();

        // Get username directly from token
        String username = accessToken.getPreferredUsername();

        // Optionally, you can also get other information if needed
        // String userId = accessToken.getSubject();
        // String email = accessToken.getEmail();
        // Set up user details in UserDTO if required

        return Optional.ofNullable(username);
    }
}



// public class AuditorAwareImpl implements AuditorAware<String> {
//     @Autowired
//     private HttpServletRequest request;
//     @Autowired
//     private Environment environment;

//     @Override
//     public Optional<String> getCurrentAuditor() {

//         KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();

//         KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
//         KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
//         AccessToken accessToken = session.getToken();

//         String keycloakId = UserInfoIn.getUserName(accessToken.getPreferredUsername(), request,
//                 environment.getProperty("keycloak.auth-server-url"), environment.getProperty("keycloak.realm"));

//         return Optional.ofNullable(keycloakId);
//     }

// }
