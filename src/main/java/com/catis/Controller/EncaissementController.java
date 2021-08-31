package com.catis.Controller;

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

import com.catis.Controller.exception.ContactVideException;
import com.catis.Controller.exception.VisiteEnCoursException;
import com.catis.objectTemporaire.Encaissement;
import com.catis.objectTemporaire.EncaissementResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private MessageRepository msgRepo;

    List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    private static Logger LOGGER = LoggerFactory.getLogger(EncaissementController.class);

    @PostMapping("/api/v1/caisse/encaissements")
    @Transactional
    public ResponseEntity<Object> save(@RequestBody Encaissement encaissement) throws ContactVideException, VisiteEnCoursException {
        Long orgId = Long.valueOf(UserInfoIn.getUserInfo(request).getOrganisanionId());
        Organisation organisation = os.findByOrganisationId(orgId);

            OperationCaisse op = new OperationCaisse();
            Vente vente = new Vente();
            Partenaire partenaire = new Partenaire();
            Partenaire partenaire2 = new Partenaire();

            double taxedetail;
            /* ---------client------------ */
            if(encaissement.getClientId()==0){
                if(encaissement.getNomclient().equals("")){
                    vente.setClient(null);
                }
                else{
                    partenaire.setNom(encaissement.getNomclient());
                    partenaire.setTelephone(encaissement.getNumeroclient());
                    Client client =new Client();
                    client.setPartenaire(partenaire);
                    vente.setClient(client);
                }
            }
            else
                vente.setClient(clientService.findCustomerById(encaissement.getClientId()));
            /*------------------------------*/


            /* ---------Contact------------ */
            partenaire2.setNom(encaissement.getNomcontacts());
            partenaire2.setTelephone(encaissement.getNumerocontacts());
            Contact contact =new Contact();
            contact.setPartenaire(partenaire2);

            vente.setContact(encaissement.getContactId() == 0 ? contact : contactService.findById(encaissement.getContactId()));
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
            for (Posales posale : posaleService.findActivePosaleBySessionId(encaissement.getSessionCaisseId())) {
                DetailVente detailVente = new DetailVente();

                Produit produit = produitService.findById(posale.getProduit().getProduitId());
                CarteGrise carteGrise = new CarteGrise();

                if (produit.getLibelle().equalsIgnoreCase("cv")) {
                    carteGrise = cgs.findLastByImmatriculationOuCarteGrise(posale.getReference());
                    visite = visiteService.ajouterVisite(carteGrise, encaissement.getMontantTotal(),
                            encaissement.getMontantEncaisse(), orgId);
                } else {
                    produit.setProduit_id(posale.getProduit().getProduitId());
                    if (encaissement.getClientId() != 0)
                        carteGrise.setProprietaireVehicule(
                                pvs.addClientToProprietaire(clientService.findCustomerById(encaissement.getClientId())));
                    else{
                        if(encaissement.getContactId() == 0){

                            carteGrise.setProprietaireVehicule(proprietaireVehicule);
                        }
                        else{
                            carteGrise.setProprietaireVehicule(
                                    pvs.addContactToProprietaire(contactService.findById(encaissement.getContactId())));
                        }

                    }

                    carteGrise.setNumImmatriculation(posale.getReference());
                    carteGrise.setProduit(produit);
                    carteGrise.setOrganisation(organisation);
                    visite = visiteService.ajouterVisite(cgs.addCarteGrise(carteGrise), encaissement.getMontantTotal(),
                            encaissement.getMontantEncaisse(), orgId);

                }
                /*-----------------Visite-----------------*/

                /*----------------------------------------*/

                /*------------------------------------------*/

                vente.setVisite(visite);
                vente.setOrganisation(organisation);
                vente = venteService.addVente(vente);
                /*------------------------------------------*/
                detailVente.setProduit(produit);
                taxedetail = 0;
                for (TaxeProduit t : produit.getTaxeProduit())
                    taxedetail += t.getTaxe().getValeur();


                detailVente.setPrix(produit.getPrix() + produit.getPrix() * taxedetail / 100);
                detailVente.setVente(vente);
                detailVente.setOrganisation(organisation);
                detailVente.setReference(posale.getReference());
                dvs.addVente(detailVente);

            }

            /* ---------Opération de caisse------------ */
            op.setMontant(encaissement.getMontantEncaisse());
            op.setOrganisation(organisation);
            op.setSessionCaisse(scs.findSessionCaisseById(encaissement.getSessionCaisseId()));
            op.setNumeroTicket(ocs.genererTicket());
            op.setVente(vente);
            op = ocs.addOperationCaisse(op);


            EncaissementResponse e = new EncaissementResponse(op,
                    detailVenteService.findByVente(op.getVente().getIdVente()), encaissement.getLang());

            Message msg = msgRepo.findByCode("EN001");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, e);
           /*      try { } catch (Exception e) {
            Message msg = msgRepo.findByCode("EN002");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, msg, e);
        }*/


    }
}
