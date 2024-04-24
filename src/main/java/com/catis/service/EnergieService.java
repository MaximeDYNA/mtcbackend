package com.catis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.catis.model.entity.Energie;
import com.catis.repository.EnergieRepository;

@Service
@CacheConfig(cacheNames={"cgenergyCache"})
public class EnergieService {

    @Autowired
    private EnergieRepository energieRepo;

    @CacheEvict(allEntries = true)
    public Energie addEnergie(Energie e) {
        return energieRepo.save(e);
    }
    @Cacheable
    public Energie findEnergie(UUID energieId) {
        return energieRepo.findById(energieId).get();
    }
    @Cacheable
    public List<Energie> energieList() {
        List<Energie> energies = new ArrayList<>();
        energieRepo.findByActiveStatusTrue().forEach(energies::add);
        return energies;
    }
    @CacheEvict(key = "#id")
    public void deleteById(UUID id){
        energieRepo.deleteById(id);
    }
}
