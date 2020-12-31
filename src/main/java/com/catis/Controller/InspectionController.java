package com.catis.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.catis.Controller.message.Message;
import com.catis.model.CarteGrise;
import com.catis.model.Inspection;
import com.catis.model.Visite;
import com.catis.objectTemporaire.InpectionReceived;
import com.catis.service.ControleurService;
import com.catis.service.GieglanFileService;
import com.catis.service.InspectionService;
import com.catis.service.LigneService;
import com.catis.service.ProduitService;
import com.catis.service.VisiteService;

@RestController
@CrossOrigin
public class InspectionController {

	@Autowired
	private InspectionService inspectionService;
	@Autowired
	private LigneService ligneService;
	@Autowired
	private ControleurService controleurService;
	@Autowired
	private ProduitService produitService;
	@Autowired
	private VisiteService visiteService;
	@Autowired
	private GieglanFileService gieglanFileService;
	
	private static Logger LOGGER = LoggerFactory.getLogger(InspectionController.class);
	
	@PostMapping(value="/api/v1/inspections")
	public ResponseEntity<Object> ajouterInspection(@RequestBody InpectionReceived inspectionReceived) {
		
		try {
				LOGGER.info("Nouvelle inpection...");
				Inspection inspection = new Inspection(inspectionReceived);
				inspection.setControleur(controleurService.findControleurById(inspectionReceived.getControleurId()));
				inspection.setLigne(ligneService.findLigneById(inspectionReceived.getLigneId()));
				inspection.setProduit(produitService.findById(inspectionReceived.getProduitId()));
				Visite visite = visiteService.findById(inspectionReceived.getVisiteId());
				inspection.setVisite(visite);
				visiteService.commencerInspection(inspectionReceived.getVisiteId());
				this.gieglanFileService.createFileGieglanOfCgrise(visite.getCarteGrise(), inspection);
				/*String[] result = "this is a test".split("\\s");
			     for (int x=0; x<result.length; x++)
			         System.out.println(result[x]);*/
				return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.OK_ADD + "Inspection", inspectionService.addInspection(inspection));
			}
			catch (Exception e) {
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_ADD + "Inspection", null);
			}
		
	}
	@GetMapping(value="/api/v1/inspections")
	public ResponseEntity<Object> inspectionList() {
		
		try {
				LOGGER.info("création onglet demandé...");
				
				
				return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.OK_LIST_VIEW + "Inspection", inspectionService.findAllInspection());
			}
			catch (Exception e) {
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_LIST_VIEW + "Inspection", null);
			}
		
	}
}
