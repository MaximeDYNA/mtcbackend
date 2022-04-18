package com.catis.controller;

import java.util.*;

//import org.keycloak.KeycloakSecurityContext;
import com.catis.controller.message.Message;
import com.catis.model.entity.Organisation;
import com.catis.objectTemporaire.CaissePOJO;
import com.catis.objectTemporaire.Listview;
import com.catis.service.OrganisationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.catis.model.entity.Caisse;
import com.catis.service.CaisseService;

@RestController
@CrossOrigin
public class CaisseController {
    @Autowired
    private CaisseService caisseService;
    @Autowired
    private OrganisationService organisationService;
    @Autowired
    private PagedResourcesAssembler<Caisse> pagedResourcesAssembler;


    private static Logger LOGGER = LoggerFactory.getLogger(CaisseController.class);

    @GetMapping(value ="/api/v1/admin/caisses", params = {"page", "size"})
    public ResponseEntity<Object> afficherLesCaisses(@RequestParam("page") int page,
                                                     @RequestParam("size") int size) {

        List<Caisse> caisses = caisseService.findAllCaisse(PageRequest.of(page, size, Sort.by("createdDate").descending()));

        Page<Caisse> pages = new PageImpl<>(caisses, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate")),300);
        PagedModel<EntityModel<Caisse>> result = pagedResourcesAssembler
                .toModel(pages);

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success",
                result);

    }


    @GetMapping("/api/v1/admin/caisses/select")
    public ResponseEntity<Object> getCaissesOfMtcforSelect(){

        List<Caisse> caisses = caisseService.findAllCaisse();
        List<Map<String, String>> caissesSelect = new ArrayList<>();

        Map<String, String> caisse;


        for(Caisse c: caisses){
            caisse = new HashMap<>();
            caisse.put("id", String.valueOf(c.getCaisseId()));
            caisse.put("name", c.getLibelle() +" | "+ c.getOrganisation().getNom());
            caissesSelect.add(caisse);
        }

        return ApiResponseHandler.generateResponse(HttpStatus.OK,
                true, Message.ListOK + " Caisses", caissesSelect);
    }


    @PostMapping("/api/v1/admin/caisses")
    public ResponseEntity<Object> save(@RequestBody CaissePOJO caissePOJO) {

        Caisse caisse = new Caisse();
        Organisation organisation = caissePOJO.getOrganisation() == null?
                null : organisationService.findOrganisationById(caissePOJO.getOrganisation());
        caisse.setCaisseId(caissePOJO.getCaisse_id());
        caisse.setLibelle(caissePOJO.getLibelle() == null ? null : caissePOJO.getLibelle() );
        caisse.setDescription(caissePOJO.getDescription() == null ? null : caissePOJO.getDescription());
        caisse.setOrganisation(organisation);
        caisse = caisseService.addCaisse(caisse);
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success",
                caisse);

    }

    @DeleteMapping("/api/v1/admin/caisses/{id}")
    public ResponseEntity<Object> delete (@PathVariable UUID id) {

        caisseService.deleteCaisseById(id);
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success",
                null);

    }


}
