package com.catis.Controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catis.model.Vehicule;
import com.catis.service.VehiculeService;

@RestController
@CrossOrigin
public class VehiculeController {

	@Autowired
	private VehiculeService vehiculeService;
	
	private static Logger LOGGER = LoggerFactory.getLogger(VehiculeController.class);
	
	@GetMapping("/api/v1/vehicules")
	public ResponseEntity<Object> vehiculeList() {
		 try {
			 return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
					 , vehiculeService.vehiculeList());
		 }
			
			catch(Exception e) {
				LOGGER.error("Une erreur est survenu lors de l'accès à la liste des adresses");
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenu", null);
			}
	 }
	@PostMapping("/api/v1/vehicules")
	public ResponseEntity<Object> addVehicule(Vehicule vehicule) {
		 try {
			 return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
					 , vehiculeService.addVehicule(vehicule));
		 }
			
			catch(Exception e) {
				LOGGER.error("Une erreur est survenu lors de l'accès à la liste des adresses");
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenu", null);
			}
	 }
	@GetMapping("/api/v1/search/vehicules/{chassis}")
	public ResponseEntity<Object> searchVehicule(@PathVariable String chassis) {
		try {
			
			 LOGGER.info("recherche de véhicule...");

			 return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès",vehiculeService.findByChassis(chassis) );
		  }
			catch(Exception e) {
				LOGGER.error("Une erreur est survenu lors de l'accès à la liste des adresses");
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenu", null);
			}
	 }
}
