package com.catis.Controller;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catis.Controller.objectTemporaire.ProduitEtTaxe;
import com.catis.model.Produit;
import com.catis.model.Taxe;
import com.catis.model.TaxeProduit;
import com.catis.service.CategorieProduitService;
import com.catis.service.ProduitService;
import com.catis.service.TaxeProduitService;


@RestController
@CrossOrigin
public class CategorieProduitController {

	/*
	 * @Autowired private CategorieProduitService categorieProduitService;
	 */
	@Autowired
	private ProduitService produitService;
	@Autowired
	private CategorieProduitService cateProduitService;
	@Autowired
	private TaxeProduitService tps;
	
	private static Logger LOGGER = LoggerFactory.getLogger(ProduitController.class);
	
	@RequestMapping("/api/v1/categorieproduits/{idString}/listesproduits")
	public ResponseEntity<Object> listerLesProduits(@PathVariable String idString) throws IllegalArgumentException{
		try {
			List<ProduitEtTaxe> pets = new ArrayList<>();
			List<Taxe> taxes;
			ProduitEtTaxe pet; 
			Long id = Long.valueOf(idString);
			for(Produit produit : produitService.findByCategorieProduit(id)) {
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
		catch(java.lang.IllegalArgumentException il) {
			LOGGER.error("Identifiant catégorie produit incorrect");
			return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Identifiant catégorie produit incorrect", null);
		}
		
	}

	@RequestMapping("/api/v1/categorieproduits") 
	public ResponseEntity<Object> ListerLesCategorieProduits(){
		LOGGER.info("liste des catégories..."); 
		return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "success",  cateProduitService.listeCategorieProduit());
	}
	

}
