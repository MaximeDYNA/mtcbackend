package com.catis.controller;


import java.util.ArrayList;
import java.util.List;

import com.catis.model.entity.*;
import com.catis.objectTemporaire.*;
import com.catis.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.catis.controller.exception.DecaissementExistantException;
import com.catis.controller.exception.InformationIncompleteException;
import com.catis.controller.exception.ProduitNonDisponibleException;
import com.catis.controller.exception.VisiteEnCoursException;
import com.catis.service.HoldService;
import com.catis.service.PosaleService;
import com.catis.service.ProduitService;
import com.catis.service.TaxeProduitService;
import com.catis.service.VisiteService;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
public class PosaleController {

    @Autowired
    private PosaleService posaleService;
    @Autowired
    private VisiteService visiteService;
    @Autowired
    private HoldService hs;
    @Autowired
    private ProduitService produitService;
    @Autowired
    private TaxeProduitService tps;
    @Autowired
    HttpServletRequest request;
    @Autowired
    private MessageRepository msgRepo;

    private static Logger LOGGER = LoggerFactory.getLogger(PosaleController.class);

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/caisse/posales")
    public ResponseEntity<Object> ajouterPosales(@RequestBody PosaleData posaleData) {

        try {
            LOGGER.info("ADD PRODUCT IN CARD");
            Message msg;

            if (posaleService.isDecaissementExist(posaleData.getHoldId(), posaleData.getSessionCaisseId())) {
                throw new DecaissementExistantException();
            }

            Hold hold = hs.findByHoldId(posaleData.getHoldId());
            Posales posale = new Posales();
            posale.setHold(hold);
            posale.setProduit(produitService.findById(posaleData.getProduitId()));
            if (visiteService.visiteEncours(posaleData.getReference(),
                    UserInfoIn.getUserInfo(request).getOrganisanionId()))
                throw new VisiteEnCoursException();
            posale.setReference(posaleData.getReference());
            posale.setStatus(true);
            posale.setSessionCaisse(hold.getSessionCaisse());

            ProduitEtTaxe card = new ProduitEtTaxe();
            card.setProduit(posaleService.addPosales(posale).getProduit());

            List<Taxe> taxes = new ArrayList<>();
            for (TaxeProduit tp : tps.findByProduitId(card.getProduit().getProduitId())) {
                taxes.add(tp.getTaxe());
            }
            card.setTaxe(taxes);
            msg = msgRepo.findByCode("PS001");
            LOGGER.info("PRODUCT SUCCESSFULLY ADDED");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, card);
        } catch (DataIntegrityViolationException integrity) {
            LOGGER.error("Duplicata de champ unique");
            Message msg = msgRepo.findByCode("PS002");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg
                    , null);
        } catch (DecaissementExistantException integrity) {
            LOGGER.error("Décaissement exitant");
            Message msg = msgRepo.findByCode("PS004");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg
                    , null);
        } catch (VisiteEnCoursException v) {
            LOGGER.error("Une visite est actuellement en cours pour ce véhicule");
            Message msg = msgRepo.findByCode("PS008");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);
        }

        catch (Exception e) {
            LOGGER.error("Erreur lors de l'ajout d'un produit dans le panier");
            e.printStackTrace();
            Message msg = msgRepo.findByCode("PS003");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/caisse/posaleslist")
    public ResponseEntity<Object> listPosales(@RequestBody HoldData holdData) {
        try {
            Message msg;
            if (!holdData.isValid()){
                msg = msgRepo.findByCode("HL000");
                throw new InformationIncompleteException(msg.getCode());
            }

            LOGGER.trace("liste des produits de l'onglet " + holdData.getSessionCaisseId());
            Card card;
            List<Card> cards = new ArrayList<>();
            List<Taxe> taxes = new ArrayList<>();
            for (Posales p : posaleService.findByNumberSessionCaisse(holdData.getNumber(), holdData.getSessionCaisseId())) {
                card = new Card();
                card.setProduit(p.getProduit());
                card.setReference(p.getReference());
                for (TaxeProduit tp : tps.findByProduitId(p.getProduit().getProduitId())) {
                    taxes.add(tp.getTaxe());
                }
                card.setTaxes(taxes);
                taxes = new ArrayList<>();
                cards.add(card);
            }
            msg = msgRepo.findByCode("HL005");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, cards);
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'ajout d'un produit dans l'onglet");
            Message msg = msgRepo.findByCode("HL004");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/caisse/deleteposale")
    public ResponseEntity<Object> deletePosales(@RequestBody PosaleDataForDelete posaleData) {

        LOGGER.trace("supression de " + posaleData.getReference() + " du panier");
        try {
            if (posaleService.findByReferenceSessionCaisse(posaleData.getReference(), posaleData.getSessionCaisseId()).size()!=0)
                posaleService.deletePosalesByReference(posaleData.getReference(), posaleData.getSessionCaisseId());
            else
                throw new ProduitNonDisponibleException();
            Message msg =msgRepo.findByCode("PS006");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, null);
        } catch (ProduitNonDisponibleException p) {
            LOGGER.error("Le produit n'est plus disponible dans le panier");
            Message msg =msgRepo.findByCode("PS005");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);
        } catch (Exception e) {
            LOGGER.error("Suppression du produit dans le panier réussi");
            Message msg =msgRepo.findByCode("PS007");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/posale/decaissement")
    public ResponseEntity<Object> ajoutDecaissement(@RequestBody PosaleData posaleData) {

        LOGGER.trace("Ajout d'un décaissement au panier");
        try {
            posaleService.deletePosale(posaleData.getNumber(), posaleData.getSessionCaisseId());
            Posales p = new Posales();
            Hold hold = hs.findBynumberAndSession(posaleData.getNumber(), posaleData.getSessionCaisseId());
            Posales posale = new Posales();
            posale.setHold(hold);
            posale.setProduit(produitService.findByLibelle("dec"));
            posale.setReference(posaleData.getReference());
            posale.setStatus(true);
            posale.setSessionCaisse(hold.getSessionCaisse());
            posaleService.addPosales(posale);

            if (!posaleService.findByReferenceSessionCaisse(posaleData.getReference(), posaleData.getSessionCaisseId()).isEmpty())
                posaleService.deletePosalesByReference(posaleData.getReference(), posaleData.getSessionCaisseId());
            else
                throw new ProduitNonDisponibleException();
            Message msg =msgRepo.findByCode("DC001");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, null);
        } catch (ProduitNonDisponibleException p) {
            LOGGER.error("Le produit n'est disponible dans le panier");
            Message msg =msgRepo.findByCode("PS005");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'ajout d'un produit dans l'onglet");
            Message msg =msgRepo.findByCode("DC002");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, null);
        }

    }

}
