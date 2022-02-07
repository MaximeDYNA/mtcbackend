package com.catis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.entity.Ligne;
import com.catis.repository.LigneRepository;

@Service
public class LigneService {

    @Autowired
    private LigneRepository ligneR;


    public Ligne addLigne(Ligne ligne) {
        return ligneR.save(ligne);
    }

    public List<Ligne> findAllLigne() {
        List<Ligne> lignes = new ArrayList<>();
        ligneR.findByActiveStatusTrue().forEach(lignes::add);
        return lignes;
    }
    public List<Ligne> findActiveByorganisation(Long orgId) {
        List<Ligne> lignes = new ArrayList<>();
        ligneR.findByActiveStatusTrueAndOrganisation_OrganisationId(orgId).forEach(lignes::add);
        return lignes;
    }

    public Ligne findLigneById(UUID id) {

        return ligneR.findById(id).get();
    }

    public void deleteById(UUID id){
        ligneR.deleteById(id);
    }

}
