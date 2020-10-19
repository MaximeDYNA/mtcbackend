package com.catis.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.catis.service.ProduitService;

@RestController
public class ProduitController {
	
	@Autowired
	private ProduitService produitService;

	private static Logger LOGGER = LoggerFactory.getLogger(ProduitController.class);
	@RequestMapping("/api/v1/produits")
	public ResponseEntity<Object> listeDesProduits() {
		LOGGER.info("liste des catégories...");
		return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "success", produitService.findAllProduit());
	}
}
