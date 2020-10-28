package com.catis.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.catis.Controller.objectTemporaire.HoldData;
import com.catis.model.Hold;
import com.catis.service.HoldService;
import com.catis.service.PosaleService;
import com.catis.service.SessionCaisseService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@RestController
@CrossOrigin
public class HoldController {

	@Autowired
	private HoldService holdService;
	@Autowired
	private SessionCaisseService scs;
	@Autowired
	private PosaleService ps;
	
	private static Logger LOGGER = LoggerFactory.getLogger(HoldController.class);
	
	@RequestMapping(value="/api/v1/newhold/{sessionCaisseId}")
	public ResponseEntity<Object> ajouterOnglet(@PathVariable Long sessionCaisseId) throws ParseException{
		
		try {
			LOGGER.info("création onglet demandé...");
			
				Hold hold = new Hold();
				hold.setSessionCaisse(scs.findSessionCaisseById(sessionCaisseId));
				hold.setNumber(holdService.maxNumber()+1);
				SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
				Date date = format.parse(format.format(new Date())); 
				hold.setTime(date);
				hold = holdService.addHold(hold);
				ps.activatePosale(hold.getNumber(), hold.getSessionCaisse().getSessionCaisseId());
				return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", hold );
			} catch (java.lang.NullPointerException nul) {
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Session Id invalide, "
						+ "veuillez vous reconnecter", null);
			}
			catch (Exception e) {
			return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenu"
					+ "bien vouloir le signaler à l'équipe CATIS", null);
			}
		
	}
	@RequestMapping(method=RequestMethod.POST, value="/api/v1/deletehold")
	public ResponseEntity<Object> supprimerOnglet(@RequestBody HoldData holdData) throws ParseException{
			
			 try {
					LOGGER.info("suppression de l'onglet "+ holdData.getNumber()+ " demandé...");
					holdService.deleteHoldByNumber(holdData.getNumber(), holdData.getSessionCaisseId());
					ps.deletePosale(holdData.getNumber(), holdData.getSessionCaisseId());
					ps.activatePosale(holdService.maxNumber(), holdData.getSessionCaisseId());
					return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "onglet supprimé", null );
			 	} 
			 catch (Exception e) { 
				 return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false,
						 	"Une erreur est survenue" + " bien vouloir contacter l'équipe CATIS", null);
			  }
			
		
	}
	@RequestMapping(method=RequestMethod.POST, value="/api/v1/selecthold")
	public ResponseEntity<Object> selectionnerOnglet(@RequestBody HoldData holdData) throws ParseException{
			
			 try {
					LOGGER.info("sélection de l'onglet "+ holdData.getNumber()+ " demandé...");
					ps.activatePosale(holdData.getNumber(), holdData.getSessionCaisseId());
					
					return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "sélection de l'onglet "+ holdData.getNumber()+ " demandé...", holdService.findByNumberSessionCaisse(holdData.getNumber(), holdData.getSessionCaisseId()) );
			 	} 
			 catch (Exception e) { 
				 return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false,
						 	"Une erreur est survenue" + " bien vouloir contacter l'équipe CATIS", null);
			  }
			
		
	}
	@RequestMapping(method=RequestMethod.GET, value="/api/v1/holdlist/{sessionCaisseId}")
	public ResponseEntity<Object> sessionsHold(@PathVariable Long sessionCaisseId) {
			
			 try {
					LOGGER.info("sélection des onglets d'une session ID...");
	
					return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "onglet de la session id "+ sessionCaisseId, holdService.findHoldBySessionCaisse(sessionCaisseId));
			 	} 
			 catch (Exception e) { 
				 return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false,
						 	"Une erreur est survenue" + " bien vouloir contacter l'équipe CATIS", null);
			  }
			
		
	}
	
	
	
}
