package com.catis.Controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.keycloak.KeycloakSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.catis.model.Caisse;
import com.catis.service.CaisseService;

@RestController
@CrossOrigin
public class CaisseController {
	@Autowired
	private CaisseService caisserservice;
	
	private final HttpServletRequest request;
	
	public CaisseController(HttpServletRequest request) {

		this.request = request;
	}
	
	private static Logger LOGGER = LoggerFactory.getLogger(AdresseController.class);
	
	@RequestMapping("/caisses")
	public List<Caisse> afficherLesCaisses(){
		return caisserservice.findAllCaisse();
	}
	@RequestMapping(method = RequestMethod.POST, value="/caisses")
	public void creerUneCaisse(@RequestBody Caisse caisse){
		caisserservice.addCaisse(caisse);
	}
	@GetMapping(value="/userconnected")
    public ResponseEntity<Object> getTasks()
    {
		 try {
			 
		        
			 return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
					 , getKeycloakSecurityContext().getIdToken().getGivenName());
		 }
			
			catch(Exception e) {
				LOGGER.error("Une erreur est survenu lors de l'accès à la liste des adresses");
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenu lors de "
						+ "l'ajout d'un client", null);
			}
       
    }
	 private KeycloakSecurityContext getKeycloakSecurityContext()
	    {
	        return (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
	    }
}
