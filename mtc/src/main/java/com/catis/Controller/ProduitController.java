package com.catis.Controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catis.service.CarteGriseService;
import com.catis.service.ProduitService;
import com.catis.service.VisiteService;
import com.catis.model.CarteGrise;
import com.catis.model.Produit;
import com.catis.model.Visite;

@RestController
public class ProduitController {
	
	@Autowired
	private ProduitService produitService;
	@Autowired
	private CarteGriseService cgs;
	@Autowired
	private VisiteService visiteService;
	

	private static Logger LOGGER = LoggerFactory.getLogger(ProduitController.class);
	
	@RequestMapping("/api/v1/produits")
	public ResponseEntity<Object> listeDesProduits() {
		LOGGER.info("liste des catégories...");
		return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", produitService.findAllProduit());
	}
	
	@RequestMapping(value="/api/v1/produits/reference/{imCha}")
	public ResponseEntity<Object> listeDesProduitsParReference(@PathVariable String imCha) {
		try {
			LOGGER.info("liste des catégories...");
			List<Produit> produits = new ArrayList<>();
			for(CarteGrise cg : cgs.findByImmatriculationOuCarteGrise(imCha)) {
				produits.add(cg.getProduit());
			}
			Date now = new Date();
			if(visiteService.findByReference(imCha).stream()
												.filter(visites -> !visites.isContreVisite())
												.filter(visites -> (Days
														.daysBetween(visites.getDateDebut(), now).getDays() > 15))
												.collect(Collectors.toList()).isEmpty())
				
			
			return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", produits);
		} catch (Exception e) {
			LOGGER.error("Veuillez signaler cette erreur à Franck");
			return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Veuillez signaler cette erreur à l'equipe CATIS", null);
		}
		
	}
}
