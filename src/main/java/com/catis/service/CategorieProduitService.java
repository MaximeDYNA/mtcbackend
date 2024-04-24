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

import com.catis.model.entity.CategorieProduit;
import com.catis.repository.CategorieProduitRepository;

@Service
@CacheConfig(cacheNames={"catproductsCache"})
public class CategorieProduitService {

    @Autowired
    private CategorieProduitRepository categoProduitRepository;

    @Cacheable(key = "'catproductlist'")
    public List<CategorieProduit> listeCategorieProduit() {
        List<CategorieProduit> categorieProduites = new ArrayList<>();
        categoProduitRepository.findByActiveStatusTrue().forEach(categorieProduites::add);
        return categorieProduites;
    }
    
    @Cacheable
    public List<CategorieProduit> listeCategorieProduit(Pageable pageable) {
        List<CategorieProduit> categorieProduites = new ArrayList<>();
        categoProduitRepository.findByActiveStatusTrue(pageable).forEach(categorieProduites::add);
        return categorieProduites;
    }

    @CacheEvict(allEntries = true)
    public CategorieProduit addCategorieProduit(CategorieProduit cp) {
        cp.setActiveStatus(true);
        return categoProduitRepository.save(cp);
    }

    @CacheEvict(key = "#id")
    public void deleteCategorieProduit(UUID id) {
        categoProduitRepository.deleteById(id);
    }
    
    @Cacheable(key = "#libelle")
    public List<CategorieProduit> findByLibelle(String libelle) {
        return categoProduitRepository.findByLibelle(libelle);

    }
    
    @Cacheable(key = "#id")
    public CategorieProduit findById(UUID id) {
        return categoProduitRepository.findById(id).get();

    }

}
