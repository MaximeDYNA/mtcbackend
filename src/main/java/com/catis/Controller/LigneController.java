package com.catis.Controller;

import java.util.ArrayList;
import java.util.List;

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
    private InspectionService inspectionService;


    private static Logger LOGGER = LoggerFactory.getLogger(LigneController.class);

    @PostMapping(value = "/api/v1/lignes")
    public ResponseEntity<Object> ajouterInspection(@RequestBody Ligne ligne) {


        LOGGER.info("Nouvelle inpection...");

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.OK_ADD + "Ligne", ligneService.addLigne(ligne));
			/*try {}
			catch (Exception e) {
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_ADD + "Inspection", null);
			}*/

    }

    @GetMapping(value = "/api/v1/lignes")
    public ResponseEntity<Object> ligneList() {

        try {
            LOGGER.info("liste des lignes");


            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.OK_LIST_VIEW + "Inspection", ligneService.findAllLigne());
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_LIST_VIEW + "Ligne", null);
        }

    }

    @GetMapping(value = "/api/v1/vehicules/lignes/{id}")
    public ResponseEntity<Object> getCartegriseByLigne(@PathVariable Long id) {

        try {
            LOGGER.info("liste des vehicules par ligne");
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
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_LIST_VIEW + "Ligne", null);
        }

    }
}
