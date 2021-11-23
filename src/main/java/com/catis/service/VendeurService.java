package com.catis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.entity.Vendeur;
import com.catis.repository.VendeurRepository;

@Service
public class VendeurService {

    @Autowired
    private VendeurRepository vendeurRepository;


    public void addVendeur(Vendeur vendeur) {
        vendeurRepository.save(vendeur);
    }

    public void editVendeur(Vendeur vendeur) {
        vendeurRepository.save(vendeur);
    }

    public List<Vendeur> findAllVendeur() {
        List<Vendeur> vendeurs = new ArrayList<>();
        vendeurRepository.findAll().forEach(vendeurs::add);
        return vendeurs;
    }

    public Vendeur findVendeurById(UUID id) {
        return vendeurRepository.findById(id).get();
    }

    public Vendeur findVendeurByPartenaireId(UUID PartenaireId) {
        return vendeurRepository.findByPartenaire_PartenaireId(PartenaireId);
    }

}
