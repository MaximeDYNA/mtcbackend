package com.catis.controller;

import com.catis.model.entity.*;
import com.catis.objectTemporaire.UserInfoIn;
import com.catis.repository.MessageRepository;
import com.catis.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.catis.controller.exception.ContactVideException;
import com.catis.controller.exception.VisiteEnCoursException;
import com.catis.objectTemporaire.Encaissement;
import com.catis.objectTemporaire.EncaissementResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
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
    @Autowired
    private MessageRepository msgRepo;
    @Autowired
    private CaissierService caissierService;

    List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    private static Logger LOGGER = LoggerFactory.getLogger(EncaissementController.class);

    @PostMapping("/api/v1/caisse/encaissements")
    @Transactional
    public ResponseEntity<Object> save(@RequestBody Encaissement encaissement) throws Exception {
        LOGGER.info("ADDING A VISIT...");
        UUID orgId = UserInfoIn.getUserInfo(request).getOrganisanionId();
        String user = UserInfoIn.getUserInfo(request).getLogin();
        try {
        Caissier caissier = caissierService.findBylogin(user);
        if(caissier==null)
            throw new Exception("Please enter a correct login");
        if(!caissier.getSessionCaisses().stream().anyMatch(
                sessionCaisse -> sessionCaisse.isActive()
        ))
            throw new Exception("Please open a session");

        Organisation organisation = os.findByOrganisationId(orgId);

            OperationCaisse op = new OperationCaisse();
            Vente vente = new Vente();
            Partenaire partenaire = new Partenaire();
            Partenaire partenaire2 = new Partenaire();

            double taxedetail;
            /* ---------client------------ */
            if(encaissement.getClientId() == ""){
                if(encaissement.getNomclient().equals("")){
                    vente.setClient(null);
                }
                else{
                    partenaire.setPartenaireId(UUID.randomUUID());
                    partenaire.setNom(encaissement.getNomclient());
                    partenaire.setTelephone(encaissement.getNumeroclient());
                    partenaire.setOrganisation(organisation);
                    Client client =new Client();
                    client.setPartenaire(partenaire);
                    client.setClientId(UUID.randomUUID());
                    client.setOrganisation(organisation);
                    vente.setClient(client);
                }
            }
            else
                vente.setClient(clientService.findCustomerById(UUID.fromString(encaissement.getClientId())));
            /*------------------------------*/


            /* ---------Contact------------ */
            partenaire2.setNom(encaissement.getNomcontacts());
            partenaire2.setPartenaireId(UUID.randomUUID());
            partenaire2.setTelephone(encaissement.getNumerocontacts());
            partenaire2.setOrganisation(organisation);
            Contact contact =new Contact();
            contact.setPartenaire(partenaire2);
            contact.setOrganisation(organisation);
            contact.setContactId(UUID.randomUUID());

            vente.setContact(encaissement.getContactId().equals("") ? contact : contactService.findById(UUID.fromString(encaissement.getContactId())));
            /*------------------------------*/

            /* ---------Session Caisse------------ */
            vente.setSessionCaisse(scs.findSessionCaisseById(encaissement.getSessionCaisseId()));
            /*------------------------------*/

            /* ---------vente------------ */
            vente.setMontantTotal(encaissement.getMontantTotal());
            vente.setMontantHT(encaissement.getMontantHT());
            vente.setNumFacture(venteService.genererNumFacture());
            /* -------------------------- */
            Visite visite;
            ProprietaireVehicule proprietaireVehicule = new ProprietaireVehicule();
            proprietaireVehicule.setPartenaire(contact.getPartenaire());
            proprietaireVehicule.setProprietaireVehiculeId(UUID.randomUUID());
            proprietaireVehicule.setOrganisation(organisation);
            for (Posales posale : posaleService.findActivePosaleBySessionId(encaissement.getSessionCaisseId())) {
                DetailVente detailVente = new DetailVente();
                Produit produit = produitService.findById(posale.getProduit().getProduitId());
                CarteGrise carteGrise = new CarteGrise();

                if (produit.getLibelle().equalsIgnoreCase("cv")) {
                    carteGrise = cgs.findLastByImmatriculationOuCarteGrise(posale.getReference());
                    visite = visiteService.ajouterVisite(carteGrise, encaissement.getMontantTotal(),
                            encaissement.getMontantEncaisse(), orgId,caissier, encaissement.getDocument());
                } else {
                    produit.setProduitId(posale.getProduit().getProduitId());
                    if (!encaissement.getClientId().equals("") )
                        carteGrise.setProprietaireVehicule(
                                pvs.addClientToProprietaire(clientService.findCustomerById(UUID.fromString(encaissement.getClientId()))));
                    else{
                        if(encaissement.getContactId().equals("")){

                            carteGrise.setProprietaireVehicule(proprietaireVehicule);
                        }
                        else{
                            carteGrise.setProprietaireVehicule(
                                    pvs.addContactToProprietaire(contactService.findById(UUID.fromString(encaissement.getContactId()))));
                        }

                    }

                    carteGrise.setNumImmatriculation(posale.getReference());
                    carteGrise.setProduit(produit);
                    carteGrise.setOrganisation(organisation);
                    carteGrise.setCarteGriseId(UUID.randomUUID());
                    visite = visiteService.ajouterVisite(cgs.addCarteGrise(carteGrise), encaissement.getMontantTotal(),
                            encaissement.getMontantEncaisse(), orgId, caissier, encaissement.getDocument());

                }
                /*-----------------Visite-----------------*/

                /*----------------------------------------*/

                /*------------------------------------------*/

                vente.setVisite(visite);
                vente.setOrganisation(organisation);
                vente.setIdVente(UUID.randomUUID());
                vente = venteService.addVente(vente);
                /*------------------------------------------*/

                taxedetail = 0;
                for (TaxeProduit t : produit.getTaxeProduit())
                    taxedetail += t.getTaxe().getValeur();

                detailVente.setProduit(produit);
                detailVente.setPrix(produit.getPrix() + produit.getPrix() * taxedetail / 100);
                detailVente.setVente(vente);
                detailVente.setIdDetailVente(UUID.randomUUID());
                detailVente.setOrganisation(organisation);
                detailVente.setReference(posale.getReference());
                dvs.addVente(detailVente);

            }

            /* ---------Opération de caisse------------ */
            op.setMontant(encaissement.getMontantEncaisse());
            op.setOrganisation(organisation);
            op.setOperationDeCaisseId(UUID.randomUUID());
            op.setSessionCaisse(scs.findSessionCaisseById(encaissement.getSessionCaisseId()));
            op.setNumeroTicket(ocs.genererTicket());
            op.setVente(vente);
            op = ocs.addOperationCaisse(op);


            EncaissementResponse e = new EncaissementResponse(op,
                    detailVenteService.findByVente(op.getVente().getIdVente()), encaissement.getLang());

            Message msg = msgRepo.findByCode("EN001");
            LOGGER.info("VISIT SUCCESSFULLY ADDED");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, e);
         } catch (Exception e) {
            Message msg = msgRepo.findByCode("EN002");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, msg, e.getMessage());
        }


    }
}
