package com.catis.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.catis.model.entity.Message;
import com.catis.model.entity.SessionCaisse;
import com.catis.repository.MessageRepository;
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

import com.catis.model.entity.Hold;
import com.catis.objectTemporaire.HoldData;
import com.catis.service.HoldService;
import com.catis.service.PosaleService;
import com.catis.service.SessionCaisseService;


@RestController
@CrossOrigin
public class HoldController {

    @Autowired
    private HoldService holdService;
    @Autowired
    private SessionCaisseService scs;
    @Autowired
    private PosaleService ps;
    @Autowired
    private MessageRepository msgRepo;

    private static Logger LOGGER = LoggerFactory.getLogger(HoldController.class);

    @RequestMapping(value = "/api/v1/caisse/newhold/{sessionCaisseId}")
    public ResponseEntity<Object> ajouterOnglet(@PathVariable Long sessionCaisseId) throws ParseException {

        try {
            LOGGER.trace("création onglet demandé...");

            Hold hold = new Hold();
            SessionCaisse sessionCaisse =scs.findSessionCaisseById(sessionCaisseId);
            hold.setSessionCaisse(sessionCaisse);
            hold.setNumber(holdService.maxNumber(sessionCaisse) + 1);
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            Date date = format.parse(format.format(new Date()));
            hold.setTime(date);
            hold = holdService.addHold(hold);
            ps.activatePosale(hold.getNumber(), hold.getSessionCaisse().getSessionCaisseId());
            Message msg = msgRepo.findByCode("HL001");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, hold);
        } catch (java.lang.NullPointerException nul) {
            Message msg = msgRepo.findByCode("SS003");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.INTERNAL_SERVER_ERROR, false, msg, null);
        } catch (Exception e) {
            Message msg = msgRepo.findByCode("HL002");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.INTERNAL_SERVER_ERROR, false, msg, null);
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/caisse/deletehold")
    public ResponseEntity<Object> supprimerOnglet(@RequestBody HoldData holdData) throws ParseException {
        try {
            LOGGER.trace("suppression de l'onglet " + holdData.getNumber() + " demandé...");
            holdService.deleteHoldByNumber(holdData.getNumber(), holdData.getSessionCaisseId());
            ps.deletePosale(holdData.getNumber(), holdData.getSessionCaisseId());
            ps.activatePosale(holdService.maxNumber(scs.findSessionCaisseById(holdData.getSessionCaisseId())), holdData.getSessionCaisseId());
            Message msg = msgRepo.findByCode("HL003");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, null);
        } catch (Exception e) {
            e.printStackTrace();
            Message msg = msgRepo.findByCode("HL006");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.INTERNAL_SERVER_ERROR, false,
                    msg, null);
        }


    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/caisse/selecthold")
    public ResponseEntity<Object> selectionnerOnglet(@RequestBody HoldData holdData) throws ParseException {

        try {
            LOGGER.trace("sélection de l'onglet " + holdData.getNumber() + " demandé...");
            ps.activatePosale(holdData.getNumber(), holdData.getSessionCaisseId());
            Message msg = msgRepo.findByCode("HL007");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, holdService.findByNumberSessionCaisse(holdData.getNumber(), holdData.getSessionCaisseId()));
        } catch (Exception e) {
            Message msg = msgRepo.findByCode("HL008");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.INTERNAL_SERVER_ERROR, false,
                    msg, null);
        }


    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/caisse/holdlist/{sessionCaisseId}")
    public ResponseEntity<Object> sessionsHold(@PathVariable Long sessionCaisseId) {

        try {
            LOGGER.trace("sélection des onglets d'une session ID...");

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "onglet de la session id " + sessionCaisseId, holdService.findHoldBySessionCaisse(sessionCaisseId));
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false,
                    "Une erreur est survenue" + " bien vouloir contacter l'équipe CATIS", null);
        }


    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/caisse/hold/refresh")

    public ResponseEntity<Object> refreshHold(@RequestBody HoldData holdData) {


        LOGGER.trace("rafraichissement de l'onglet");
        ps.deletePosale(holdData.getNumber(), holdData.getSessionCaisseId());
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "onglet de la session id " + holdData.getSessionCaisseId(), null);
			 /*	try {} 
			 catch (Exception e) { 
				 return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false,
						 	"Une erreur est survenue" + " bien vouloir contacter l'équipe CATIS", null);
			  }*/


    }


}
