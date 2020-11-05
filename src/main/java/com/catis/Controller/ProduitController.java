package com.catis.Controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catis.service.CarteGriseService;
import com.catis.service.PosaleService;
import com.catis.service.ProduitService;
import com.catis.service.TaxeProduitService;
import com.catis.service.TaxeService;
import com.catis.service.VisiteService;
import com.catis.Controller.exception.VisiteEnCoursException;
import com.catis.model.CarteGrise;
import com.catis.model.Posales;
import com.catis.model.Produit;
import com.catis.model.Taxe;
import com.catis.model.TaxeProduit;
import com.catis.model.Visite;
import com.catis.objectTemporaire.HoldData;
import com.catis.objectTemporaire.ProduitEtTaxe;

@RestController
@CrossOrigin
public class ProduitController {
	
	@Autowired
	private ProduitService produitService;
	@Autowired
	private CarteGriseService cgs;
	@Autowired
	private VisiteService visiteService;
	@Autowired
	private PosaleService posaleService;
	
	@Autowired
	private TaxeProduitService tps;
	@Autowired
	private TaxeService taxeService;

	private static Logger LOGGER = LoggerFactory.getLogger(ProduitController.class);
	
	@RequestMapping("/api/v1/produits")
	public ResponseEntity<Object> listeDesProduits() {
		LOGGER.info("liste des catégories...");
		return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", produitService.findProduitWithoutContreVisite());
	}
	
	@RequestMapping("/api/v1/produits/hold")
	public ResponseEntity<Object> listeDesProduitsParOnglet(@RequestBody HoldData holdeleter ) {
		LOGGER.info("produits par onglet...");
		List <Produit> produits = new ArrayList<>();
		for(Posales posales : posaleService.findByNumberSessionCaisse(holdeleter.getNumber(), holdeleter.getSessionCaisseId())) {
			produits.add(posales.getProduit());
		}
		return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", produits);
	}
	
	
	
	@RequestMapping(value="/api/v1/produits/reference/{imCha}")
	public ResponseEntity<Object> listeDesProduitsParReference(@PathVariable String imCha) throws VisiteEnCoursException {
		try {
				if(visiteService.visiteEncours(imCha))
					throw new VisiteEnCoursException("Une visite est déjà en cours");
				LOGGER.info("liste des catégories...");
				List<Produit> produits = new ArrayList<>();
				for(CarteGrise cg : cgs.findByImmatriculationOuCarteGrise(imCha)) {
					System.out.println("produit de carte grise");
					produits.add(cg.getProduit());
				}
	
				
				if(!visiteService.viensPourContreVisite(imCha)) {
					
					if(cgs.findByImmatriculationOuCarteGrise(imCha).isEmpty()) {
						List<ProduitEtTaxe> pets = new ArrayList<>();
						List<Taxe> taxes;
						ProduitEtTaxe pet; 
						for(Produit produit : produitService.findProduitWithoutContreVisite()) {
							pet = new ProduitEtTaxe();
							pet.setProduit(produit);
							taxes = new ArrayList<>();
							for(TaxeProduit tp: tps.findByProduitId(produit.getProduitId())) {
								taxes.add(tp.getTaxe());
							}
							pet.setTaxe(taxes);
							pets.add(pet);
							
						}
						return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", pets);
					}
					else {
						List<ProduitEtTaxe> pets = new ArrayList<>();
						ProduitEtTaxe pet = new ProduitEtTaxe();
						List <Taxe> taxes = new ArrayList<>();
						pet.setProduit(produitService.findByImmatriculation(imCha));
						
						pet.setTaxe(taxeService.taxListByLibelle(produitService.findByLibelle("cv").getLibelle()));
						pets.add(pet);
						//Visite v =  visiteService.findByReference(imCha).get(0);
						return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", pets);
					}
						
				}
				
				else {
					
					List<ProduitEtTaxe> pets = new ArrayList<>();
					ProduitEtTaxe pet = new ProduitEtTaxe(produitService.findByLibelle("cv"), taxeService.taxListByLibelle("cv") );
					pets.add(pet);
					
					
					return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", pets );
				}
			
			} catch (Exception e) {
				LOGGER.error("Veuillez signaler cette erreur à Franck");
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Veuillez signaler cette erreur à l'equipe CATIS", null);
			}
		
	}
}
