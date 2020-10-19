package com.catis.security;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catis.Controller.ApiResponseHandler;
import com.catis.model.Utilisateur;
import com.catis.service.UtilisateurService;

@RestController
public class Connexion {
	@Autowired
	private UtilisateurService userservice;
	private static Logger LOGGER = LoggerFactory.getLogger(Connexion.class);
	
	@RequestMapping("/api/v1/login")
	public ResponseEntity<Object> authentification(@RequestBody Utilisateur u) {
		LOGGER.info("Une authentification est en cours...");
		Utilisateur user = userservice.findUtilisateurByLogin(u.getLogin());
		if(user.getMotDePasse().equals(u.getMotDePasse())) {
			user.setIdOrganisation("idOrgTest");
			return  ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Success", user);
		}
		else
		return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Success", null);
	}
	
}
