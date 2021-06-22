package com.catis.Controller;

import com.catis.Controller.message.Message;
import com.catis.objectTemporaire.*;
import com.catis.service.OrganisationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.catis.model.Organisation;
import com.catis.service.OrganisationService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OrganisationController {

    @Autowired
    private OrganisationService os;
    @Autowired
    HttpServletRequest request;
    @Autowired
    private PagedResourcesAssembler<OrganisationDTO> pagedResourcesAssembler;

    private static Logger LOGGER = LoggerFactory.getLogger(OrganisationController.class);


    @GetMapping("/api/v1/organisation")
    public ResponseEntity<Object> connectedUserOrganisation() {
                UserDTO u =UserInfoIn.getUserInfo(request);
                Organisation organisation = os.findByOrganisationId(Long.valueOf(u.getOrganisanionId()));

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", organisation);
        /*try {} catch (Exception e) {

            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_LIST_VIEW + "Organisation", null);
        }*/
    }
    /**Admin**/
   /* @GetMapping("/api/v1/organisations")
    public List<OrganisationListDTO> getOrganisation() {

        List<Organisation> organisations = os.findAllOrganisation();
        List<OrganisationListDTO> organisationsForVue = new ArrayList<>();
        organisations.forEach(organisation -> {
            organisationsForVue.add(new OrganisationListDTO(organisation.getOrganisationId(), organisation.getNom()));
        });
        return organisationsForVue;
        /*try {} catch (Exception e) {

            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_LIST_VIEW + "Organisation", null);
        }
    }*/
    
    @GetMapping("/api/v1/organisations/{id}")
    public ResponseEntity<Object> getOrganisation(@PathVariable Long id){

        Organisation organisation = os.findOrganisationById(id);
        return ApiResponseHandler.generateResponse(HttpStatus.OK,
                true, Message.ListOK + "Organisation", organisation);
            /*try {}
        catch (Exception e){
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    false, Message.AddKO + "assurances", null);
        }*/
    }
    @DeleteMapping("/api/v1/organisations/{id}")
    public ResponseEntity<Object> deleteOrganisation(@PathVariable Long id){

        os.deleteById(id);
        return ApiResponseHandler.generateResponse(HttpStatus.OK,
                true, Message.DeleteOK + "Organisation", null);
            /*try {}
        catch (Exception e){
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    false, Message.AddKO + "assurances", null);
        }*/
    }
    @GetMapping(value="/api/v1/organisations", params = { "page", "size" })
    public ResponseEntity<Object> findPaginated(@RequestParam("page") int page,
                                                @RequestParam("size") int size) throws Exception {

        Page<OrganisationDTO> resultPage = os.findAll(PageRequest.of(page, size));

        if (page > resultPage.getTotalPages()) {
            throw new Exception();
        }

        PagedModel<EntityModel<OrganisationDTO>> collModel = pagedResourcesAssembler
                .toModel(resultPage);

        return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "OK", collModel);
    }
    @GetMapping(value = "/api/v1/organisations/children/{id}" ,params = { "page", "size" })
    public ResponseEntity<Object> findChildPaginated(@PathVariable Long id, @RequestParam("page") int page,
                                                     @RequestParam("size") int size) throws Exception {

        Page<OrganisationDTO> resultPage = os.findAllChildren(id,PageRequest.of(page, size));

        if (page > resultPage.getTotalPages()) {
            throw new Exception();
        }

        PagedModel<EntityModel<OrganisationDTO>> collModel = pagedResourcesAssembler
                .toModel(resultPage);

        return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "OK", collModel);
    }
    @GetMapping("/api/v1/organisations/kanban")
    public ResponseEntity<Object> listforKanbanView(){
        try {

            List<OrganisationKanbanViewDTO> kabanViews = new ArrayList<>();
            List<Organisation> parentList = os.findAllForSelect();
            for(Organisation o : parentList ){
                kabanViews.add(new OrganisationKanbanViewDTO(o.getNom(), os.findChildren(o.getNom()), os.findChildren(o.getNom()).size()));
            }

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Affichage Kaban view assurance", kabanViews);

        } catch (Exception e) {

            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Erreur lors de l'affichage"
                    + " de la liste des visite en cours", null);
        }

    }
    @GetMapping("/api/v1/organisations/graphview")
    public ResponseEntity<Object> listforGraphView() {
        try {
            List<Organisation> parents = os.findAllForSelect();
            int[] histogramme = new int[parents.size()];
            List<AssuranceCircleViewDTO> graphViews = new ArrayList<>();
            int i = 0;
            int entitySize = 0;
            for(Organisation o : parents){
                entitySize = os.findChildren(o.getNom()).size();
                histogramme[i] = entitySize;
                graphViews.add(new AssuranceCircleViewDTO(o.getNom(), entitySize));
            }

            return ApiResponseHandler.reponseForGraph(HttpStatus.OK, true, "Affichage graphe",graphViews ,histogramme);
        } catch (Exception e) {

            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Erreur lors de l'affichage"
                    + " de la liste des visite en cours", null);
        }

    }
    @GetMapping("/api/v1/organisation/select")
    public ResponseEntity<Object> findAllForSelect(){
        try {
            List<Organisation> results = os.findAllForSelect();
            List<Map<String, String>> mapList = new ArrayList<>();
            Map<String, String> map = new HashMap<>();
            for(Organisation organisation : results){
                map.put("id",String.valueOf(organisation.getOrganisationId()));
                map.put("name", organisation.getNom());
                mapList.add(map);
                map = new HashMap<>();
            }
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "OK", mapList);
        }
        catch(Exception e){
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "OK", null);
        }
    }
    @GetMapping("/api/v1/organisation/child/select")
    public ResponseEntity<Object> findAllChildForSelect(){
        try {
            List<Organisation> results = os.findAllChildForSelect();
            List<Map<String, String>> mapList = new ArrayList<>();
            Map<String, String> map = new HashMap<>();
            for(Organisation organisation : results){
                map.put("id",String.valueOf(organisation.getOrganisationId()));
                map.put("name", organisation.getNom()+" | "+organisation.getParentOrganisation() == null ? "No parent" : organisation.getParentOrganisation().getNom());
                mapList.add(map);
                map = new HashMap<>();
            }
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "OK", mapList);
        }
        catch(Exception e){
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "OK", null);
        }
    }
    @GetMapping("/api/v1/organisations/parents")
    public ResponseEntity<Object> findParent(){
        try {
            List<Organisation> results = os.findAllForSelect();
            List<String> names = new ArrayList<>();
            results.forEach(organisation -> {
                names.add(organisation.getNom());
            });
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "OK", names);
        }
        catch(Exception e){
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "OK", null);
        }
    }
    @PostMapping ("/api/v1/organisations")
    public ResponseEntity<Object> enregistrer(@RequestBody OrganisationPOJO organisationPOJO){
        try {
            Organisation organisation = new Organisation();
            Organisation parentOrganisation = organisationPOJO.getParentOrganisation() == null?
                    null : os.findOrganisationById(organisationPOJO.getParentOrganisation());
            organisation.setAdress(organisationPOJO.getAdresse());
            organisation.setOrganisationId(organisationPOJO.getId() == null ? null : organisationPOJO.getId() );
            organisation.setParent(organisationPOJO.isParent());
            organisation.setDevise(organisationPOJO.getDevise());
            organisation.setRegion(organisationPOJO.getRegion());
            organisation.setLang(organisationPOJO.getLang());
            organisation.setNumeroDeContribuable(organisationPOJO.getNumContribuable());
            organisation.setTel1(organisationPOJO.getTel1());
            organisation.setTel2(organisationPOJO.getTel2());
            organisation.setNom(organisationPOJO.getNom());
            organisation.setParentOrganisation(parentOrganisation);
            organisation = os.enregistrer(organisation);

            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "OK", organisation);
        }
        catch(Exception e){
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "OK", null);
        }
    }
}
