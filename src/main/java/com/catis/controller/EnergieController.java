package com.catis.controller;

import com.catis.objectTemporaire.EnergiePOJO;
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

import com.catis.controller.message.Message;
import com.catis.model.entity.Energie;
import com.catis.model.entity.Utilisateur;
import com.catis.service.EnergieService;

import java.util.*;

import javax.transaction.Transactional;

@RestController
@CrossOrigin
public class EnergieController {

    @Autowired
    private EnergieService energieService;
    @Autowired
    private OrganisationService os;

    @Autowired
    private PagedResourcesAssembler<Energie> pagedResourcesAssembler;


    private static Logger LOGGER = LoggerFactory.getLogger(AdresseController.class);

    @GetMapping("/api/v1/cg/energies")
    public ResponseEntity<Object> energieList() {
        try {

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
                    , energieService.energieList());
        } catch (Exception e) {
            LOGGER.error("Une erreur est survenu lors de l'accès à la liste des energies");
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenu lors de "
                    + "l'ajout d'un client", null);
        }
    }

    /*Admin*/

    @GetMapping(value="/api/v1/admin/energies",params = {"page", "size"})
    public ResponseEntity<Object> adminEnergieList(@RequestParam("page") int page,
    @RequestParam("size") int size) {
        try {

            List<Energie> energies = energieService.energieListPage(PageRequest.of(page, size, Sort.by("createdDate").descending()));

            Page<Energie> pages = new PageImpl<>(energies, PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdDate")),300);

            PagedModel<EntityModel<Energie>> result = pagedResourcesAssembler
                    .toModel(pages);

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
                    , result);
        } catch (Exception e) {
            LOGGER.error("Une erreur est survenu lors de l'accès à la liste des energies");
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenu lors de "
                    + "l'ajout d'un client", null);
        }
    }
    // old method no pagination enabled
    // @GetMapping("/api/v1/admin/energies")
    // public ResponseEntity<Object> adminEnergieList() {
    //     try {

    //         List<Energie> energies = energieService.energieList();

    //         return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
    //                 , energies);
    //     } catch (Exception e) {
    //         LOGGER.error("Une erreur est survenu lors de l'accès à la liste des energies");
    //         return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenu lors de "
    //                 + "l'ajout d'un client", null);
    //     }
    // }
    @PostMapping("/api/v1/admin/energies")
    public ResponseEntity<Object> energie(@RequestBody EnergiePOJO energiePOJO) {
        try {

            Energie energie = new Energie();

            energie.setEnergieId(energiePOJO.getEnergieId());

            energie.setLibelle(energiePOJO.getLibelle());

            energie.setOrganisation(energiePOJO.getOrganisationId() == null ? null : os.findByOrganisationId(energiePOJO.getOrganisationId()));
            energie = energieService.addEnergie(energie);

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès" , energie );
        } catch (Exception e) {
            LOGGER.error("Une erreur est survenu lors de l'accès à la liste des adresses");
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_ADD + "Energie", null);
        }
    }
    @DeleteMapping("/api/v1/admin/energies/{id}")
    public ResponseEntity<Object> energie(@PathVariable UUID id) {
        try {
            energieService.deleteById(id);
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
                    , null);
        } catch (Exception e) {
            LOGGER.error("Une erreur est survenu lors de l'accès à la liste des adresses");
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_ADD + "Energie", null);
        }
    }

    @Transactional
    @GetMapping("/api/v1/admin/energies/select")
    public ResponseEntity<Object> getCaissesOfMtcforSelect(){

        List<Energie> cats = energieService.energieList();
        List<Map<String, String>> catsSelect = new ArrayList<>();

        Map<String, String> cat;

        for(Energie c: cats){
            cat = new HashMap<>();
            cat.put("id", String.valueOf(c.getEnergieId()));
            cat.put("name", c.getLibelle() +" | "
                    + (c.getOrganisation() == null? "Tous" : c.getOrganisation().getNom()));
            catsSelect.add(cat);
        }

        return ApiResponseHandler.generateResponse(HttpStatus.OK,
                true, "Select catégorie produit OK", catsSelect);
    }
}
