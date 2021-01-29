package com.catis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.Ligne;
import com.catis.repository.InspectionRepository;
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
        ligneR.findAll().forEach(lignes::add);
        return lignes;
    }

    public Ligne findLigneById(Long id) {

        return ligneR.findById(id).get();
    }

}
