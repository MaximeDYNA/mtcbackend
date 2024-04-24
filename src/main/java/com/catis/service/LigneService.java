package com.catis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.catis.model.entity.Ligne;
import com.catis.repository.LigneRepository;

@Service
@CacheConfig(cacheNames={"lignesCache"})
public class LigneService {

    @Autowired
    private LigneRepository ligneR;

    @CacheEvict(allEntries = true)
    public Ligne addLigne(Ligne ligne) {
        return ligneR.save(ligne);
    }

    @Cacheable
    public List<Ligne> findAllLigne() {
        List<Ligne> lignes = new ArrayList<>();
        ligneR.findByActiveStatusTrue().forEach(lignes::add);
        return lignes;
    }
    @Cacheable
    public List<Ligne> findActiveByorganisation(UUID orgId) {
        List<Ligne> lignes = new ArrayList<>();
        ligneR.findByActiveStatusTrueAndOrganisation_OrganisationId(orgId).forEach(lignes::add);
        return lignes;
    }

    @Cacheable(key = "#id")
    public Ligne findLigneById(UUID id) {

        return ligneR.findById(id).get();
    }
    @CacheEvict(key = "#id")
    public void deleteById(UUID id){
        ligneR.deleteById(id);
    }

}
