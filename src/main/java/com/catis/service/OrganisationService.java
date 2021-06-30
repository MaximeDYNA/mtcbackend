package com.catis.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.catis.model.Visite;
import com.catis.objectTemporaire.*;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    public Organisation findOrganisationById(Long id){
        Optional<Organisation> o = organisationRepository.findById(id);
        if(o.isPresent()){
            return o.get();
        }
        return null;
    }
    public Page<OrganisationDTO> findAll(Pageable pageable){
        List<OrganisationDTO> organisationDTOS = new ArrayList<>();
        OrganisationDTO oDTO;
        ModelForSelectDTO parentOrganisation;
        for(Organisation o : organisationRepository.findByActiveStatusTrueAndParentTrue(pageable)){
            oDTO = new OrganisationDTO();
            parentOrganisation = new ModelForSelectDTO();
            oDTO.setId(o.getOrganisationId());
            oDTO.setAdresse(o.getAdress());
            oDTO.setNom(o.getNom());
            oDTO.setTel1(o.getTel1());
            oDTO.setTel2(o.getTel2());
            oDTO.setParent(o.isParent());
            parentOrganisation.setId(o.getParentOrganisation()==null?null:o.getParentOrganisation().getOrganisationId());
            parentOrganisation.setName(o.getParentOrganisation()==null?null:o.getParentOrganisation().getNom() );
            oDTO.setParentOrganisation(parentOrganisation);
            organisationDTOS.add(oDTO);
        }
        Page<OrganisationDTO> organisationDTOPage = new PageImpl<>(organisationDTOS,pageable,Long.valueOf(organisationDTOS.size()));
        return organisationDTOPage;
    }
    public Page<OrganisationDTO> findAllChildren(Long idParent, Pageable pageable){
        List<OrganisationDTO> organisationDTOS = new ArrayList<>();
        OrganisationDTO oDTO;
        ModelForSelectDTO parentOrganisation;
        for(Organisation o : organisationRepository.findByActiveStatusTrueAndParentOrganisation_ActiveStatusTrueAndParentOrganisation_OrganisationId(idParent,pageable)){
            oDTO = new OrganisationDTO();
            parentOrganisation = new ModelForSelectDTO();
            oDTO.setId(o.getOrganisationId());
            oDTO.setAdresse(o.getAdress());
            oDTO.setNom(o.getNom());
            oDTO.setTel1(o.getTel1());
            oDTO.setTel2(o.getTel2());
            oDTO.setParent(o.isParent());
            parentOrganisation.setId(o.getParentOrganisation()==null?null:o.getParentOrganisation().getOrganisationId());
            parentOrganisation.setName(o.getParentOrganisation()==null?null:o.getParentOrganisation().getNom() );
            oDTO.setParentOrganisation(parentOrganisation);
            organisationDTOS.add(oDTO);
        }
        Page<OrganisationDTO> organisationDTOPage = new PageImpl<>(organisationDTOS,pageable,Long.valueOf(organisationDTOS.size()));
        return organisationDTOPage;
    }
    public List<Organisation> findAll(){
        List<Organisation> orgs = new ArrayList<>();
        organisationRepository.findAll().forEach(orgs::add);
        return orgs;
    }
    public List<ChildKanbanDTO> findChildren(String nom){
        List<Organisation> orgs = organisationRepository.findByActiveStatusTrueAndParentOrganisation_ActiveStatusTrueAndParentOrganisation_Nom(nom);
        List<ChildKanbanDTO> childKanbanDTOS = new ArrayList<>();
        ChildKanbanDTO childKanbanDTO;
        for(Organisation o : orgs){
            childKanbanDTO = new ChildKanbanDTO();
            childKanbanDTO.setId(o.getOrganisationId());
            childKanbanDTO.setName(o.getNom());
            childKanbanDTOS.add(childKanbanDTO);
        }
        return childKanbanDTOS;
    }
    public List<ChildKanbanDTO> findChildren(Long id){
        List<Organisation> orgs = organisationRepository.findByActiveStatusTrueAndParentOrganisation_ActiveStatusTrueAndParentOrganisation_OrganisationId(id);
        List<ChildKanbanDTO> childKanbanDTOS = new ArrayList<>();
        ChildKanbanDTO childKanbanDTO;
        for(Organisation o : orgs){
            childKanbanDTO = new ChildKanbanDTO();
            childKanbanDTO.setId(o.getOrganisationId());
            childKanbanDTO.setName(o.getNom());
            childKanbanDTOS.add(childKanbanDTO);
        }
        return childKanbanDTOS;
    }
    public List<Organisation> findAllForSelect(){
        List<Organisation> orgs = new ArrayList<>();
        organisationRepository.findByActiveStatusTrueAndParentTrue().forEach(orgs::add);
        return orgs;
    }
    public List<Organisation> findAllChildForSelect(){
        List<Organisation> orgs = new ArrayList<>();
        organisationRepository.findByActiveStatusTrueAndParentFalse().forEach(orgs::add);
        return orgs;
    }
    public void deleteById(Long id){
        organisationRepository.deleteById(id);
    }

    public Organisation enregistrer(Organisation organisation){
        return organisationRepository.save(organisation);
    }
    public void addOrgansiation(Organisation organisation) {
        organisationRepository.save(organisation);
    }

    public Organisation findByOrganisationId(Long id) {
        return organisationRepository.findById(id).get();
    }




}
