package com.catis.Controller;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.catis.Controller.message.Message;
import com.catis.model.Client;
import com.catis.model.Contact;
import com.catis.model.Partenaire;
import com.catis.model.Vendeur;
import com.catis.objectTemporaire.ClientPartenaire;
import com.catis.service.OrganisationService;
import com.catis.service.PartenaireService;
import com.catis.service.VendeurService;

@RestController
@CrossOrigin
public class VendeurController {

	@Autowired
	private VendeurService vendeurService;
	@Autowired
	private PartenaireService partenaireService;
	@Autowired
	private OrganisationService os;
	private static Logger LOGGER = LoggerFactory.getLogger(ContactController.class);
	
	@RequestMapping("/api/v1/vendeurs")
	public ResponseEntity<Object> afficherLesVendeurs(){
		LOGGER.info("liste des vendeurs...");
		return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "success", vendeurService.findAllVendeur());
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/api/v1/vendeurs")
	@Transactional
	public ResponseEntity<Object> addVendeur(@RequestBody ClientPartenaire clientPartenaire) throws ParseException {
		LOGGER.info("Ajout d'un vendeur...");
		try {
		Vendeur vendeur = new Vendeur();
		Partenaire partenaire = new Partenaire();
		partenaire.setCni(clientPartenaire.getCni());
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
		vendeur.setPartenaire(partenaireService.addPartenaire(partenaire));
		vendeur.setDescription(clientPartenaire.getVariants());
		vendeurService.addVendeur(vendeur);
		LOGGER.info("Ajout de "+ partenaire.getNom() +" réussi");
		return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", vendeur);
	}
	catch (DataIntegrityViolationException integrity) {
		LOGGER.error("Duplicata de champ unique");
		return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "uniq_matricule"
				 , null);
	}catch(Exception e) {
		LOGGER.info("Une erreur est survenu lors de l'ajout d'un client");
		return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Une erreur est survenu lors de "
				+ "l'ajout d'un client", null);
	}
	
	
	
}
	@RequestMapping(method = RequestMethod.GET, value="/api/v1/vendeurs/listview")
	public  ResponseEntity<Object> listeDesClientsView(){
		LOGGER.info("listview vendeurs...");
		try {
			LOGGER.info("Liste des vendeurs");
			Map<String ,Object> listView; 
			List<Map<String ,Object>> mapList = new ArrayList<>();
			for(Vendeur v : vendeurService.findAllVendeur()) {
				listView = new HashMap<>();
				listView.put("id", v.getVendeurId());
				listView.put("nom", v.getPartenaire().getNom());
				listView.put("prenom", v.getPartenaire().getPrenom());
				listView.put("email", v.getPartenaire().getEmail());
				listView.put("tel", v.getPartenaire().getTelephone());
				listView.put("cni", v.getPartenaire().getCni());
				listView.put("createdDate", v.getPartenaire().getCreatedDate());
				listView.put("modifiedDate", v.getPartenaire().getModifiedDate());
				listView.put("description", v.getDescription());
				mapList.add(listView);
			}
			
			return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", mapList);
			
		} catch (Exception e) {
			return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_LIST_VIEW + "Vente", null);
		}

	}
	
	@RequestMapping(method = RequestMethod.GET, value="/api/v1/search/vendeurs/{keyword}")
	public  ResponseEntity<Object> search(@PathVariable String keyword){
		LOGGER.info("Recherche vendeurs...");
		List<ClientPartenaire> clientPartenaires = new ArrayList<>();
		ClientPartenaire cp; 

		for(Partenaire p : partenaireService.findPartenaireByNom(keyword)) {
			cp = new ClientPartenaire();
			if(vendeurService.findVendeurByPartenaireId(p.getPartenaireId())!=null) {
				cp.setNom(p.getNom());
				cp.setPrenom(p.getPrenom());
				cp.setTelephone(p.getTelephone());
				cp.setContactId(vendeurService.findVendeurByPartenaireId(p.getPartenaireId()).getVendeurId());
				clientPartenaires.add(cp);
			}
			
		}
	return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "success", clientPartenaires );
	}
}
