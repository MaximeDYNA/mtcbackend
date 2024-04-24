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

import com.catis.model.entity.MarqueVehicule;
import com.catis.repository.MarqueVehiculeRepository;

@Service
@CacheConfig(cacheNames={"cgbrandCache"})
public class MarqueService {

    @Autowired
    private MarqueVehiculeRepository marqueRepo;

    @Cacheable
    public List<MarqueVehicule> marqueList() {
        List<MarqueVehicule> marques = new ArrayList<>();
        marqueRepo.findByActiveStatusTrueOrderByLibelleAsc().forEach(marques::add);
        return marques;
    }
    @Cacheable
    public List<MarqueVehicule> marqueList(Pageable pageable) {
        List<MarqueVehicule> marques = new ArrayList<>();
        marqueRepo.findByActiveStatusTrue(pageable).forEach(marques::add);
        return marques;
    }
    @Cacheable(key = "#id")
    public MarqueVehicule findById(UUID id) {

        return marqueRepo.findById(id).get();
    }
    @CacheEvict(allEntries = true)
    public MarqueVehicule addMarque(MarqueVehicule marque) {
        MarqueVehicule marqueVehicule = marqueRepo.save(marque);
        return marqueVehicule;
    }
    @CacheEvict(key = "#id")
    public void deleteById(UUID id){
        marqueRepo.deleteById(id);
    }
}
