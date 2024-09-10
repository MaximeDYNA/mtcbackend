package com.catis.objectTemporaire;

import javax.servlet.http.HttpServletRequest;


import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.AccessToken;




import com.catis.model.entity.Controleur;
import org.springframework.core.env.Environment;


import java.util.Map;
import java.util.Set;
import java.util.UUID;




public class UserInfoIn {

    public static UserDTO getInfosControleur(Controleur controleur, Environment env) {
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

    public static UserDTO getInfosFromToken(HttpServletRequest request) {
    
        // Get the KeycloakSecurityContext from the request
        KeycloakSecurityContext context = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
        
        if (context == null) {
            throw new IllegalStateException("Keycloak security context is missing");
        }
    
        // Retrieve the AccessToken from the KeycloakSecurityContext
        AccessToken accessToken = context.getToken();
        
        // Extract user information from the AccessToken
        UserDTO user = new UserDTO();
        user.setId(accessToken.getSubject());
        user.setNom(accessToken.getName());
        user.setPrenom(accessToken.getGivenName());
        user.setLogin(accessToken.getPreferredUsername());
        user.setEmail(accessToken.getEmail());
        
        // You might want to add more fields if they're available in the token
    
        return user;
    }
    

    // public static UserDTO getInfosbyName(String name, HttpServletRequest request, String serverUrl, String realm) {


    //     KeycloakSecurityContext context = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
    //     Keycloak keycloak = KeycloakBuilder
    //             .builder()
    //             .serverUrl(serverUrl)
    //             .realm(realm)
    //             .authorization(context.getTokenString())
    //             .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(20).build())
    //             .build();
    //     UserRepresentation userResource = keycloak.realm(realm).users().search(name).get(0);



    //     UserDTO user = new UserDTO();
    //     user.setNom(userResource.getLastName());
    //     user.setPrenom(userResource.getFirstName());
    //     user.setLogin(userResource.getUsername());
    //     user.setEmail(userResource.getEmail());


    //     return user;

    // }


    public static String getKeycloakId(HttpServletRequest request) {
        // Get the KeycloakSecurityContext from the request
        KeycloakSecurityContext context = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
        
        if (context == null) {
            throw new IllegalStateException("KeycloakSecurityContext is not available in the request");
        }
    
        // Retrieve the AccessToken from the KeycloakSecurityContext
        AccessToken accessToken = context.getToken();
        
        // Return the Keycloak ID (subject) from the AccessToken
        return accessToken.getSubject();
    }

    // public static String getKeycloakId(String name, HttpServletRequest request, String serverUrl, String realm) {


    //     KeycloakSecurityContext context = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
    //     Keycloak keycloak = KeycloakBuilder
    //             .builder()
    //             .serverUrl(serverUrl)
    //             .realm(realm)
    //             .authorization(context.getTokenString())
    //             .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(20).build())
    //             .build();
    //     UserRepresentation userResource = keycloak.realm(realm).users().search(name).get(0);

    //     return userResource.getId();
    // }

    
    // public static String getUserName(String name, HttpServletRequest request, String serverUrl, String realm)  {

    //     KeycloakSecurityContext context ;
    //     Keycloak keycloak;
    //     Optional<UserRepresentation> userResource;
    //     if(request != null){
    //         context = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
    //         keycloak= KeycloakBuilder
    //                 .builder()
    //                 .serverUrl(serverUrl)
    //                 .realm(realm)
    //                 .authorization(context.getTokenString())
    //                 .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(20).build())
    //                 .build();
    //         userResource = keycloak.realm(realm).users().search(name).stream().findFirst();
    //         if(userResource.isPresent())
    //             return userResource.get().getUsername();
    //         else
    //             return "";
    //     }
    //     else
    //         return "Yvan's Job";
    // }



    // public static UserDTO getUserInfo(HttpServletRequest request) {
    //     KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
    //     KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
    //     UserDTO user = new UserDTO();
    //     if (principal instanceof KeycloakPrincipal) {

    //         KeycloakPrincipal<KeycloakSecurityContext> kp = (KeycloakPrincipal<KeycloakSecurityContext>) principal;
    //         AccessToken accessToken = kp.getKeycloakSecurityContext().getToken();

    //         System.out.println("Token Subject: " + accessToken.getSubject());
    //         System.out.println("Token Roles: " + accessToken.getRealmAccess().getRoles());
    //         System.out.println("Token Scopes: " + accessToken.getScope());
    //         System.out.println("Token Expiration: " + accessToken.getExpiration());
        
    //         // Log other important claims if needed
    //         System.out.println("Other Claims: " + accessToken.getOtherClaims().toString());
    //         Map<String, AccessToken.Access> resourceAccess = accessToken.getResourceAccess();
    //         System.out.println("Resource Access: " + resourceAccess);



    //         user.setId(accessToken.getSubject());
    //         user.setNom(accessToken.getName());
    //         user.setPrenom(accessToken.getNickName());
    //         user.setLogin(accessToken.getPreferredUsername());
    //         user.setEmail(accessToken.getEmail());
    //         System.out.println("User Roles");
    //         System.out.println(accessToken.getRealmAccess().getRoles().toString());
    //         AccessToken.Access realmAccess = accessToken.getRealmAccess();
    //         user.setRoles(realmAccess.getRoles());
    //         user.setOrganisanionId(UUID.fromString(accessToken.getOtherClaims().get("organisationId").toString()));
    //     }
    //     return  user;
    // }





public static UserDTO getUserInfo(HttpServletRequest request) {
    KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
    KeycloakPrincipal<KeycloakSecurityContext> principal = (KeycloakPrincipal<KeycloakSecurityContext>) token.getPrincipal();
    UserDTO user = new UserDTO();
    
    if (principal instanceof KeycloakPrincipal) {
        KeycloakPrincipal<KeycloakSecurityContext> kp = (KeycloakPrincipal<KeycloakSecurityContext>) principal;
        AccessToken accessToken = kp.getKeycloakSecurityContext().getToken();

        // Basic user information
        user.setId(accessToken.getSubject());
        user.setNom(accessToken.getName());
        user.setPrenom(accessToken.getNickName());
        user.setLogin(accessToken.getPreferredUsername());
        user.setEmail(accessToken.getEmail());

        // Print user roles (Realm Access)
        AccessToken.Access realmAccess = accessToken.getRealmAccess();
        if (realmAccess != null) {
            Set<String> roles = realmAccess.getRoles();
            System.out.println("User Realm Roles: " + roles);
            user.setRoles(roles);
        }

        // Print user policies if applicable
        Map<String, Object> otherClaims = accessToken.getOtherClaims();
        if (otherClaims != null) {
            System.out.println("Other Claims:");
            for (Map.Entry<String, Object> claim : otherClaims.entrySet()) {
                System.out.println(claim.getKey() + ": " + claim.getValue());
            }
        }

        // Example of accessing a custom claim like "organisationId"
        if (otherClaims.containsKey("organisationId")) {
            user.setOrganisanionId(UUID.fromString(otherClaims.get("organisationId").toString()));
        }
    }
    
    return user;
}


}
