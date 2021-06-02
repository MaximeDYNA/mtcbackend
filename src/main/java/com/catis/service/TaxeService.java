package com.catis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.catis.model.Taxe;
import com.catis.model.TaxeProduit;
import com.catis.repository.TaxeProduitRepository;
import com.catis.repository.TaxeRepository;

@Service
public class TaxeService {

    @Autowired
    private TaxeProduitRepository taxeProduitRepository;
    @Autowired
    private TaxeRepository taxeRepository;

    public List<Taxe> taxListByLibelle(String libelle) {
        List<Taxe> taxes = new ArrayList<>();
        for (TaxeProduit tp : taxeProduitRepository.findByProduit_LibelleIgnoreCase(libelle)) {
            taxes.add(tp.getTaxe());
        }
        return taxes;
    }

    public List<Taxe> getAllActiveTax() {
        List<Taxe> taxes = new ArrayList<>();
        taxeRepository.findByActiveStatusTrue().forEach(taxes::add);
        return taxes;
    }

    public Taxe findByNom(String nom) {
        return taxeRepository.findByNom(nom);
    }


}
