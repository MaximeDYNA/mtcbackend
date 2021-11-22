package com.catis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.entity.Energie;
import com.catis.repository.EnergieRepository;

@Service
public class EnergieService {

    @Autowired
    private EnergieRepository energieRepo;

    public Energie addEnergie(Energie e) {
        return energieRepo.save(e);
    }

    public Energie findEnergie(UUID energieId) {
        return energieRepo.findById(energieId).get();
    }

    public List<Energie> energieList() {
        List<Energie> energies = new ArrayList<>();
        energieRepo.findByActiveStatusTrue().forEach(energies::add);
        return energies;
    }
    public void deleteById(UUID id){
        energieRepo.deleteById(id);
    }
}
