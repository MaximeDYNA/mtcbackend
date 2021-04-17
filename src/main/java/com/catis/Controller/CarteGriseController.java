package com.catis.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.catis.Controller.message.Message;
import com.catis.model.CarteGrise;
import com.catis.model.Vehicule;
import com.catis.model.Visite;
import com.catis.objectTemporaire.CarteGriseReceived;
import com.catis.service.CarteGriseService;
import com.catis.service.EnergieService;
import com.catis.service.MarqueService;
import com.catis.service.ProduitService;
import com.catis.service.ProprietaireVehiculeService;
import com.catis.service.VehiculeService;
import com.catis.service.VisiteService;

@RestController
@CrossOrigin
public class CarteGriseController {
    @Autowired
    private CarteGriseService cgs;
    @Autowired
    private ProprietaireVehiculeService pvs;
    @Autowired
    private MarqueService ms;
    @Autowired
    private ProduitService ps;
    @Autowired
    private VehiculeService vs;
    @Autowired
    private VisiteService visiteService;
    @Autowired
    private EnergieService energieService;

    private static Logger LOGGER = LoggerFactory.getLogger(CarteGriseController.class);

    @GetMapping("/api/v1/cartegrise/search/{imCha}")
    public ResponseEntity<Object> search(@PathVariable String imCha) {
        LOGGER.trace("Recherche carte grise...");
        try {
            //cgs.findByImmatriculationOuCarteGrise(imCha)
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", cgs.findBychassis(imCha));
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenue", null);

        }
    }

    @GetMapping("/api/v1/search/cartegrise/assurance/{imma}")
    public ResponseEntity<Object> searchForAssurance(@PathVariable String imma) {
        LOGGER.trace("Recherche carte grise...");
        try {
            //cgs.findByImmatriculationOuCarteGrise(imCha)
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success",
                    cgs.findCartegriseForAssurance(imma));
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false,
                    "Une erreur est survenue", null);

        }
    }
    @GetMapping("/api/v1/search/cartegrise/assurance/imma/{imma}")
    public ResponseEntity<Object> searchForAssuranceByImma(@PathVariable String imma) {
        LOGGER.trace("Recherche carte grise...");
        try {
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success",
                    cgs.findByImmatriculation(imma));
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false,
                    "Une erreur est survenue", null);

        }
    }
    @GetMapping("/api/v1/search/cartegrise/assurance/visite/{imma}")
    public ResponseEntity<Object> isCGhasValidVisite(@PathVariable String imma) {
        LOGGER.trace("visite...");
        try {
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success",
                    cgs.isCarteGriseHasValidVisite(imma));
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false,
                    "Une erreur est survenue", null);

        }
    }


    @GetMapping("/api/v1/search/last/{imCha}")
    public ResponseEntity<Object> searchLast(@PathVariable String imCha) {
        LOGGER.trace("Recherche carte grise...");

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", cgs.findLastCgBychassis(imCha));
		/*try {	} 
		catch(Exception e){ 
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenue", null );*/


    }

    @GetMapping("/api/v1/cartegrises")
    public ResponseEntity<Object> findAll() {
        LOGGER.trace("Recherche carte grise...");
        try {
            //cgs.findByImmatriculationOuCarteGrise(imCha)
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", cgs.findAll());
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenue", null);

        }
    }

    @PostMapping("/api/v1/cartegrise")
    public ResponseEntity<Object> misajour(@RequestBody CarteGriseReceived carteGriseR) throws IOException {
        LOGGER.trace("mise à jour demandé...");

        System.out.println("......" + carteGriseR.getVisiteId());
        CarteGrise carteGrise = new CarteGrise(carteGriseR);

        //initialise le vehicule avec les éléments reçus par la vue

        Vehicule vehicule = new Vehicule(carteGriseR);

        //set modifie l'energie du vehicule

        vehicule.setEnergie(energieService.findEnergie(carteGriseR.getEnergieId()));

        // retrouve l'objet visite en bd

        Visite visite = visiteService.findById(carteGriseR.getVisiteId());

        //recupère l'id de la cg

        carteGrise.setCarteGriseId(visite.getCarteGrise().getCarteGriseId());
        carteGrise.setProprietaireVehicule(pvs.findById(carteGriseR.getProprietaireId()));
        vehicule.setMarqueVehicule(ms.findById(carteGriseR.getMarqueVehiculeId()));
        carteGrise.setProduit(ps.findById(carteGriseR.getProduitId()));
        carteGrise.setVehicule(vs.addVehicule(vehicule));

        visite.setStatut(1);
        visiteService.modifierVisite(visite);

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", cgs.updateCarteGrise(carteGrise));
			/*try {} 
		catch(Exception e){ 
			
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_ADD +"Carte grise", null );
				  
		}*/

    }

    @GetMapping("/api/v1/cartegrise/listview")
    public ResponseEntity<Object> carteGriseListView() {
        LOGGER.trace("Recherche carte grise...");
        try {
            LOGGER.trace("Liste des produits");
            Map<String, Object> listView;
            List<Map<String, Object>> mapList = new ArrayList<>();
            for (CarteGrise c : cgs.findAll()) {
                listView = new HashMap<>();
                listView.put("id", c.getCarteGriseId());
                listView.put("immatriculation", c.getNumImmatriculation());
                listView.put("proprietaire", c.getProprietaireVehicule().getPartenaire().getNom());
                listView.put("montant", c.getMontantPaye());

                //listView.put("marque", c.getMarqueVehicule());

                listView.put("createdDate", c.getCreatedDate());
                listView.put("modifiedDate", c.getModifiedDate());
                mapList.add(listView);
            }

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", mapList);

        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_LIST_VIEW + "Client", null);
        }

    }
}
