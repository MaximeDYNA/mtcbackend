package com.catis.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.catis.model.Client;
import com.catis.model.Contact;
import com.catis.model.Partenaire;
import com.catis.objectTemporaire.ClientContactHandler;
import com.catis.objectTemporaire.ClientPartenaire;
import com.catis.service.ContactService;
import com.catis.service.OrganisationService;
import com.catis.service.PartenaireService;

@RestController
@CrossOrigin
public class ContactController {

    @Autowired
    private ContactService contactService;
    @Autowired
    private PartenaireService partenaireService;
    @Autowired
    private OrganisationService os;

    private static Logger LOGGER = LoggerFactory.getLogger(ContactController.class);


    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/contacts")
    public ResponseEntity<Object> addContact(@RequestBody ClientPartenaire clientPartenaire) throws ParseException {
        LOGGER.info("Ajout d'un contact...");

        Contact contact = new Contact();
        Partenaire partenaire = new Partenaire();
        partenaire.setCni(clientPartenaire.getCni());
        if (clientPartenaire.getDateNaiss() != null) {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(clientPartenaire.getDateNaiss());
            partenaire.setDateNaiss(date);
        } else
            partenaire.setDateNaiss(null);


        partenaire.setEmail(clientPartenaire.getEmail());
        partenaire.setTelephone(clientPartenaire.getTelephone());
        partenaire.setNom(clientPartenaire.getNom());
        partenaire.setPrenom(clientPartenaire.getPrenom());
        partenaire.setPassport(clientPartenaire.getPassport());
        partenaire.setLieuDeNaiss(clientPartenaire.getLieuDeNaiss());
        partenaire.setPermiDeConduire(clientPartenaire.getPermiDeConduire());
        partenaire.setOrganisation(os.findByOrganisationId(1L));
        contact.setPartenaire(partenaireService.addPartenaire(partenaire));
        contact.setDescription(clientPartenaire.getVariants());
        contactService.addContact(contact);
        LOGGER.info("Ajout de " + partenaire.getNom() + " réussi");
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", contact);
		/*try {}
		catch (DataIntegrityViolationException integrity) {
			LOGGER.error("Duplicata de champ unique");
			return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "uniq_matricule"
					 , null);
		}catch(Exception e) {
			LOGGER.info("Une erreur est survenu lors de l'ajout d'un client");
			return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Une erreur est survenu lors de "
					+ "l'ajout d'un client", null);
		}	*/
    }

    @RequestMapping(value = "/api/v1/contacts")
    private ResponseEntity<Object> getContacts() {
        LOGGER.info("liste des Contacts...");

        return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "success", contactService.getContacts());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/contacts/addtocustomer")
    public ResponseEntity<Object> ajouterAuClient(@RequestBody ClientContactHandler cch) {
        try {
            LOGGER.info("liste des Contacts...");
            Client client = new Client();
            client.setClientId(cch.getClientId());
            Contact contact = contactService.findById(cch.getContactId());
            contact.setClient(client);

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", contactService.addContact(contact));

        } catch (Exception e) {
            LOGGER.error("Une erreur est survenue");
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "success", null);
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/search/contacts/{keyword}")
    public ResponseEntity<Object> search(@PathVariable String keyword) {
        LOGGER.info("Recherche contacts...");
        try {
            List<ClientPartenaire> clientPartenaires = new ArrayList<>();
            ClientPartenaire cp;

            for (Partenaire p : partenaireService.findPartenaireByNom(keyword)) {
                cp = new ClientPartenaire();
                if (contactService.getContactByPartenaireId(p.getPartenaireId()) != null) {
                    cp.setNom(p.getNom());
                    cp.setPrenom(p.getPrenom());
                    cp.setTelephone(p.getTelephone());
                    cp.setContactId(contactService.getContactByPartenaireId(p.getPartenaireId()).getContactId());
                    clientPartenaires.add(cp);
                }
            }
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "success", clientPartenaires);
        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, true, "Une erreur est survenue", null);

        }


    }


}
