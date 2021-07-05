package com.catis.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.keycloak.KeycloakSecurityContext;
import com.catis.Controller.message.Message;
import com.catis.model.entity.Organisation;
import com.catis.objectTemporaire.CaissePOJO;
import com.catis.service.OrganisationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.catis.model.entity.Caisse;
import com.catis.service.CaisseService;

@RestController
@CrossOrigin
public class CaisseController {
    @Autowired
    private CaisseService caisseService;
    @Autowired
    private OrganisationService organisationService;


    private static Logger LOGGER = LoggerFactory.getLogger(CaisseController.class);

    @RequestMapping("/api/v1/caisses")
    public ResponseEntity<Object> afficherLesCaisses() {

        List<Caisse> caisses = caisseService.findAllCaisse();
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success",
                caisses);

    }
    @GetMapping("/api/v1/caisses/select")
    public ResponseEntity<Object> getCaissesOfMtcforSelect(){

        List<Caisse> caisses = caisseService.findAllCaisse();
        List<Map<String, String>> caissesSelect = new ArrayList<>();

        Map<String, String> caisse;


        for(Caisse c: caisses){
            caisse = new HashMap<>();
            caisse.put("id", String.valueOf(c.getCaisse_id()));
            caisse.put("name", c.getLibelle() +" | "+ c.getOrganisation().getNom());
            caissesSelect.add(caisse);
        }

        return ApiResponseHandler.generateResponse(HttpStatus.OK,
                true, Message.ListOK + " Caisses", caissesSelect);
    }


    @PostMapping("/api/v1/caisses")
    public ResponseEntity<Object> save(@RequestBody CaissePOJO caissePOJO) {

        Caisse caisse = new Caisse();
        Organisation organisation = caissePOJO.getOrganisation() == null?
                null : organisationService.findOrganisationById(caissePOJO.getOrganisation());
        caisse.setCaisse_id(caissePOJO.getCaisse_id());
        caisse.setLibelle(caissePOJO.getLibelle() == null ? null : caissePOJO.getLibelle() );
        caisse.setDescription(caissePOJO.getDescription() == null ? null : caissePOJO.getDescription());
        caisse.setOrganisation(organisation);
        caisse = caisseService.addCaisse(caisse);
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success",
                caisse);

    }

    @DeleteMapping("/api/v1/caisses/{id}")
    public ResponseEntity<Object> delete (@PathVariable Long id) {

        caisseService.deleteCaisseById(id);
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success",
                null);

    }
    @RequestMapping(method = RequestMethod.POST, value = "/caisses")
    public void creerUneCaisse(@RequestBody Caisse caisse) {
        caisseService.addCaisse(caisse);
    }
	/*@GetMapping(value="/userconnected")
    public ResponseEntity<Object> getTasks()
    {
		 try {
			 
		        
			 return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès"
					 , getKeycloakSecurityContext().getIdToken().getGivenName());
		 }
			
			catch(Exception e) {
				LOGGER.error("Une erreur est survenu lors de l'accès à la liste des adresses");
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Une erreur est survenu lors de "
						+ "l'ajout d'un client", null);
			}
       
    }
	 private KeycloakSecurityContext getKeycloakSecurityContext()
	    {
	        return (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
	    }*/
}
