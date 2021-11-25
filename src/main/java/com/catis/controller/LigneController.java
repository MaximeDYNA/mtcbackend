package com.catis.controller;

import java.util.ArrayList;
import java.util.List;

import com.catis.controller.configuration.SessionData;
import com.catis.model.entity.Organisation;
import com.catis.objectTemporaire.LignePOJO;
import com.catis.service.OrganisationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.catis.controller.message.Message;
import com.catis.model.entity.CarteGrise;
import com.catis.model.entity.Ligne;
import com.catis.objectTemporaire.VehiculeByLineDTO;
import com.catis.service.CarteGriseService;
import com.catis.service.InspectionService;
import com.catis.service.LigneService;

import javax.servlet.http.HttpServletRequest;

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

    @Autowired
    HttpServletRequest request;


    private static Logger LOGGER = LoggerFactory.getLogger(LigneController.class);

    @PostMapping(value = "/api/v1/controleur/lignes")
    public ResponseEntity<Object> ajouterInspection(@RequestBody Ligne ligne) {


        LOGGER.trace("Nouvelle inpection...");

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.OK_ADD + "Ligne", ligneService.addLigne(ligne));
			/*try {}
			catch (Exception e) {
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_ADD + "Inspection", null);
			}*/

    }

    @GetMapping(value = "/api/v1/controleur/lignes")
    public ResponseEntity<Object> ligneList() {
        LOGGER.trace("liste des lignes de l'organisation");


        try {
            Long orgId = SessionData.getOrganisationId(request);

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.OK_LIST_VIEW + "Inspection", ligneService.findActiveByorganisation(orgId));
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_LIST_VIEW + "Ligne", null);
        }

    }

    @GetMapping(value = "/api/v1/vehicules/lignes/{id}")
    public ResponseEntity<Object> getCartegriseByLigne(@PathVariable Long id) {


            LOGGER.trace("liste des vehicules par ligne");
            Long orgId = SessionData.getOrganisationId(request);

            List<VehiculeByLineDTO> vehicules = new ArrayList<>();
            for (CarteGrise cg : cgService.findByLigne(id, orgId)) {
                VehiculeByLineDTO v = new VehiculeByLineDTO();
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
