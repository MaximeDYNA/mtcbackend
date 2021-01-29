package com.catis.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.catis.model.DivisionPays;
import com.catis.service.DivisionPaysService;

@RestController
public class DivisionPaysController {

    @Autowired
    private DivisionPaysService divisionService;


    private static Logger LOGGER = LoggerFactory.getLogger(DivisionPaysController.class);

    @GetMapping("/api/v1/adresses/pays/divisionpays")
    public ResponseEntity<Object> divisionPaysList() {
        try {
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
                    , divisionService.findAllDivisionPays());
        } catch (Exception e) {
            LOGGER.error("Une erreur est survenu lors de l'accès à la liste des adresses");
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "", null);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/adresses/pays/divisionpays")
    public ResponseEntity<Object> addDivisionPays(@RequestBody DivisionPays dp) {

        DivisionPays divisionPays = divisionService.addDivisionPays(dp);
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
                , divisionPays);
			
		/*try {}
		catch(Exception e) {
			LOGGER.error("Une erreur est survenu lors de l'ajout d'une ville");
			return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Erreur lors de l'ajout d'une ville", null);
		}*/
    }
}
