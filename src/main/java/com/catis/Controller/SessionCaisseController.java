package com.catis.Controller;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.catis.model.Hold;
import com.catis.model.SessionCaisse;
import com.catis.objectTemporaire.CloseSessionData;
import com.catis.objectTemporaire.OpenData;
import com.catis.service.HoldService;
import com.catis.service.OperationCaisseService;
import com.catis.service.OrganisationService;
import com.catis.service.SessionCaisseService;
import com.catis.service.UtilisateurService;

@RestController
@CrossOrigin
public class SessionCaisseController {

	@Autowired
	private SessionCaisseService sessionCaisseService;
	@Autowired
	private OperationCaisseService operationCaisse;
	@Autowired
	private HoldService hs;
	@Autowired
	private OrganisationService os;
	@Autowired
	private UtilisateurService us;
	private static Logger LOGGER = LoggerFactory.getLogger(SessionCaisseController.class);
	
	@RequestMapping( value="/api/v1/sessioncaisseexist/{userId}")
	public ResponseEntity<Object> isSessionCaisseActive(@PathVariable Long userId) {
		if(sessionCaisseService.findActiveSessionCaissierById(userId)!=null) {
			LOGGER.info("Session de Caisse déjà ouverte");
			return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", sessionCaisseService.findActiveSessionCaissierById(userId));
		}
		else {
			LOGGER.info("Aucune session caisse trouvée");
			return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Aucune session active pour cet utilisateur", null);
		}
			
	}
	@RequestMapping("/api/v1/ouverturecaisse")
	public ResponseEntity<Object> ouvertureCaisse(@RequestBody OpenData openData) {
		LOGGER.info("ouverture de caisse en cours...");
				
			Date now = new Date();
			SessionCaisse sessionCaisse = new SessionCaisse();
			sessionCaisse.setOrganisationId(os.findByOrganisationId(1L));
			sessionCaisse.setDateHeureOuverture(now);
			sessionCaisse.setActive(true);
			sessionCaisse.setMontantOuverture(openData.getMontantOuverture());
			//sessionCaisse.setUser(us.findUtilisateurById(openData.getUserId()));
			sessionCaisse = sessionCaisseService.enregistrerSessionCaisse(sessionCaisse);
			Hold hold = new Hold();
			
			hold.setNumber(hs.maxNumber()+1);
			hold.setSessionCaisse(sessionCaisse);
			hold.setTime(sessionCaisse.getDateHeureOuverture());
			hs.addHold(hold);
			return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", sessionCaisse);
		/*try {}
		catch(Exception e){
			LOGGER.error("Une erreur est survenu");
			return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Erreur", null);
		}*/
		
	}
	@RequestMapping("/api/v1/sessioncaisses")
	public SessionCaisse sessionCaisse(@RequestBody SessionCaisse sessionCaisse) {
		return sessionCaisseService.findSessionCaisseById(0);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/api/v1/fermerSessionCaisse")
	public ResponseEntity<Object> fermerSessionCaisse(@RequestBody CloseSessionData closeSessionData) throws IOException{
		
			LOGGER.info("Fermeture session caisse...");
			hs.deleteHoldBySessionCaisse(closeSessionData.getSessionCaisseId());
			//System.out.println("******6262626262"+		ImageSizeHandler.compress("bonjour"));
			//System.out.println("*....................**decompressed***"+	 ImageSizeHandler.decompress(ImageSizeHandler.compress("bonjour")));
			sessionCaisseService.fermerSessionCaisse(closeSessionData.getSessionCaisseId(), closeSessionData.getMontantFermeture());
			return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", operationCaisse.findBySession(closeSessionData.getSessionCaisseId()));
		/*try {} catch (Exception e) {
			LOGGER.error("Erreur lors de la suppression de l'onglet");
			return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenu "
					+ "bien vouloir le signaler à l'équipe CATIS", null);
		}*/
		
	}
	
}
