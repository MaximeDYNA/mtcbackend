package com.catis.Controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.catis.Controller.objectTemporaire.Encaissement;
import com.catis.Controller.objectTemporaire.ProduitVue;
import com.catis.model.CarteGrise;
import com.catis.model.Client;
import com.catis.model.Contact;
import com.catis.model.DetailVente;
import com.catis.model.OperationCaisse;
import com.catis.model.Produit;
import com.catis.model.SessionCaisse;
import com.catis.model.Vendeur;
import com.catis.model.Vente;
import com.catis.service.CaissierCaisseService;
import com.catis.service.CarteGriseService;
import com.catis.service.ClientService;
import com.catis.service.ContactService;
import com.catis.service.DetailVenteService;
import com.catis.service.OperationCaisseService;
import com.catis.service.ProduitService;
import com.catis.service.SessionCaisseService;
import com.catis.service.VendeurService;
import com.catis.service.VenteService;

@RestController
@CrossOrigin
public class EncaissementController {

	@Autowired
	private OperationCaisseService ocs;
	@Autowired
	private SessionCaisseService scs;
	@Autowired
	private CaissierCaisseService ccs;
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

	
	private static Logger LOGGER = LoggerFactory.getLogger(ClientController.class);
	
	@RequestMapping(method = RequestMethod.POST, value="/api/v1/encaissements")
	private ResponseEntity<Object>  enregistrerEncaissement(@RequestBody Encaissement encaissement){
		
		try
		{
			OperationCaisse op = new OperationCaisse();
			Vente vente = new Vente();
			
			DetailVente detailVente;
			Produit produit;
			CarteGrise carteGrise;
			
			/* ---------client------------*/
				vente.setClient(clientService.findCustomerById(encaissement.getClientId()));
			/*------------------------------*/
				
			/* ---------Vendeur------------*/

				vente.setVendeur(vendeurService.findVendeurById(encaissement.getVendeurId()));
			/*------------------------------*/
				
			/* ---------Contact------------*/
				
				vente.setContact(contactService.findById(encaissement.getContactid()));
			/*------------------------------*/
				
			/* ---------Session Caisse------------*/
				vente.setSessionCaisse(scs.findSessionCaisseById(encaissement.getSessionCaisseId()));
			/*------------------------------*/
				
			/* ---------vente------------*/
				vente.setMontantTotal(encaissement.getMontantTotal());
			/* --------------------------*/
			
			
			 vente = venteService.addVente(vente);
			 	
			for(ProduitVue produitVue :  encaissement.getProduitVue()) {
				detailVente = new DetailVente();
				produit = new Produit();
				carteGrise = new CarteGrise();
				produit.setProduit_id(produitVue.getProduitId());
				carteGrise.setNumImmatriculation(produitVue.getReference());
				detailVente.setProduit(produitService.findById(produitVue.getProduitId()));
				detailVente.setVente(vente);
				dvs.addVente(detailVente);
				cgs.addCarteGrise(carteGrise);
			}
			/* ---------Opération de caisse------------*/
			op.setLibelle("Encaissement");
			op.setMontant(encaissement.getMontantEncaisse());
			op.setSessionCaisse(scs.findSessionCaisseById(encaissement.getSessionCaisseId()));
			op.setNumeroTicket(encaissement.getNumeroTicket());
			op.setVente(vente);
			/* --------------------------*/
			 ocs.addOperationCaisse(op);
			 return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "success", op);
		}
		catch(java.util.NoSuchElementException nosuch) {
			LOGGER.error("Une valeur referencée n'existe pas");
			return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Une valeur referencée n'existe pas", null);
		}
		catch(Exception e) {
			LOGGER.error("Une erreur est survenue");
			return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Veuillez signaler cette erreur au web master", null);
		}
		
		
	}
}
