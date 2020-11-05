package com.catis;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.catis.Controller.ApiResponseHandler;
import com.catis.service.VisiteService;

@RestController
public class VisiteController {

	@Autowired
	private VisiteService vs;
	private static Logger log  = LoggerFactory.getLogger(VisiteController.class);
	@RequestMapping(method=RequestMethod.GET, value="/api/v1/visitesencours")
	public ResponseEntity<Object> listDesVisitesEncours(){
		try {
			log.info("Liste des visites");
			return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "liste des visite en cours", vs.enCoursVisitList());
		} catch (Exception e) {
			log.error("Erreur lors de l'affichage de la liste des visite en cours");
			return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Erreur lors de l'affichage"
					+ " de la liste des visite en cours", null);
		}
		
	}
}
