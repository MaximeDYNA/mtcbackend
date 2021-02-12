package com.catis.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.catis.objectTemporaire.UserDTO;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.Organisation;
import com.catis.repository.OrganisationRepository;

import javax.servlet.http.HttpServletRequest;

@Service
public class OrganisationService {

    @Autowired
    private OrganisationRepository organisationRepository;

    public List<Organisation> findAllOrganisation() {
        List<Organisation> organisations = new ArrayList<>();
        organisationRepository.findAll().forEach(organisations::add);
        return organisations;
    }

    public Organisation organisationIdRender(HttpServletRequest request){
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        Organisation organisation = null;
        if (principal instanceof KeycloakPrincipal) {

            KeycloakPrincipal<KeycloakSecurityContext> kp = (KeycloakPrincipal<KeycloakSecurityContext>) principal;
            AccessToken accessToken = kp.getKeycloakSecurityContext().getToken();
            System.out.println("******************"+ accessToken.getPreferredUsername() +"  "+accessToken
                    .getOtherClaims().toString() );
            Long l = Long.valueOf(accessToken
                    .getOtherClaims()
                    .get("organisationId").toString());



            organisation = findByOrganisationId(l) ;

        }
        return  organisation;
    }

    public void addOrgansiation(Organisation organisation) {
        organisationRepository.save(organisation);
    }

    public Organisation findByOrganisationId(Long id) {
        return organisationRepository.findById(id).get();
    }

}
