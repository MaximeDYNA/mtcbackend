package com.catis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.entity.MarqueVehicule;
import com.catis.repository.MarqueVehiculeRepository;

@Service
public class MarqueService {

    @Autowired
    private MarqueVehiculeRepository marqueRepo;

    public List<MarqueVehicule> marqueList() {
        List<MarqueVehicule> marques = new ArrayList<>();
        marqueRepo.findByActiveStatusTrue().forEach(marques::add);
        return marques;
    }

    public MarqueVehicule findById(Long id) {

        return marqueRepo.findById(id).get();
    }

    public MarqueVehicule addMarque(MarqueVehicule marque) {
        MarqueVehicule marqueVehicule = marqueRepo.save(marque);
        return marqueVehicule;
    }

    public void deleteById(Long id){
        marqueRepo.deleteById(id);
    }


}
