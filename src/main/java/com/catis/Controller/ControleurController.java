package com.catis.Controller;

import javax.servlet.http.HttpServletRequest;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.catis.model.Controleur;
import com.catis.objectTemporaire.UserDTO;
import com.catis.service.ControleurService;

@RestController
@CrossOrigin
public class ControleurController {

    @Autowired
    public ControleurService controleurService;
    @Autowired
    private HttpServletRequest request;
    private static String serverUrl = "http://192.168.8.113:8180/auth";
    private static String realm = "mtckeycloak";

    private static Logger LOGGER = LoggerFactory.getLogger(ContactController.class);

    @GetMapping(value = "/api/v1/controleur/{keycloakId}")
    public ResponseEntity<Object> getInfosControleur(@PathVariable String keycloakId) {

        Controleur controleur = controleurService.findControleurBykeycloakId(keycloakId);
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", controleur);

    }

    @GetMapping(value = "/api/v1/controleurinfo/{id}")
    public ResponseEntity<Object> getInfosControleur(@PathVariable Long id) {

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

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", user);

    }

}
