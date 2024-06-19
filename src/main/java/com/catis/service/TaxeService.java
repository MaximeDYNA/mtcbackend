package com.catis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.catis.model.entity.Taxe;
import com.catis.model.entity.TaxeProduit;
import com.catis.repository.TaxeProduitRepository;
import com.catis.repository.TaxeRepository;

@CacheConfig(cacheNames={"taxCache"})
@Service
public class TaxeService {

    @Autowired
    private TaxeProduitRepository taxeProduitRepository;
    @Autowired
    private TaxeRepository taxeRepository;

    @Cacheable
    public List<Taxe> taxListByLibelle(String libelle) {
        List<Taxe> taxes = new ArrayList<>();
        for (TaxeProduit tp : taxeProduitRepository.findByProduit_LibelleIgnoreCase(libelle)) {
            taxes.add(tp.getTaxe());
        }
        return taxes;
    }

    @Cacheable
    public List<Taxe> getAllActiveTax() {
        List<Taxe> taxes = new ArrayList<>();
        taxeRepository.findByActiveStatusTrue().forEach(taxes::add);
        return taxes;
    }
    @Cacheable
    public List<Taxe> getAllActiveTax(Pageable pageable) {
        List<Taxe> taxes = new ArrayList<>();
        taxeRepository.findByActiveStatusTrue(pageable).forEach(taxes::add);
        return taxes;
    }

    @CacheEvict(allEntries = true)
    public Taxe save(Taxe taxe) {
        taxe = taxeRepository.save(taxe);
        return taxe;
    }
    @CacheEvict(allEntries = true)
    public void deleteById(UUID id) {
        taxeRepository.deleteById(id);
    }
    @Cacheable
    public Taxe findByNom(String nom) {
        return taxeRepository.findByNom(nom);
    }


}
