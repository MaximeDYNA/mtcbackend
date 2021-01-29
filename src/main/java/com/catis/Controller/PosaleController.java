package com.catis.Controller;


import java.util.ArrayList;
import java.util.List;

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

import com.catis.Controller.exception.DecaissementExistantException;
import com.catis.Controller.exception.InformationIncompleteException;
import com.catis.Controller.exception.ProduitNonDisponibleException;
import com.catis.Controller.exception.VisiteEnCoursException;
import com.catis.model.Hold;
import com.catis.model.Posales;
import com.catis.model.Taxe;
import com.catis.model.TaxeProduit;
import com.catis.objectTemporaire.Card;
import com.catis.objectTemporaire.HoldData;
import com.catis.objectTemporaire.PosaleData;
import com.catis.objectTemporaire.PosaleDataForDelete;
import com.catis.objectTemporaire.ProduitEtTaxe;
import com.catis.service.CarteGriseService;
import com.catis.service.HoldService;
import com.catis.service.PosaleService;
import com.catis.service.ProduitService;
import com.catis.service.TaxeProduitService;
import com.catis.service.VisiteService;

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

    private static Logger LOGGER = LoggerFactory.getLogger(PosaleController.class);

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/posales")
    public ResponseEntity<Object> ajouterPosales(@RequestBody PosaleData posaleData) {

        try {
            LOGGER.info("Ajout d'un produit dans un onglet");
            if (posaleService.isDecaissementExist(posaleData.getHoldId(), posaleData.getSessionCaisseId())) {
                throw new DecaissementExistantException();

            }


            Hold hold = hs.findByHoldId(posaleData.getHoldId());
            Posales posale = new Posales();
            posale.setHold(hold);
            posale.setProduit(produitService.findById(posaleData.getProduitId()));
            if (visiteService.visiteEncours(posaleData.getReference()))
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


            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", card);
        } catch (DataIntegrityViolationException integrity) {
            LOGGER.error("Duplicata de champ unique");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "uniq_matricule"
                    , null);
        } catch (DecaissementExistantException integrity) {
            LOGGER.error("Décaissement exitant");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "exists_dec"
                    , null);
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'ajout d'un produit dans l'onglet");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Une erreur est survenu"
                    + "bien vouloir le signaler à l'équipe CATIS", null);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/posaleslist")
    public ResponseEntity<Object> listPosales(@RequestBody HoldData holdData) {
        try {
            if (!holdData.isValid())
                throw new InformationIncompleteException("Bien vouloir envoyer toutes les informations de l'onglet");
            LOGGER.info("liste des produits de l'onglet " + holdData.getSessionCaisseId());
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
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", cards);
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'ajout d'un produit dans l'onglet");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Une erreur est survenu"
                    + "bien vouloir le signaler à l'équipe CATIS", null);
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/deleteposale")
    public ResponseEntity<Object> deletePosales(@RequestBody PosaleDataForDelete posaleData) {

        LOGGER.info("supression de " + posaleData.getReference() + " du panier");
        try {
            if (!posaleService.findByReferenceSessionCaisse(posaleData.getReference(), posaleData.getSessionCaisseId()).isEmpty())
                posaleService.deletePosalesByReference(posaleData.getReference(), posaleData.getSessionCaisseId());
            else
                throw new ProduitNonDisponibleException();
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "supprimé avec succès", null);
        } catch (ProduitNonDisponibleException p) {
            LOGGER.error("Le produit n'est disponible dans le panier");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Le produit n'est plus disponible dans le panier", null);
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'ajout d'un produit dans l'onglet");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Une erreur est survenu"
                    + "bien vouloir le signaler à l'équipe CATIS", null);
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/posale/decaissement")
    public ResponseEntity<Object> ajoutDecaissement(@RequestBody PosaleData posaleData) {

        LOGGER.info("Ajout d'un décaissement au panier");
        try {
            posaleService.deletePosale(posaleData.getHoldId(), posaleData.getSessionCaisseId());
            Posales p = new Posales();
            Hold hold = hs.findByHoldId(posaleData.getHoldId());
            Posales posale = new Posales();
            posale.setHold(hold);
            posale.setProduit(produitService.findById(1L));
            posale.setReference(posaleData.getReference());
            posale.setStatus(true);
            posale.setSessionCaisse(hold.getSessionCaisse());
            posaleService.addPosales(posale);

            if (!posaleService.findByReferenceSessionCaisse(posaleData.getReference(), posaleData.getSessionCaisseId()).isEmpty())
                posaleService.deletePosalesByReference(posaleData.getReference(), posaleData.getSessionCaisseId());
            else
                throw new ProduitNonDisponibleException();
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "supprimé avec succès", null);
        } catch (ProduitNonDisponibleException p) {
            LOGGER.error("Le produit n'est disponible dans le panier");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Le produit n'est plus disponible dans le panier", null);
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'ajout d'un produit dans l'onglet");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Une erreur est survenu"
                    + "bien vouloir le signaler à l'équipe CATIS", null);
        }

    }

}
