package com.catis.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catis.model.MarqueVehicule;
import com.catis.service.MarqueService;

@RestController
@CrossOrigin
public class MarqueController {

    @Autowired
    private MarqueService marqueService;
    private static Logger LOGGER = LoggerFactory.getLogger(MarqueController.class);

    @GetMapping("/api/v1/marques")
    public ResponseEntity<Object> listMarque() {
        LOGGER.info("List des marques...");
        try {
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", marqueService.marqueList());
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenue", null);

        }
    }

    @PostMapping("/api/v1/marques")
    public ResponseEntity<Object> addMarque(MarqueVehicule marque) {
        LOGGER.info("List des marques...");
        try {
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", marqueService.addMarque(marque));
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenue", null);

        }
    }
}
