package com.catis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.CategorieProduit;
import com.catis.repository.CategorieProduitRepository;

@Service
public class CategorieProduitService {

    @Autowired
    private CategorieProduitRepository categoProduitRepository;

    public List<CategorieProduit> listeCategorieProduit() {
        List<CategorieProduit> categorieProduites = new ArrayList<>();
        categoProduitRepository.findByActiveStatusTrue().forEach(categorieProduites::add);
        return categorieProduites;
    }


    public CategorieProduit addCategorieProduit(CategorieProduit cp) {
        cp.setActiveStatus(true);
        return categoProduitRepository.save(cp);
    }

    public void deleteCategorieProduit(Long id) {
        categoProduitRepository.deleteById(id);
    }

    public List<CategorieProduit> findByLibelle(String libelle) {
        return categoProduitRepository.findByLibelle(libelle);

    }

    public CategorieProduit findById(Long id) {
        return categoProduitRepository.findById(id).get();

    }

}
