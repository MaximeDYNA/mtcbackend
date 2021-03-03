package com.catis.Controller;

import com.catis.model.*;
import com.catis.objectTemporaire.UserInfoIn;
import com.catis.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.catis.Controller.exception.ContactVideException;
import com.catis.Controller.exception.VisiteEnCoursException;
import com.catis.objectTemporaire.Encaissement;
import com.catis.objectTemporaire.EncaissementResponse;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@CrossOrigin
public class EncaissementController {
    @Autowired
    HttpServletRequest request;

    @Autowired
    private OperationCaisseService ocs;
    @Autowired
    private OrganisationService os;
    @Autowired
    private SessionCaisseService scs;
    @Autowired
    private VenteService venteService;
    @Autowired
    private DetailVenteService dvs;
    @Autowired
    private CarteGriseService cgs;
    @Autowired
    private ClientService clientService;
    @Autowired
    private VendeurService vendeurService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private ProduitService produitService;
    @Autowired
    private DetailVenteService detailVenteService;
    @Autowired
    private PosaleService posaleService;
    @Autowired
    private VisiteService visiteService;
    @Autowired
    private ProprietaireVehiculeService pvs;
    @Autowired
    private GieglanFileService gieglanFileService;
    @Autowired
    private CategorieTestVehiculeService catSer;

    List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    private static Logger LOGGER = LoggerFactory.getLogger(EncaissementController.class);

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/encaissements")
    @Transactional
    public ResponseEntity<Object> enregistrerEncaissement(@RequestBody Encaissement encaissement)
            throws ContactVideException, VisiteEnCoursException {

        OperationCaisse op = new OperationCaisse();
        Vente vente = new Vente();

        DetailVente detailVente;
        Produit produit;
        CarteGrise carteGrise;
        Vehicule vehicule;
        /* ---------client------------ */
        vente.setClient(
                clientService.findCustomerById(encaissement.getClientId() == 0 ? 1 : encaissement.getClientId()));
        /*------------------------------*/

        /* ---------Vendeur------------ */

        vente.setVendeur(
                vendeurService.findVendeurById(encaissement.getVendeurId() == 0 ? 1 : encaissement.getVendeurId()));
        /*------------------------------*/

        /* ---------Contact------------ */
        vente.setContact(contactService.findById(encaissement.getContactId()));
        /*------------------------------*/

        /* ---------Session Caisse------------ */
        vente.setSessionCaisse(scs.findSessionCaisseById(encaissement.getSessionCaisseId()));
        /*------------------------------*/

        /* ---------vente------------ */
        vente.setMontantTotal(encaissement.getMontantTotal());
        vente.setMontantHT(encaissement.getMontantHT());
        /* -------------------------- */

        /* ---------vente------------ */
        vente.setNumFacture(venteService.genererNumFacture());
        /* -------------------------- */
        Visite visite;
        for (Posales posale : posaleService.findActivePosale()) {
            detailVente = new DetailVente();
            produit = new Produit();
            vehicule = new Vehicule();
            produit = produitService.findById(posale.getProduit().getProduitId());
            carteGrise = new CarteGrise();

            if (produit.getLibelle().equalsIgnoreCase("cv")) {
                carteGrise = cgs.findLastByImmatriculationOuCarteGrise(posale.getReference());
                //carteGrise.setProduit(produit);
                visite = visiteService.ajouterVisite(carteGrise, encaissement.getMontantTotal(),
                        encaissement.getMontantEncaisse(), 1L);
            } else {
                produit.setProduit_id(posale.getProduit().getProduitId());
                if (encaissement.getClientId() != 0)
                    carteGrise.setProprietaireVehicule(
                            pvs.addClientToProprietaire(clientService.findCustomerById(encaissement.getClientId())));
                else
                    carteGrise.setProprietaireVehicule(
                            pvs.addContactToProprietaire(contactService.findById(encaissement.getContactId())));
                carteGrise.setNumImmatriculation(posale.getReference());
                carteGrise.setProduit(produit);
                visite = visiteService.ajouterVisite(cgs.addCarteGrise(carteGrise), encaissement.getMontantTotal(),
                        encaissement.getMontantEncaisse(), Long.valueOf(UserInfoIn.getUserInfo(request).getOrganisanionId() ));

            }
            /*-----------------Visite-----------------*/

            /*----------------------------------------*/

            /*------------------------------------------*/
            vente.setVisite(visite);
            vente = venteService.addVente(vente);
            /*------------------------------------------*/
            detailVente.setProduit(produit);
            detailVente.setVente(vente);
            detailVente.setReference(posale.getReference());
            dvs.addVente(detailVente);

        }

        /* ---------Opération de caisse------------ */
        op.setMontant(encaissement.getMontantEncaisse());
        op.setOrganisation(os.findByOrganisationId(
                Long.valueOf(UserInfoIn.getUserInfo(request).getOrganisanionId()))
                );
        op.setSessionCaisse(scs.findSessionCaisseById(encaissement.getSessionCaisseId()));
        op.setNumeroTicket(ocs.genererTicket());
        op.setVente(vente);
        /* -------------------------- */
        if (op.getMontant() > 0) {
            if (encaissement.getContactId() != 0)
                ocs.addOperationCaisse(op);
            else
                throw new ContactVideException("Erreur : Veuillez renseigner le contact");
        }

        EncaissementResponse e = new EncaissementResponse(op,
                detailVenteService.findByVente(op.getVente().getIdVente()), encaissement.getLang());
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", e);
    }
    /*
     * try {catch(java.util.NoSuchElementException nosuch) {
     * LOGGER.error("Une valeur referencée n'existe pas"); return
     * ApiResponseHandler.generateResponse(HttpStatus.OK, false,
     * "Une valeur referencée n'existe pas", null); } catch(ContactVideException c)
     * { LOGGER.error("Veuillez renseigner le contact"); return
     * ApiResponseHandler.generateResponse(HttpStatus.FORBIDDEN, false,
     * "Veuillez renseigner le contact, si l'erreur persiste contactez CATIS",
     * null); } catch(Exception e) { LOGGER.error("Une erreur est survenue"); return
     * ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false,
     * "Veuillez signaler cette erreur au web master CATIS", null); }
     *
     *
     * }
     */
}
