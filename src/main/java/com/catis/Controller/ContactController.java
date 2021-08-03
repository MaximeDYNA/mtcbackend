package com.catis.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.catis.model.entity.Message;
import com.catis.model.entity.Organisation;
import com.catis.objectTemporaire.*;
import com.catis.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.catis.model.entity.Contact;
import com.catis.model.entity.Partenaire;
import com.catis.service.ContactService;
import com.catis.service.OrganisationService;
import com.catis.service.PartenaireService;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
public class ContactController {

    @Autowired
    private ContactService contactService;
    @Autowired
    private PartenaireService partenaireService;
    @Autowired
    private OrganisationService os;
    @Autowired
    private MessageRepository msgRepo;
    @Autowired
    HttpServletRequest request;

    private static Logger LOGGER = LoggerFactory.getLogger(ContactController.class);


    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/caisse/contacts")
    public ResponseEntity<Object> addContact(@RequestBody ClientPartenaire clientPartenaire) throws ParseException {

        try{
            LOGGER.trace("Ajout d'un contact...");

            Contact contact = new Contact();
            Partenaire partenaire = new Partenaire();
            partenaire.setCni(clientPartenaire.getCni());


            if (clientPartenaire.getDateNaiss() != null) {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(clientPartenaire.getDateNaiss());
                partenaire.setDateNaiss(date);
            } else
                partenaire.setDateNaiss(null);

            Long orgId = Long.valueOf(UserInfoIn.getUserInfo(request).getOrganisanionId());
            partenaire.setEmail(clientPartenaire.getEmail());
            partenaire.setTelephone(clientPartenaire.getTelephone());
            partenaire.setNom(clientPartenaire.getNom());

            partenaire.setPrenom(clientPartenaire.getPrenom());
            partenaire.setPassport(clientPartenaire.getPassport());
            partenaire.setLieuDeNaiss(clientPartenaire.getLieuDeNaiss());
            partenaire.setPermiDeConduire(clientPartenaire.getPermiDeConduire());
            partenaire.setOrganisation(os.findByOrganisationId(orgId));
            partenaire.setContact(contact);
            contact.setPartenaire(partenaire);
            contact.setDescription(clientPartenaire.getVariants());
            contactService.addContact(contact);
            LOGGER.trace("Ajout de " + partenaire.getNom() + " réussi");
            Message message = msgRepo.findByCode("CT001");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, message, contact);

        }catch (Exception e){
            e.printStackTrace();
            Message message = msgRepo.findByCode("CT002");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, message, null);

        }

		/*try {}
		catch (DataIntegrityViolationException integrity) {
			LOGGER.error("Duplicata de champ unique");
			return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "uniq_matricule"
					 , null);
		}catch(Exception e) {
			LOGGER.trace("Une erreur est survenu lors de l'ajout d'un client");
			return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Une erreur est survenu lors de "
					+ "l'ajout d'un client", null);
		}	*/
    }

    @RequestMapping(value = "/api/v1/caisse/contacts")
    private ResponseEntity<Object> getContacts() {
        LOGGER.trace("liste des Contacts...");

        return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "success", contactService.getContacts());
    }
    @GetMapping("/api/v1/admin/contacts")
    private ResponseEntity<Object> getAdminContacts() {
        LOGGER.trace("liste des Contacts...");
        List<Contact> contacts = contactService.getContacts();
        List<ContactDTO> contactDTOs = new ArrayList<>();

        ContactDTO c;
        for(Contact contact : contacts){
            c = new ContactDTO();
            c.setCni(contact.getPartenaire().getCni());
            c.setContactId(contact.getContactId());
            c.setEmail(contact.getPartenaire().getEmail());
            c.setTelephone(contact.getPartenaire().getTelephone());
            c.setCreatedDate(contact.getCreatedDate());
            c.setNom(contact.getPartenaire().getNom());
            c.setPrenom(contact.getPartenaire().getPrenom());
            c.setDateNaiss(contact.getPartenaire().getDateNaiss());
            c.setPermiDeConduire(contact.getPartenaire().getPermiDeConduire());
            c.setOrganisation(contact.getOrganisation());
            c.setLieuDeNaiss(contact.getPartenaire().getLieuDeNaiss());
            c.setPassport(contact.getPartenaire().getPassport());
            c.setDescription(contact.getDescription());
            contactDTOs.add(c);

        }
        return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "success", contactDTOs);
    }
    @DeleteMapping("/api/v1/admin/contacts/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) throws ParseException {
        try {
            contactService.deleteById(id);
            return ApiResponseHandler.generateResponse(HttpStatus.OK,
                    true, "OK", null);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.OK,
                    true, "KO", null);
        }

    }
    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/admin/contacts")
    public ResponseEntity<Object> addClient(@RequestBody ContactPOJO contactPOJO) throws ParseException {
        try {
            LOGGER.trace("Ajout d'un client...");
            Contact contact = new Contact();
            Partenaire partenaire = new Partenaire();
            partenaire.setCni(contactPOJO.getCni());
            Date date = contactPOJO.getDateNaiss() == null ? null:
                    contactPOJO.getDateNaiss();

            partenaire.setEmail(contactPOJO.getEmail());
            partenaire.setTelephone(contactPOJO.getTelephone());
            partenaire.setNom(contactPOJO.getNom());
            partenaire.setPrenom(contactPOJO.getPrenom());
            partenaire.setPassport(contactPOJO.getPassport());
            partenaire.setDateNaiss(date);
            partenaire.setLieuDeNaiss(contactPOJO.getLieuDeNaiss());
            partenaire.setPermiDeConduire(contactPOJO.getPermiDeConduire());
            Organisation organisation = contactPOJO.getOrganisationId()==null ? null : os.findByOrganisationId(contactPOJO.getOrganisationId());
            partenaire.setOrganisation(organisation);
            contact.setContactId(contactPOJO.getContactId());
            contact.setPartenaire(partenaire);
            contact.setOrganisation(organisation);
            contact.setDescription(contactPOJO.getDescription());
            contact = contactService.addContact(contact);
            LOGGER.trace("Ajout de " + partenaire.getNom() + " réussi");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", contact);
        } catch (DataIntegrityViolationException integrity) {
            LOGGER.error("Duplicata de champ unique");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "uniq_matricule"
                    , null);
        } catch (Exception e) {
            LOGGER.trace("Une erreur est survenu lors de l'ajout d'un client");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "Une erreur est survenu lors de "
                    + "l'ajout d'un client", null);
        }

    }
    /*@RequestMapping(method = RequestMethod.POST, value = "/api/v1/contacts/addtocustomer")
    public ResponseEntity<Object> ajouterAuClient(@RequestBody ClientContactHandler cch) {
        try {
            LOGGER.trace("liste des Contacts...");
            Client client = new Client();
            client.setClientId(cch.getClientId());
            Contact contact = contactService.findById(cch.getContactId());
            contact.setClient(client);

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", contactService.addContact(contact));

        } catch (Exception e) {
            LOGGER.error("Une erreur est survenue");
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "success", null);
        }

    }*/

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/caisse/search/contacts/{keyword}")
    public ResponseEntity<Object> search(@PathVariable String keyword) {
        LOGGER.trace("Recherche contacts...");
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
