package com.catis.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.catis.model.Pays;
import com.catis.service.PaysService;

@RestController
public class PaysController {

	@Autowired
	private PaysService paysService;
	
	@RequestMapping(method= RequestMethod.POST, value="/api/v1/adresse/pays")
	public ResponseEntity<Object> addPays(@RequestBody Pays pays){
		
			 return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
					 , paysService.addPays(pays));
		/*try {} catch (Exception e) {
			return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenue lors de l'ajout "
					+ "d'un pays"
					 , null);
		}*/
	}
	@GetMapping("/api/v1/pays")
	public ResponseEntity<Object> listPays(){
		try {
			 return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
					 , paysService.findAllPays());
		} catch (Exception e) {
			return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenue l'affichage de la liste des pays "
					+ "d'un pays"
					 , null);
		}
	}
}
