package com.catis.Controller;

import java.util.ArrayList;
import java.util.List;

import com.catis.model.Organisation;
import com.catis.objectTemporaire.LignePOJO;
import com.catis.service.OrganisationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.catis.Controller.message.Message;
import com.catis.model.CarteGrise;
import com.catis.model.Ligne;
import com.catis.objectTemporaire.VehiculeByLineDTO;
import com.catis.service.CarteGriseService;
import com.catis.service.InspectionService;
import com.catis.service.LigneService;

@RestController
@CrossOrigin

public class LigneController {

    @Autowired
    private LigneService ligneService;

    @Autowired
    private CarteGriseService cgService;

    @Autowired
    private OrganisationService os;

    @Autowired
    private InspectionService inspectionService;


    private static Logger LOGGER = LoggerFactory.getLogger(LigneController.class);

    @PostMapping(value = "/api/v1/lignes")
    public ResponseEntity<Object> ajouterInspection(@RequestBody Ligne ligne) {


        LOGGER.trace("Nouvelle inpection...");

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.OK_ADD + "Ligne", ligneService.addLigne(ligne));
			/*try {}
			catch (Exception e) {
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_ADD + "Inspection", null);
			}*/

    }

    @GetMapping(value = "/api/v1/lignes")
    public ResponseEntity<Object> ligneList() {

        try {
            LOGGER.trace("liste des lignes");


            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.OK_LIST_VIEW + "Inspection", ligneService.findAllLigne());
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_LIST_VIEW + "Ligne", null);
        }

    }

    @GetMapping(value = "/api/v1/vehicules/lignes/{id}")
    public ResponseEntity<Object> getCartegriseByLigne(@PathVariable Long id) {


            LOGGER.trace("liste des vehicules par ligne");
            List<VehiculeByLineDTO> vehicules = new ArrayList<>();
            VehiculeByLineDTO v;
            for (CarteGrise cg : cgService.findByLigne(id)) {
                v = new VehiculeByLineDTO();
                v.setCarteGriseId(cg.getCarteGriseId());

                v.setIdInspection(inspectionService.findLastByRef(cg.getNumImmatriculation() == null ?
                        cg.getVehicule().getChassis() : cg.getNumImmatriculation())
                        .getIdInspection());
                v.setRef(cg.getNumImmatriculation() == null ? cg.getVehicule().getChassis()
                        : cg.getNumImmatriculation());
                v.setIdCategorie(cg.getProduit().getProduitId());
                vehicules.add(v);
                v.toString();
            }

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.OK_LIST_VIEW + "Véhicule par ligne", vehicules);
        /*try {} catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_LIST_VIEW + "Ligne", null);
        }*/

    }

    @GetMapping(value = "/api/v1/admin/lignes")
    public ResponseEntity<Object> adminList() {

        try {
            LOGGER.trace("liste des lignes");
            List<Ligne> lignes = new ArrayList<>();
            ligneService.findAllLigne().forEach(lignes::add);
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.OK_LIST_VIEW + "Inspection", lignes);
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_LIST_VIEW + "Ligne", null);
        }

    }
    @PostMapping(value = "/api/v1/admin/lignes")
    public ResponseEntity<Object> add(@RequestBody LignePOJO lignePOJO) {

        try {
            LOGGER.trace("liste des lignes");

            Ligne l = new Ligne();
            Organisation o = lignePOJO.getOrganisationId() == null ? null : os.findByOrganisationId(lignePOJO.getOrganisationId().getId());


            l.setIdLigne(lignePOJO.getIdLigne());
            l.setNom(lignePOJO.getNom());
            l.setDescription(lignePOJO.getDescription());
            l.setOrganisation(o);
            l = ligneService.addLigne(l);

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.OK_LIST_VIEW + "Inspection", l);
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_LIST_VIEW + "Ligne", null);
        }

    }

    @DeleteMapping("/api/v1/admin/lignes/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id){

        try{
            ligneService.deleteById(id);
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", null);

        }
        catch (Exception e){
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "failed", null);
        }
    }
}
