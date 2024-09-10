package com.catis.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.catis.model.entity.Organisation;
import com.catis.objectTemporaire.*;
import com.catis.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.catis.controller.message.Message;
import com.catis.model.entity.Client;
import com.catis.model.entity.Partenaire;
import com.catis.service.ClientService;
import com.catis.service.OrganisationService;
import com.catis.service.PartenaireService;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@RestController
@CrossOrigin
public class ClientController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private PartenaireService partenaireService;
    @Autowired
    private MessageRepository msgRepo;
    @Autowired
    private OrganisationService os;
    @Autowired
    HttpServletRequest request;
    @Autowired
    private PagedResourcesAssembler<ClientDTO> pagedResourcesAssembler;
    private static Logger LOGGER = LoggerFactory.getLogger(ClientController.class);

    @Transactional
    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/caisse/clients")
    public ResponseEntity<Object> ajouterClient(@RequestBody ClientPartenaire clientPartenaire) throws ParseException {

            LOGGER.trace("Ajout d'un client...");
            Client client = new Client();
            Partenaire partenaire = new Partenaire();
            partenaire.setCni(clientPartenaire.getCni());
            if (clientPartenaire.getDateNaiss() != null) {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(clientPartenaire.getDateNaiss());
                ;
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
            UserDTO u = UserInfoIn.getUserInfo(request);
            partenaire.setOrganisation(os.findByOrganisationId(u.getOrganisanionId()));
            client.setDescription(clientPartenaire.getVariants());
            clientService.addCustomer(client);
            LOGGER.trace("Ajout de " + partenaire.getNom() + " réussi");
            com.catis.model.entity.Message message = msgRepo.findByCode("CL001");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, true, message ,client );

       /* try {} catch (DataIntegrityViolationException integrity) {
            LOGGER.error("Duplicata de champ unique");
            com.catis.model.entity.Message message = msgRepo.findByCode("CL002");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, message
                    , null);
        } catch (Exception e) {
            LOGGER.trace("Une erreur est survenu lors de l'ajout d'un client");
            com.catis.model.entity.Message message = msgRepo.findByCode("CL002");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, message, null);
        }*/


    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/caisse/clients")
    public ResponseEntity<Object> listeDesClients() {
        LOGGER.trace("liste des clients...");
        return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "success", clientService.findAllCustomer());
    }
    @Transactional
    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/caisse/clients/listview")
    public ResponseEntity<Object> listeDesClientsView() {
        LOGGER.trace("listview clients...");
        try {
            LOGGER.trace("Liste des produits");
            Map<String, Object> listView;
            List<Map<String, Object>> mapList = new ArrayList<>();
            for (Client c : clientService.findAllCustomer()) {
                listView = new HashMap<>();
                listView.put("id", c.getClientId());
                listView.put("nom", c.getPartenaire().getNom());
                listView.put("prenom", c.getPartenaire().getPrenom());
                listView.put("email", c.getPartenaire().getEmail());
                listView.put("tel", c.getPartenaire().getTelephone());
                listView.put("cni", c.getPartenaire().getCni());
                listView.put("createdDate", c.getCreatedDate());
                listView.put("modifiedDate", c.getModifiedDate());
                listView.put("description", c.getDescription());
                mapList.add(listView);
            }

            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", mapList);

        } catch (Exception e) {
            return ApiResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, Message.ERREUR_LIST_VIEW + "Client", null);
        }

    }


    @Transactional
    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/caisse/search/clients/{keyword}")
    public ResponseEntity<Object> search(@PathVariable String keyword) {
        try{
            LOGGER.trace("Recherche clients...");
            List<ClientPartenaire> clientPartenaires = new ArrayList<>();
            ClientPartenaire cp;
            
            for (Partenaire p : partenaireService.findPartenaireByNom(keyword)) {
                System.out.println("ClientPartenaire0: " + p.toString());
                if (clientService.findByPartenaire(p.getPartenaireId()) != null) {
                    cp = new ClientPartenaire();
                    cp.setNom(p.getNom());
                    cp.setPrenom(p.getPrenom() == null ? "" : p.getPrenom());
                    cp.setTelephone(p.getTelephone());
                    cp.setClientId(clientService.findByPartenaire(p.getPartenaireId()).getClientId());
                    clientPartenaires.add(cp);
                }

            }
            com.catis.model.entity.Message message = msgRepo.findByCode("CL004");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, message, clientPartenaires);
        }catch(Exception e){
            e.printStackTrace();
            com.catis.model.entity.Message message = msgRepo.findByCode("CL005");
            return ApiResponseHandler.generateResponseWithAlertLevel(HttpStatus.OK, false, message, null);
        }

    }
    
    @Transactional
    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/admin/clients")
    public ResponseEntity<Object> addClient(@RequestBody ClientPOJO clientPOJO) throws ParseException {
        try {
            LOGGER.trace("Ajout d'un client...");
            Client client = new Client();
            Partenaire partenaire = new Partenaire();
            partenaire.setCni(clientPOJO.getCni());
            Date date = clientPOJO.getDateNaiss() == null ? null:
                    clientPOJO.getDateNaiss();

            partenaire.setEmail(clientPOJO.getEmail());
            partenaire.setTelephone(clientPOJO.getTelephone());
            partenaire.setNom(clientPOJO.getNom());
            partenaire.setPrenom(clientPOJO.getPrenom());
            partenaire.setPassport(clientPOJO.getPassport());
            partenaire.setDateNaiss(date);
            partenaire.setLieuDeNaiss(clientPOJO.getLieuDeNaiss());
            partenaire.setPermiDeConduire(clientPOJO.getPermiDeConduire());
            Organisation organisation = clientPOJO.getOrganisationId()==null ? null : os.findByOrganisationId(clientPOJO.getOrganisationId());
            partenaire.setOrganisation(organisation);
            client.setClientId(clientPOJO.getClientId());
            client.setPartenaire(partenaire);
            client.setOrganisation(organisation);
            client.setDescription(clientPOJO.getDescription());
            client = clientService.addCustomer(client);
            LOGGER.trace("Ajout de " + partenaire.getNom() + " réussi");
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", client);
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
    @Transactional
    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/admin/clients", params = {"page", "size"})
    public ResponseEntity<Object> getClients(@RequestParam("page") int page,
                                             @RequestParam("size") int size) {
        LOGGER.trace("liste des clients...");

        List<Client> clients = clientService.findAllCustomer(PageRequest.of(page, size, Sort.by("createdDate").descending()));
        List<ClientDTO> clientDTOS = new ArrayList<>();
        ClientDTO c;
        for(Client client : clients){
            c = new ClientDTO();
            c.setCni(client.getPartenaire().getCni());
            c.setClientId(client.getPartenaire().getClient().getClientId());
            c.setEmail(client.getPartenaire().getEmail());
            c.setTelephone(client.getPartenaire().getTelephone());
            c.setCreatedDate(client.getCreatedDate());
            c.setNom(client.getPartenaire().getNom());
            c.setPrenom(client.getPartenaire().getPrenom());
            c.setDateNaiss(client.getPartenaire().getDateNaiss());
            c.setPermiDeConduire(client.getPartenaire().getPermiDeConduire());
            c.setOrganisation(client.getOrganisation());
            c.setLieuDeNaiss(client.getPartenaire().getLieuDeNaiss());
            c.setPassport(client.getPartenaire().getPassport());
            c.setDescription(client.getDescription());
            clientDTOS.add(c);

        }
        Page<ClientDTO> pages = new PageImpl<>(clientDTOS, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate")),300);
        PagedModel<EntityModel<ClientDTO>> result = pagedResourcesAssembler
                .toModel(pages);

        return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "success", result );
    }

    @DeleteMapping("/api/v1/admin/clients/{id}")
    public ResponseEntity<Object> getClients(@PathVariable UUID id){
        try {
            clientService.deleteById(id);
            return ApiResponseHandler.generateResponse(HttpStatus.OK,
                    true, "OK", null);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.OK,
                    false, "KO", null);
        }
    }

}
