package com.catis.service;

import java.util.*;

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
import org.springframework.transaction.annotation.Transactional;

import com.catis.dtoprojections.OrganisationDataDTO;
import com.catis.model.entity.Organisation;
import com.catis.repository.OrganisationRepository;

import javax.servlet.http.HttpServletRequest;

@Service
@Transactional
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
            UUID l = UUID.fromString(accessToken
                    .getOtherClaims()
                    .get("organisationId").toString());



            organisation = findByOrganisationId(l) ;

        }
        return  organisation;
    }

    public Organisation findOrganisationById(UUID id){
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
    public Page<OrganisationDTO> findAllChildren(UUID idParent, Pageable pageable){
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
    public List<ChildKanbanDTO> findChildren(UUID id){
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

    // flemming implimnted
    public List<OrganisationDataDTO> findOrganisations(String nom, Pageable pageable) {
        return organisationRepository.findByActiveStatusTrueAndParentFalse(nom, pageable);
    }

    public void deleteById(UUID id){
        organisationRepository.deleteById(id);
    }

   
    public Organisation enregistrer(Organisation organisation){
        return organisationRepository.save(organisation);
    }

    public void addOrgansiation(Organisation organisation) {
        organisationRepository.save(organisation);
    }

    public Organisation findByOrganisationId(UUID id) {
        return organisationRepository.findById(id).get();
    }




}
