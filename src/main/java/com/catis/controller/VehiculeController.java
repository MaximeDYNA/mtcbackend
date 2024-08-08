package com.catis.controller;

import java.util.*;

import javax.transaction.Transactional;

import com.catis.objectTemporaire.VehiculePOJO;
import com.catis.service.EnergieService;
import com.catis.service.MarqueService;
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

import com.catis.dtoprojections.VehiculeDTO;
import com.catis.model.entity.Energie;
import com.catis.model.entity.Vehicule;
import com.catis.service.VehiculeService;

@RestController
@CrossOrigin
public class VehiculeController {

    @Autowired
    private VehiculeService vehiculeService;
    @Autowired
    private EnergieService energieService;
    @Autowired
    private MarqueService marqueService;
    @Autowired
    private OrganisationService os;

    @Autowired
    private PagedResourcesAssembler<Vehicule> pagedResourcesAssembler;


    private static Logger LOGGER = LoggerFactory.getLogger(VehiculeController.class);

    @GetMapping("/api/v1/cg/vehicules")
    public ResponseEntity<Object> vehiculeList() {
        try {
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
                    , vehiculeService.vehiculeList());
        } catch (Exception e) {
            LOGGER.error("Une erreur est survenu lors de l'accès à la liste des adresses");
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenu", null);
        }
    }

    @PostMapping("/api/v1/vehicules")
    public ResponseEntity<Object> addVehicule(Vehicule vehicule) {
        try {
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
                    , vehiculeService.addVehicule(vehicule));
        } catch (Exception e) {
            LOGGER.error("Une erreur est survenu lors de l'accès à la liste des adresses");
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenu", null);
        }
    }

    @GetMapping("/api/v1/all/search/vehicules/{chassis}")
    public ResponseEntity<Object> searchVehicule(@PathVariable String chassis) {
        try {
            LOGGER.trace("recherche de véhicule...");

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès", vehiculeService.findByChassis(chassis));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            LOGGER.error("Une erreur est survenu lors de l'accès à la liste des adresses");
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenu", null);
        }
    }

    /**Admin*/
    // @GetMapping("/api/v1/admin/vehicules")
    // public ResponseEntity<Object> vehiculeAdminList() {
    //     try {
    //         List<Vehicule> vehicules = vehiculeService.vehiculeList();
    //         return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
    //                 , vehicules);
    //     } catch (Exception e) {
    //         LOGGER.error("Une erreur est survenu lors de l'accès à la liste des adresses");
    //         return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenu", null);
    //     }
    // }
    // flemming implimented
    @GetMapping(value="/api/v1/admin/vehicules",params = {"search","page", "size"})
    public ResponseEntity<Object> vehiculeAdminList(@RequestParam("search") String search, @RequestParam("page") int page,
    @RequestParam("size") int size) {
        if(search==null){
            search = "";
        }
        try {
            
            Page<Vehicule> vehicules = vehiculeService.vehiculeListSearchPage(search, PageRequest.of(page, size, Sort.by("createdDate").descending()));
            
            PagedModel<EntityModel<Vehicule>> result = pagedResourcesAssembler
            .toModel(vehicules);
            
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
            , result);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Une erreur est survenu lors de l'accès à la liste des vehicule");
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenu", null);
        }
    }

    @Transactional
    @PostMapping("/api/v1/admin/vehicules")
    public ResponseEntity<Object> addAdminVehicule(@RequestBody VehiculePOJO vehiculePOJO) {
        try {

            Vehicule vehicule = new Vehicule();
            vehicule.setCarrosserie(vehiculePOJO.getCarrosserie());
            vehicule.setChassis(vehiculePOJO.getChassis());
            vehicule.setChargeUtile(vehiculePOJO.getChargeUtile());
            vehicule.setChargeUtile(vehiculePOJO.getChargeUtile());
            vehicule.setVehiculeId(vehiculePOJO.getVehiculeId());
            vehicule.setTypeVehicule(vehiculePOJO.getTypeVehicule());
            vehicule.setPlaceAssise(vehiculePOJO.getPlaceAssise());
            vehicule.setPremiereMiseEnCirculation(vehiculePOJO.getPremiereMiseEnCirculation());
            vehicule.setPuissAdmin(vehiculePOJO.getPuissAdmin());
            vehicule.setPoidsTotalCha(vehiculePOJO.getPoidsTotalCha());
            vehicule.setPoidsVide(vehiculePOJO.getPoidsVide());
            vehicule.setChargeUtile(vehiculePOJO.getChargeUtile());
            vehicule.setCylindre(vehiculePOJO.getCylindre());
            vehicule.setEnergie(vehiculePOJO.getEnergie() == null ? null: energieService.findEnergie(vehiculePOJO.getEnergie()));
            vehicule.setMarqueVehicule( vehiculePOJO.getMarqueVehicule() ==null ? null : marqueService.findById(vehiculePOJO.getMarqueVehicule()));
            vehicule.setOrganisation(vehiculePOJO.getOrganisationId() == null ? null : os.findByOrganisationId(vehiculePOJO.getOrganisationId().getId()));

            vehicule = vehiculeService.addVehicule(vehicule);

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
                    , vehicule );
        } catch (Exception e) {
            LOGGER.error("Une erreur est survenu lors de l'accès à la liste des adresses");
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenu", null);
        }
    }
    @DeleteMapping("/api/v1/admin/vehicules/{id}")
    public ResponseEntity<Object> vehiculeAdminList(@PathVariable UUID id) {
        try {
            vehiculeService.deleteById(id);
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
                    , null);
        } catch (Exception e) {

            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenu", null);
        }
    }

    @Transactional
    @GetMapping("/api/v1/admin/vehicules/select")
    public ResponseEntity<Object> getCaissesOfMtcforSelect(){

        List<Vehicule> cats = vehiculeService.vehiculeList();
        List<Map<String, String>> catsSelect = new ArrayList<>();

        Map<String, String> cat;

        for(Vehicule c: cats){
            cat = new HashMap<>();
            cat.put("id", String.valueOf(c.getVehiculeId()));
            cat.put("name", c.getChassis() +" | "
                    + (c.getOrganisation() == null? "Toutes" : c.getOrganisation().getNom()));
            catsSelect.add(cat);
        }

        return ApiResponseHandler.generateResponse(HttpStatus.OK,
                true, "Select catégorie produit OK", catsSelect);
    }
    // flemming implimented
    @GetMapping(value="/api/v1/admin/vehicules/select/{keyword}")
    public ResponseEntity<Object> getCaissesOfMtcforSelect(@PathVariable String keyword){

        List<VehiculeDTO> cats = vehiculeService.getVehicules(keyword, PageRequest.of(0, 15, Sort.by("createdDate").descending()));
        List<Map<String, String>> catsSelect = new ArrayList<>();

        Map<String, String> cat;

        for(VehiculeDTO c: cats){
            cat = new HashMap<>();
            
            cat.put("id", String.valueOf(c.getVehiculeId()));
            cat.put("name", c.getChassis() +" | " + c.getOrganisationNom());
            catsSelect.add(cat);
        }

        return ApiResponseHandler.generateResponse(HttpStatus.OK,
                true, "Select catégorie produit OK", catsSelect);
    }
}
