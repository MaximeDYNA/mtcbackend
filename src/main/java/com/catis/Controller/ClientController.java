package com.catis.Controller;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.catis.model.Client;
import com.catis.model.Partenaire;
import com.catis.objectTemporaire.ClientPartenaire;
import com.catis.service.ClientService;
import com.catis.service.OrganisationService;
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
	@Autowired
	private OrganisationService os;
	private static Logger LOGGER = LoggerFactory.getLogger(ClientController.class);
	
	@RequestMapping(method = RequestMethod.POST, value="/api/v1/clients")
	public ResponseEntity<Object> ajouterClient(@RequestBody ClientPartenaire clientPartenaire) throws ParseException {
			LOGGER.info("Ajout d'un client...");
	
			Client client = new Client();
			Partenaire partenaire = new Partenaire();
			partenaire.setCni(clientPartenaire.getCni());
			System.out.println("*******************"+clientPartenaire.getDateNaiss());
			if(clientPartenaire.getDateNaiss()!=null) {
				Date date = new SimpleDateFormat("yyyy-MM-dd").parse(clientPartenaire.getDateNaiss()); 
				partenaire.setDateNaiss(date);
			}
			else
				partenaire.setDateNaiss(null);
			   
			
			partenaire.setEmail(clientPartenaire.getEmail());
			partenaire.setTelephone(clientPartenaire.getTelephone());
			partenaire.setNom(clientPartenaire.getNom());
			partenaire.setPrenom(clientPartenaire.getPrenom());
			partenaire.setPassport(clientPartenaire.getPassport());
			partenaire.setLieuDeNaiss(clientPartenaire.getLieuDeNaiss());
			partenaire.setPermiDeConduire(clientPartenaire.getPermiDeConduire());
			partenaire.setOrganisation(os.findByOrganisationId(0L));
			client.setPartenaire(partenaireService.addPartenaire(partenaire));
			client.setDescription(clientPartenaire.getVariants());
			clientService.addCustomer(client);
			LOGGER.info("Ajout de "+ partenaire.getNom() +" réussi");
			return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", client);
		/*try {}
		catch (DataIntegrityViolationException integrity) {
			LOGGER.error("Duplicata de champ unique");
			return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "uniq_matricule"
					 , null);
		}catch(Exception e) {
			LOGGER.info("Une erreur est survenu lors de l'ajout d'un client");
			return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Une erreur est survenu lors de "
					+ "l'ajout d'un client", null);
		}*/
		
		
		
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/api/v1/clients")
	public  ResponseEntity<Object> listeDesClients(){
		LOGGER.info("liste des clients...");
		return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "success", clientService.findAllCustomer());
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/api/v1/search/clients/{keyword}")
	public  ResponseEntity<Object> search(@PathVariable String keyword){
		LOGGER.info("Recherche clients...");
		List<ClientPartenaire> clientPartenaires = new ArrayList<>();
		ClientPartenaire cp; 
		for(Partenaire p : partenaireService.findPartenaireByNom(keyword)) {
			if(clientService.findByPartenaire(p.getPartenaireId())!=null) {
				cp = new ClientPartenaire();
				cp.setNom(p.getNom());
				cp.setPrenom(p.getPrenom());
				cp.setTelephone(p.getTelephone());
				cp.setClientId(clientService.findByPartenaire(p.getPartenaireId()).getClientId());
				clientPartenaires.add(cp);
			}
			
		}
	return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "success", clientPartenaires );
	}
}