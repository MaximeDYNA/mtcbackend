package com.catis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.catis.model.entity.Taxe;
import com.catis.model.entity.TaxeProduit;
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

    public List<Taxe> getAllActiveTax(Pageable pageable) {
        List<Taxe> taxes = new ArrayList<>();
        taxeRepository.findByActiveStatusTrue(pageable).forEach(taxes::add);
        return taxes;
    }

    public Taxe save(Taxe taxe) {
        taxe = taxeRepository.save(taxe);
        return taxe;
    }

    public void deleteById(Long id) {
        taxeRepository.deleteById(id);
    }
    public Taxe findByNom(String nom) {
        return taxeRepository.findByNom(nom);
    }


}
