package com.catis.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import com.catis.model.entity.Caissier;
import com.catis.model.entity.Produit;
import com.catis.repository.CaissierRepository;
//import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.catis.model.entity.Hold;
import com.catis.model.entity.SessionCaisse;
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
    @Autowired
    private PagedResourcesAssembler<SessionCaisse> pagedResourcesAssembler;

   // private static Logger LOGGER = Logger.getLogger(SessionCaisseController.class);

    @RequestMapping(value = "/api/v1/caisse/sessioncaisseexist/{userId}")
    public ResponseEntity<Object> isSessionCaisseActive(@PathVariable String userId) {
        SessionCaisse c = sessionCaisseService.findSessionCaisseByKeycloakId(userId);
        if (c != null) {
         //   LOGGER.info("Session de Caisse déjà ouverte");
            System.out.println(c.getSessionCaisseId());
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", c);
        } else {
        //    LOGGER.info("Aucune session caisse trouvée");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Aucune session active pour cet utilisateur", null);
        }

    }

    @RequestMapping(value = "/api/v1/connexion/log")
    public ResponseEntity<Object> logConnexion(@PathVariable String userId) {
        SessionCaisse c = sessionCaisseService.findSessionCaisseByKeycloakId(userId);
        if (c != null) {
         //   LOGGER.info("Session de Caisse déjà ouverte");
            System.out.println(c.getSessionCaisseId());
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", c);
        } else {
         //   LOGGER.info("Aucune session caisse trouvée");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Aucune session active pour cet utilisateur", null);
        }

    }

    @PostMapping("/api/v1/caisse/ouverturecaisse")
    public ResponseEntity<Object> ouvertureCaisse(@RequestBody OpenData openData) {
        //MDC.put("user", UserInfoIn.getUserInfo(request).getNom() +" | "+UserInfoIn.getUserInfo(request).getId());
        // System.out.println(MDC.get("user"));

      //  LOGGER.info("ouverture de caisse en cours...");


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
        return sessionCaisseService.findSessionCaisseById(UUID.randomUUID());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/caisse/fermerSessionCaisse")
    public ResponseEntity<Object> fermerSessionCaisse(@RequestBody CloseSessionData closeSessionData) throws IOException {

       // LOGGER.info("Fermeture session caisse...");
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

    //Admin session de caisse

    @GetMapping(value="/api/v1/admin/sessioncaisses",  params = {"page", "size"})
    public ResponseEntity<Object> sessionCaisse(@RequestParam("page") int page,
                                                @RequestParam("size") int size) {

        List<SessionCaisse> sessionCaisseList = sessionCaisseService.getAll(PageRequest.of(page, size, Sort.by("createdDate").descending()));

        Page<SessionCaisse> pages = new PageImpl<>(sessionCaisseList, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate")),300);
        PagedModel<EntityModel<SessionCaisse>> result = pagedResourcesAssembler
                .toModel(pages);
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", result);
    }

}
