package com.catis.Controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
import com.catis.model.Client;
import com.catis.model.Partenaire;
import com.catis.service.ClientService;
import com.catis.service.PartenaireService;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@RestController
@CrossOrigin
public class ClientController {
	@Autowired
	private ClientService clientService;
	@Autowired
	private PartenaireService partenaireService;
	private static Logger LOGGER = LoggerFactory.getLogger(ClientController.class);
	
	@RequestMapping(method = RequestMethod.POST, value="/api/v1/clients")
	public void ajouterClient(@RequestBody ClientPartenaire clientPartenaire) {

	}
	
	@RequestMapping(method = RequestMethod.GET, value="/api/v1/clients")
	public  ResponseEntity<Object> listeDesClients(){
		LOGGER.info("liste des clients...");
		return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "success", clientService.findAllCustomer());
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/api/v1/search/clients/{keyword}")
	public  ResponseEntity<Object> search(@PathVariable String keyword){
		LOGGER.info("liste des clients...");
		List<ClientPartenaire> clientPartenaires = new ArrayList<>();
		ClientPartenaire cp; 
		for(Partenaire p : partenaireService.findPartenaireByNom(keyword)) {
			cp = new ClientPartenaire();
			cp.setNom(p.getNom());
			cp.setPrenom(p.getPrenom());
			cp.setTelephone(p.getTelephone());
			if(clientService.findByPartenaire(p.getPartenaireId())!=null)
			cp.setClientId(clientService.findByPartenaire(p.getPartenaireId()).getClientId());
			clientPartenaires.add(cp);
		}
	return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "success", clientPartenaires );
	}
}
