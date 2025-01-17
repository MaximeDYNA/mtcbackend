package com.catis.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.catis.controller.configuration.SessionData;
import com.catis.model.entity.Inspection;
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
import javax.transaction.Transactional;

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
            UUID orgId = SessionData.getOrganisationId(request);
            List<Ligne> lignes = ligneService.findActiveByorganisation(orgId);

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.OK_LIST_VIEW + "Inspection",lignes );
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_LIST_VIEW + "Ligne", null);
        }

    }

    @Transactional
    @GetMapping(value = "/api/v1/vehicules/lignes/{id}")
    public ResponseEntity<Object> getCartegriseByLigne(@PathVariable UUID id) {


            LOGGER.trace("liste des vehicules par ligne");
            UUID orgId = SessionData.getOrganisationId(request);
            Inspection inspection;
            List<VehiculeByLineDTO> vehicules = new ArrayList<>();
            for (CarteGrise cg : cgService.findByLigne(id, orgId)) {
                inspection = inspectionService.findLastByRef(cg.getNumImmatriculation() == null ?
                                cg.getVehicule().getChassis() : cg.getNumImmatriculation());

                VehiculeByLineDTO v = new VehiculeByLineDTO();
                v.setCarteGriseId(cg.getCarteGriseId());
                v.setCertidocsId(String.valueOf(inspection.getFileId()));
                v.setIdInspection(inspection.getIdInspection());
                v.setRef(cg.getNumImmatriculation() == null ? cg.getVehicule().getChassis()
                        : cg.getNumImmatriculation());
                v.setIdCategorie(cg.getProduit().getProduitId());
                v.setInspected(inspection.isVisibleToTab());
                vehicules.add(v);
                LOGGER.info(v.toString());
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
    @Transactional
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
    public ResponseEntity<Object> deleteById(@PathVariable UUID id){

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
