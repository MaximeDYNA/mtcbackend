package com.catis.controller;

import com.catis.model.entity.Vente;
import com.catis.objectTemporaire.MarquePOJO;
import com.catis.service.OrganisationService;
import org.apache.commons.lang3.builder.ToStringBuilder;
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

import com.catis.model.entity.MarqueVehicule;
import com.catis.service.MarqueService;

import java.util.*;

import javax.transaction.Transactional;

@RestController
@CrossOrigin
public class MarqueController {

    @Autowired
    private MarqueService marqueService;
    @Autowired
    private OrganisationService os;
    private static Logger LOGGER = LoggerFactory.getLogger(MarqueController.class);
    @Autowired
    private PagedResourcesAssembler<MarqueVehicule> pagedResourcesAssemblerVente;

    @GetMapping("/api/v1/all/search/marques")
    public ResponseEntity<Object> listMarque() {
        LOGGER.trace("List des marques...");
        try {
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", marqueService.marqueList());
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenue", null);

        }
    }


    /*Admin*/

    @Transactional
    @GetMapping(value="/api/v1/admin/marques", params ={"page", "size"})
    public ResponseEntity<Object> listAdminMarque(@RequestParam("page") int page,
                                                  @RequestParam("size") int size) {
        LOGGER.trace("List des marques...");
        try {
            List<MarqueVehicule> marqueVehiculeList = marqueService.marqueList(PageRequest.of(page, size));

            Page<MarqueVehicule> pages = new PageImpl<>(marqueVehiculeList, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate")),300);
            PagedModel<EntityModel<MarqueVehicule>> result = pagedResourcesAssemblerVente
                    .toModel(pages);

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success",result );
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenue", null);

        }
    }
    @Transactional
    @PostMapping("/api/v1/admin/marques")
    public ResponseEntity<Object> addMarque(@RequestBody MarquePOJO marque) {
        LOGGER.trace("List des marques...");
        System.out.println(ToStringBuilder.reflectionToString(marque));
        MarqueVehicule marqueVehicule = new MarqueVehicule();
        marqueVehicule.setMarqueVehiculeId(marque.getMarqueVehiculeId());
        marqueVehicule.setDescription(marque.getDescription());
        marqueVehicule.setLibelle(marque.getLibelle());
        marqueVehicule.setOrganisation(marque.getOrganisationId() == null ? null : os.findByOrganisationId(marque.getOrganisationId()));
        marqueVehicule = marqueService.addMarque(marqueVehicule);
        try {
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", marqueVehicule );
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenue", null);

        }
    }

    @DeleteMapping("/api/v1/admin/marques/{id}")
    public ResponseEntity<Object> addMarque(@PathVariable UUID id) {
        LOGGER.trace("List des marques...");
            marqueService.deleteById(id);
        try {
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", null );
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenue", null);

        }
    }
    @Transactional
    @GetMapping("/api/v1/admin/marques/select")
    public ResponseEntity<Object> getCaissesOfMtcforSelect(){

        List<MarqueVehicule> cats = marqueService.marqueList();
        List<Map<String, String>> catsSelect = new ArrayList<>();

        Map<String, String> cat;

        for(MarqueVehicule c: cats){
            cat = new HashMap<>();
            cat.put("id", String.valueOf(c.getMarqueVehiculeId()));
            cat.put("name", c.getLibelle() +" | "
                    + (c.getOrganisation() == null? "Tous" : c.getOrganisation().getNom()));
            catsSelect.add(cat);
        }

        return ApiResponseHandler.generateResponse(HttpStatus.OK,
                true, "Select catégorie produit OK", catsSelect);
    }
}
