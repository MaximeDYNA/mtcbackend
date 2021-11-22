package com.catis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.catis.model.entity.Client;
import com.catis.repository.ClientRepository;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Client addCustomer(Client client) {
        return clientRepository.save(client);
    }

    public void editCustomer(Client client) {
        clientRepository.save(client);
    }

    public List<Client> findAllCustomer() {
        List<Client> clients = new ArrayList<>();
        clientRepository.findByActiveStatusTrue().forEach(clients::add);
        return clients;
    }
    public List<Client> findAllCustomer(Pageable pageable) {
        List<Client> clients = new ArrayList<>();
        clientRepository.findByActiveStatusTrue(pageable).forEach(clients::add);
        return clients;
    }

    public void deleteById(UUID id){
        clientRepository.deleteById(id);
    }

    public Client findCustomerById(UUID id) {
        return clientRepository.findByClientId(id);
    }

    public Client findByPartenaire(UUID id) {
        return clientRepository.findByPartenaire_PartenaireId(id);
    }
}
