package com.catis.Controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catis.service.CategorieProduitService;
import com.catis.service.ProduitService;


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
	
	private static Logger LOGGER = LoggerFactory.getLogger(ProduitController.class);
	
	@RequestMapping("/api/v1/categorieproduits/{id}/listesproduits")
	public ResponseEntity<Object> listerLesProduits(@PathVariable String id){
		LOGGER.info("Id catégorie: "+id+", liste des produits...");
		return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "success", produitService.findByCategorieProduit(id));
	}

	@RequestMapping("/api/v1/categorieproduits") 
	public ResponseEntity<Object> ListerLesCategorieProduits(){
		LOGGER.info("liste des catégories..."); 
		return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "success",  cateProduitService.listeCategorieProduit());
	}
	

}
