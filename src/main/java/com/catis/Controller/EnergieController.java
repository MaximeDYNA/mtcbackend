package com.catis.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.catis.Controller.message.Message;
import com.catis.model.Energie;
import com.catis.service.EnergieService;

@RestController
@CrossOrigin
public class EnergieController {

    @Autowired
    private EnergieService energieService;


    private static Logger LOGGER = LoggerFactory.getLogger(AdresseController.class);

    @GetMapping("/api/v1/energies")
    public ResponseEntity<Object> energieList() {
        try {

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
                    , energieService.energieList());
        } catch (Exception e) {
            LOGGER.error("Une erreur est survenu lors de l'accès à la liste des energies");
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenu lors de "
                    + "l'ajout d'un client", null);
        }
    }

    @PostMapping("/api/v1/energies")
    public ResponseEntity<Object> energie(@RequestBody Energie energie) {
        try {
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
                    , energieService.addEnergie(energie));
        } catch (Exception e) {
            LOGGER.error("Une erreur est survenu lors de l'accès à la liste des adresses");
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_ADD + "Energie", null);
        }
    }
}
