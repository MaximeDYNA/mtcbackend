package com.catis.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@CrossOrigin
public class ClientController {
	@Autowired
	private ClientService clientService;
	@Autowired
	private PartenaireService partenaireService;
	
	@RequestMapping(method = RequestMethod.POST, value="/api/v1/clients")
	public void ajouterClient(@RequestBody ClientPartenaire clientPartenaire) {
		Client client = new Client();
		Partenaire partenaire= new Partenaire();
		partenaire.setNom(clientPartenaire.getNom());
		partenaire.setPrenom(clientPartenaire.getPrenom());
		partenaire.setDateNaiss(clientPartenaire.getDateNaiss());
		partenaire.setLieuDeNaiss(clientPartenaire.getLieuDeNaiss());
		partenaire.setPassport(clientPartenaire.getPassport());
		partenaire.setPermiDeConduire(clientPartenaire.getPermiDeConduire());
		partenaire.setCni(clientPartenaire.getCni());
		
		
		client.setDescription("Bon client");
		client.setIdorganisation("aux");
		if(partenaireService.partenaireAlreadyExist(partenaire)) {
			partenaireService.addPartenaire(partenaire);
			client.setPartenaire(partenaireService.findPartenaireByNom(clientPartenaire.getNom()).stream()
				.filter(part ->part.getPrenom().equals(clientPartenaire.getPrenom()))
				.collect(Collectors.toList()).get(0)
				);
			clientService.addCustomer(client);
		}
		
		
		
	}
	
	@RequestMapping(method = RequestMethod.GET, value="api/v1/clients")
	public List<Client> listeDesClients(){
		return clientService.findAllCustomer();
	}
	@RequestMapping(method = RequestMethod.GET, value="api/v1/clients/{id}")
	public Client getClients(@PathVariable String id){
		return clientService.findCustomerById(id);
	}
}
