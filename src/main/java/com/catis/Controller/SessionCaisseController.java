package com.catis.Controller;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;


import com.catis.model.Caissier;
import com.catis.objectTemporaire.UserInfoIn;
import com.catis.repository.CaissierRepository;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.catis.model.Hold;
import com.catis.model.SessionCaisse;
import com.catis.objectTemporaire.CloseSessionData;
import com.catis.objectTemporaire.OpenData;
import com.catis.service.HoldService;
import com.catis.service.OperationCaisseService;
import com.catis.service.OrganisationService;
import com.catis.service.SessionCaisseService;
import com.catis.service.UtilisateurService;

import javax.servlet.http.HttpServletRequest;

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
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private CaissierRepository cr;

    private static Logger LOGGER = Logger.getLogger(SessionCaisseController.class);

    @RequestMapping(value = "/api/v1/sessioncaisseexist/{userId}")
    public ResponseEntity<Object> isSessionCaisseActive(@PathVariable String userId) {
        SessionCaisse c = sessionCaisseService.findSessionCaisseByKeycloakId(userId);
        if (c != null) {
            LOGGER.info("Session de Caisse déjà ouverte");
            System.out.println(c.getSessionCaisseId());
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", c);
        } else {
            LOGGER.info("Aucune session caisse trouvée");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Aucune session active pour cet utilisateur", null);
        }

    }

    @RequestMapping(value = "/api/v1/connexion/log")
    public ResponseEntity<Object> logConnexion(@PathVariable String userId) {
        SessionCaisse c = sessionCaisseService.findSessionCaisseByKeycloakId(userId);
        if (c != null) {
            LOGGER.info("Session de Caisse déjà ouverte");
            System.out.println(c.getSessionCaisseId());
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", c);
        } else {
            LOGGER.info("Aucune session caisse trouvée");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Aucune session active pour cet utilisateur", null);
        }

    }

    @PostMapping("/api/v1/ouverturecaisse")
    public ResponseEntity<Object> ouvertureCaisse(@RequestBody OpenData openData) {
        //MDC.put("user", UserInfoIn.getUserInfo(request).getNom() +" | "+UserInfoIn.getUserInfo(request).getId());
        // System.out.println(MDC.get("user"));

        LOGGER.info("ouverture de caisse en cours...");


        Date now = new Date();
        SessionCaisse sessionCaisse = new SessionCaisse();

        sessionCaisse.setOrganisation(os.organisationIdRender(request));
        sessionCaisse.setDateHeureOuverture(now);
        sessionCaisse.setActive(true);
        sessionCaisse.setMontantOuverture(openData.getMontantOuverture());
        //sessionCaisse.setUser(us.findUtilisateurById(openData.getUserId()));
        Optional<Caissier> c = cr.findByUser_KeycloakId(openData.getUserId()).stream().findFirst();
        if(c.isPresent()){
            sessionCaisse.setCaissier(c.get());
        }
        else
            sessionCaisse.setCaissier(null);

        SessionCaisse sessionAlreadyOpen = sessionCaisseService
                .findSessionCaisseByKeycloakId(openData.getUserId());
        if( sessionAlreadyOpen == null)
            sessionCaisse = sessionCaisseService.enregistrerSessionCaisse(sessionCaisse);
        else
            sessionCaisse = sessionAlreadyOpen;

        Hold hold = new Hold();

        hold.setNumber(hs.maxNumber(sessionCaisse) + 1);
        hold.setSessionCaisse(sessionCaisse);
        hold.setTime(now);
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

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/fermerSessionCaisse")
    public ResponseEntity<Object> fermerSessionCaisse(@RequestBody CloseSessionData closeSessionData) throws IOException {

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
