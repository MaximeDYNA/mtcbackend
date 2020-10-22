package com.catis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.Client;
import com.catis.repository.ClientRepository;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;
	
	public void addCustomer(Client client) {
		clientRepository.save(client);
	}
	public void editCustomer(Client client) {
		clientRepository.save(client);
	}
	public List<Client> findAllCustomer() {
		List<Client> clients = new ArrayList<>();
		clientRepository.findAll().forEach(clients::add);
		return clients;
	}
	public Client findCustomerById(long id) {
		return clientRepository.findByClientId(id);
	}
	public Client findByPartenaire(long id) {		
		return clientRepository.findByPartenaire_PartenaireId(id);
	}
}
