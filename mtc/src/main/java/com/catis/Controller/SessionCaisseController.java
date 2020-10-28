package com.catis.Controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.catis.model.Posales;
import com.catis.model.SessionCaisse;
import com.catis.security.Connexion;
import com.catis.service.HoldService;
import com.catis.service.SessionCaisseService;

@RestController
public class SessionCaisseController {

	@Autowired
	private SessionCaisseService sessionCaisseService;
	@Autowired
	private HoldService hs;
	private static Logger LOGGER = LoggerFactory.getLogger(Connexion.class);
	
	@RequestMapping("/api/v1/ouverturecaisse")
	public ResponseEntity<Object> ouvertureCaisse(@RequestBody SessionCaisse sessionCaisse) {
		LOGGER.info("ouverture de caisse en cours...");
		Date now = new Date();
		sessionCaisse.setIdOrganisation("aux");
		sessionCaisse.setDateHeureOuverture(now);
		sessionCaisseService.enregistrerSessionCaisse(sessionCaisse);
		sessionCaisse = sessionCaisseService.findSessionCaisseById(sessionCaisse.getSessionCaisseId());
		System.out.println(sessionCaisse.getCaisse().getDescription());
		return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "success", sessionCaisse);
	}
	@RequestMapping("/api/v1/sessioncaisses")
	public SessionCaisse sessionCaisse(@RequestBody SessionCaisse sessionCaisse) {
		return sessionCaisseService.findSessionCaisseById(0);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/api/v1/fermerSessionCaisse/{sessionCaisseId}")
	public ResponseEntity<Object> fermerSessionCaisse(@PathVariable Long sessionCaisseId){
		try {
			LOGGER.info("Fermeture session caisse...");
			hs.deleteHoldBySessionCaisse(sessionCaisseId);
			return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", sessionCaisseService.fermerSessionCaisse(sessionCaisseId));
		} catch (Exception e) {
			LOGGER.error("Erreur lors de la suppression de l'onglet");
			return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenu "
					+ "bien vouloir le signaler à l'équipe CATIS", null);
		}
		
	}
	
}
