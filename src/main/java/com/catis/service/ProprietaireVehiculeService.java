package com.catis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.entity.Client;
import com.catis.model.entity.Contact;
import com.catis.model.entity.Partenaire;
import com.catis.model.entity.ProprietaireVehicule;
import com.catis.repository.ProprietaireVehiculeRepository;

@Service
public class ProprietaireVehiculeService {

    @Autowired
    private ProprietaireVehiculeRepository pvr;
    @Autowired
    private PartenaireService partenaireService;


    public List<ProprietaireVehicule> findAll() {
        List<ProprietaireVehicule> proprietaires = new ArrayList<>();
        pvr.findByActiveStatusTrue().forEach(proprietaires::add);
        return proprietaires;
    }

    public List<ProprietaireVehicule> searchProprio(String nom) {
        List<ProprietaireVehicule> proprietaires = new ArrayList<>();
        pvr.findByActiveStatusTrueAndPartenaire_NomStartsWithIgnoreCase(nom).forEach(proprietaires::add);
        return proprietaires;
    }

    public ProprietaireVehicule addProprietaire(ProprietaireVehicule p) {
        return pvr.save(p);
    }

    public ProprietaireVehicule findById(UUID id) {
        return pvr.findById(id).get();
    }

    public void deleteById(UUID id){
        pvr.deleteById(id);
    }

    public ProprietaireVehicule addClientToProprietaire(Client client) {
        Partenaire p = partenaireService.findPartenaireById(client.getPartenaire().getPartenaireId());
        if (pvr.findByPartenaire_PartenaireId(p.getPartenaireId()).isEmpty()) {
            ProprietaireVehicule pro = new ProprietaireVehicule();
            pro.setPartenaire(p);
            return pvr.save(pro);
        }
        return pvr.findByPartenaire_PartenaireId(p.getPartenaireId()).get(0);

    }

    public ProprietaireVehicule addContactToProprietaire(Contact contact) {
        Partenaire p = partenaireService.findPartenaireById(contact.getPartenaire().getPartenaireId());
        if (pvr.findByPartenaire_PartenaireId(p.getPartenaireId()).isEmpty()) {
            ProprietaireVehicule pro = new ProprietaireVehicule();
            pro.setPartenaire(p);
            return pvr.save(pro);
        }
        return pvr.findByPartenaire_PartenaireId(p.getPartenaireId()).get(0);

    }
}
