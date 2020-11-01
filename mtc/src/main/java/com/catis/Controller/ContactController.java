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
import com.catis.model.Contact;
import com.catis.model.Partenaire;
import com.catis.service.ContactService;
import com.catis.service.PartenaireService;

@RestController
@CrossOrigin
public class ContactController {

	@Autowired
	private ContactService contactService;
	@Autowired
	private PartenaireService partenaireService;
	
	private static Logger LOGGER = LoggerFactory.getLogger(ContactController.class);
	
	
	@RequestMapping(method= RequestMethod.POST, value="/api/v1/contacts")
	private void addContact(@RequestBody Contact contact) {
		
		contactService.addContact(contact);
	}
	@RequestMapping(value="/api/v1/contacts")
	private ResponseEntity<Object> getContacts() {
		LOGGER.info("liste des Contacts...");
		return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "success", contactService.getContacts());
	}
	@RequestMapping(method = RequestMethod.GET, value="/api/v1/search/contacts/{keyword}")
	public  ResponseEntity<Object> search(@PathVariable String keyword){
		LOGGER.info("Recherche contacts...");
		try {
				List<ClientPartenaire> clientPartenaires = new ArrayList<>();
				ClientPartenaire cp; 
		
				for(Partenaire p : partenaireService.findPartenaireByNom(keyword)) {
					cp = new ClientPartenaire();
					if(contactService.getContactByPartenaireId(p.getPartenaireId())!=null) {
						cp.setNom(p.getNom());
						cp.setPrenom(p.getPrenom());
						cp.setTelephone(p.getTelephone());
						cp.setContactId(contactService.getContactByPartenaireId(p.getPartenaireId()).getContactId());
						clientPartenaires.add(cp);
					}
				}
				return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "success", clientPartenaires);
			} 
		catch(Exception e){ 
				return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, true, "Une erreur est survenue", null );
				  
		}
			
		
	}
	
	
}
