package com.catis.Controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.catis.model.SessionCaisse;
import com.catis.security.Connexion;
import com.catis.service.SessionCaisseService;

@RestController
public class SessionCaisseController {

	@Autowired
	private SessionCaisseService sessionCaisseService;
	private static Logger LOGGER = LoggerFactory.getLogger(Connexion.class);
	
	@RequestMapping("/api/v1/ouverturecaisse")
	public ResponseEntity<Object> ouvertureCaisse(@RequestBody SessionCaisse sessionCaisse) {
		LOGGER.info("ouverture de caisse en cours...");
		Date now = new Date();
		sessionCaisse.setIdOrganisation("aux");
		sessionCaisse.setDateHeureOuverture(now);
		sessionCaisseService.enregistrerSessionCaisse(sessionCaisse);
		sessionCaisse = sessionCaisseService.findSessionCaisseById(sessionCaisse.getIdSessionCaisse());
		System.out.println(sessionCaisse.getCaisse().getDescription());
		return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "success", sessionCaisse);
	}
	@RequestMapping("/api/v1/sessioncaisses")
	public SessionCaisse sessionCaisse(@RequestBody SessionCaisse sessionCaisse) {
		
		return sessionCaisseService.findSessionCaisseById("auxsce1");
	}
}
