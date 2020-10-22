package com.catis.Controller;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.catis.Controller.objectTemporaire.ClientPartenaire;
import com.catis.model.Partenaire;
import com.catis.model.Vendeur;
import com.catis.service.PartenaireService;
import com.catis.service.VendeurService;

@RestController
@CrossOrigin
public class VendeurController {

	@Autowired
	private VendeurService vendeurService;
	@Autowired
	private PartenaireService partenaireService;
	private static Logger LOGGER = LoggerFactory.getLogger(ContactController.class);
	
	@RequestMapping("/api/v1/vendeurs")
	public ResponseEntity<Object> afficherLesVendeurs(){
		LOGGER.info("liste des vendeurs...");
		return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "success", vendeurService.findAllVendeur());
	}
	@RequestMapping(method = RequestMethod.POST, value="/api/v1/vendeurs")
	public void creerUnVendeur(@RequestBody Vendeur vendeur){
		vendeurService.addVendeur(vendeur);
	}
	@RequestMapping(method = RequestMethod.GET, value="/api/v1/search/vendeurs/{keyword}")
	public  ResponseEntity<Object> search(@PathVariable String keyword){
		LOGGER.info("liste des vendeurs...");
		List<ClientPartenaire> clientPartenaires = new ArrayList<>();
		ClientPartenaire cp; 

		for(Partenaire p : partenaireService.findPartenaireByNom(keyword)) {
			cp = new ClientPartenaire();
			cp.setNom(p.getNom());
			cp.setPrenom(p.getPrenom());
			cp.setTelephone(p.getTelephone());
				if(vendeurService.findVendeurByPartenaireId(p.getPartenaireId())!=null)
				cp.setContactId(vendeurService.findVendeurByPartenaireId(p.getPartenaireId()).getVendeurId());
			clientPartenaires.add(cp);
		}
	return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "success", clientPartenaires );
	}
}
