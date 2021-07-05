package com.catis.Controller;

import com.catis.objectTemporaire.MarquePOJO;
import com.catis.service.OrganisationService;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.catis.model.entity.MarqueVehicule;
import com.catis.service.MarqueService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class MarqueController {

    @Autowired
    private MarqueService marqueService;
    @Autowired
    private OrganisationService os;
    private static Logger LOGGER = LoggerFactory.getLogger(MarqueController.class);

    @GetMapping("/api/v1/marques")
    public ResponseEntity<Object> listMarque() {
        LOGGER.trace("List des marques...");
        try {
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", marqueService.marqueList());
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenue", null);

        }
    }


    /*Admin*/

    @GetMapping("/api/v1/admin/marques")
    public ResponseEntity<Object> listAdminMarque() {
        LOGGER.trace("List des marques...");
        try {
            List<MarqueVehicule> marqueVehiculeList = marqueService.marqueList();

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success",marqueVehiculeList );
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenue", null);

        }
    }

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
    public ResponseEntity<Object> addMarque(@PathVariable Long id) {
        LOGGER.trace("List des marques...");
            marqueService.deleteById(id);
        try {
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", null );
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenue", null);

        }
    }
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
