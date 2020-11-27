package com.catis.Controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.catis.Controller.exception.ContactVideException;
import com.catis.model.CarteGrise;
import com.catis.model.DetailVente;
import com.catis.model.OperationCaisse;
import com.catis.model.Posales;
import com.catis.model.Produit;
import com.catis.model.Vehicule;
import com.catis.model.Vente;
import com.catis.model.Visite;
import com.catis.objectTemporaire.Encaissement;
import com.catis.objectTemporaire.EncaissementResponse;
import com.catis.service.CarteGriseService;
import com.catis.service.ClientService;
import com.catis.service.ContactService;
import com.catis.service.DetailVenteService;
import com.catis.service.OperationCaisseService;
import com.catis.service.PosaleService;
import com.catis.service.ProduitService;
import com.catis.service.ProprietaireVehiculeService;
import com.catis.service.SessionCaisseService;
import com.catis.service.VendeurService;
import com.catis.service.VenteService;
import com.catis.service.VisiteService;

@RestController
@CrossOrigin
public class EncaissementController {

	@Autowired
	private OperationCaisseService ocs;
	@Autowired
	private SessionCaisseService scs;
	@Autowired 
	private VenteService venteService;
	@Autowired
	private DetailVenteService dvs;
	@Autowired
	private CarteGriseService cgs;
	@Autowired
	private ClientService clientService ;
	@Autowired
	private VendeurService vendeurService ;
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
	

	
	private static Logger LOGGER = LoggerFactory.getLogger(EncaissementController.class);
	
	@RequestMapping(method = RequestMethod.POST, value="/api/v1/encaissements")
	@Transactional
	public ResponseEntity<Object>  enregistrerEncaissement(@RequestBody Encaissement encaissement) throws ContactVideException{
	
			OperationCaisse op = new OperationCaisse();
			Vente vente = new Vente();
			Visite visite= new Visite();
			
			DetailVente detailVente;
			Produit produit;
			CarteGrise carteGrise;
			Vehicule vehicule;
			/* ---------client------------*/
				vente.setClient(clientService.findCustomerById(encaissement.getClientId()));
			/*------------------------------*/
				
			/* ---------Vendeur------------*/
				
				vente.setVendeur(vendeurService.findVendeurById(encaissement.getVendeurId()));
			/*------------------------------*/
				
			/* ---------Contact------------*/
				vente.setContact(contactService.findById(encaissement.getContactId()));
			/*------------------------------*/
				
			/* ---------Session Caisse------------*/
				vente.setSessionCaisse(scs.findSessionCaisseById(encaissement.getSessionCaisseId()));
			/*------------------------------*/
				
			/* ---------vente------------*/
				vente.setMontantTotal(encaissement.getMontantTotal());
				vente.setMontantHT(encaissement.getMontantHT());
			/* --------------------------*/
				
			/* ---------vente------------*/
				vente.setNumFacture(venteService.genererNumFacture());;
			/* --------------------------*/
			
			
			 vente = venteService.addVente(vente);
			 	
			for(Posales posale 	:  posaleService.findActivePosale()) {
				detailVente = new DetailVente();
				produit = new Produit();
				vehicule = new Vehicule();
				produit = produitService.findById(posale.getProduit().getProduitId());
				carteGrise = new CarteGrise();
				
				produit.setProduit_id(posale.getProduit().getProduitId());
				if(encaissement.getClientId()!=0)
					carteGrise.setProprietaireVehicule(
							pvs.addClientToProprietaire(clientService
									.findCustomerById(encaissement.getClientId()))
							);
				else
					carteGrise.setProprietaireVehicule(
							pvs.addContactToProprietaire(contactService
									.findById(encaissement.getContactId())));
				carteGrise.setNumImmatriculation(posale.getReference());
				carteGrise.setProduit(produit);
				/*-----------------Visite-----------------*/
					visiteService.ajouterVisite(cgs.addCarteGrise(carteGrise),
													encaissement.getMontantEncaisse());
				/*----------------------------------------*/
				detailVente.setProduit(produit);
				detailVente.setVente(vente);
				detailVente.setReference(posale.getReference());
				dvs.addVente(detailVente);
				
			}
			/* ---------Opération de caisse------------*/
			op.setType(encaissement.isType());
			op.setMontant(encaissement.getMontantEncaisse());
			
			op.setSessionCaisse(scs.findSessionCaisseById(encaissement.getSessionCaisseId()));
			op.setNumeroTicket(ocs.genererTicket());
			op.setVente(vente);
			/* --------------------------*/
				if(op.getMontant()>0) {
					if(encaissement.getContactId()!= 0)
						ocs.addOperationCaisse(op);
					else 
					throw new ContactVideException("Erreur : Veuillez renseigner le contact");
				}
					
			
				
				
			
			 EncaissementResponse e = new EncaissementResponse(op, detailVenteService.findByVente(op.getVente().getIdVente()), encaissement.getLang() );
			 return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", e );
		/*	try
		{}
		catch(java.util.NoSuchElementException nosuch) {
			LOGGER.error("Une valeur referencée n'existe pas");
			return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Une valeur referencée n'existe pas", null);
		}*/
		/*catch(ContactVideException c) {
			LOGGER.error("Veuillez renseigner le contact");
			return ApiResponseHandler.generateResponse(HttpStatus.FORBIDDEN, false, "Veuillez renseigner le contact, si l'erreur persiste contactez CATIS", null);
		}*/
	/*	catch(Exception e) {
			LOGGER.error("Une erreur est survenue");
			return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Veuillez signaler cette erreur au web master CATIS", null);
		}*/
		
		
	}
}
