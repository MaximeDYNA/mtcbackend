package com.catis.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.catis.model.Adresse;
import com.catis.service.AdresseService;

@RestController
public class AdresseController {
	
	@Autowired
	private AdresseService adresseService;
	
	
	private static Logger LOGGER = LoggerFactory.getLogger(AdresseController.class);
	
	@GetMapping("/api/v1/adresses")
	public ResponseEntity<Object> adressList() {
		 
			 
			 return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
					 , adresseService.findAll());
		/*try { }
			
			catch(Exception e) {
				LOGGER.error("Une erreur est survenu lors de l'accès à la liste des adresses");
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenu lors de "
						+ "l'ajout d'un client", null);
			}*/
	 }
	@GetMapping("/api/v1/adresse/listview")
	public ResponseEntity<Object> adressListView() {
		
		try {
			LOGGER.info("Liste des adresses demandée");
			
			Map<String ,Object> adresseListView; 
			List<Map<String ,Object>> mapList = new ArrayList<>();
			for(Adresse a : adresseService.findAll()) {
				adresseListView = new HashMap<>();
				adresseListView.put("id", a.getAdresseId());
				adresseListView.put("nom", a.getNom());
				adresseListView.put("description", a.getDescription());
				adresseListView.put("ville", a.getDivisionPays().getLibelle());
				adresseListView.put("pays", a.getPays().getNomPays());
				adresseListView.put("createdDate", a.getCreatedDate());
				adresseListView.put("modifiedDate", a.getModifiedDate());
				mapList.add(adresseListView);
			}
			return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success",  mapList);
			
		} catch (Exception e) {
			LOGGER.error("Une erreur est survenu en affichage mode liste: Visite");
			return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Erreur",  null);
		}
		
	 }
	
	@PostMapping("/api/v1/adresses")
	public ResponseEntity<Object> addAddress(Adresse adresse) {
		try {
			return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
					 , adresseService.addAdresse(adresse));
			
		}
		catch(Exception e) {
			LOGGER.error("Une erreur est survenu lors de l'ajout d'une adresse");
			return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenu lors de "
					+ "l'ajout d'un client", null);
		}
	}

	 /*@RequestMapping(
            path="api/v1/userInfos", 
            method = RequestMethod.GET)
   @ResponseBody
    public void getUserInformation(KeycloakAuthenticationToken token) {
        if(token != null){
            System.out.println("token :" + token);

            try {
                System.out.println(token.getAccount().getKeycloakSecurityContext().getAuthorizationContext().getPermissions());
                System.out.println(token.getAccount().getRoles());
                System.out.println(token.getCredentials());
            } catch (Exception e) {
                // TODO: handle exception
            } 

        }else{
            System.out.println("User not connected.");
        }
    }*/

}
