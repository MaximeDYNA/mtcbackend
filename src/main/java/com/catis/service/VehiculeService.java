package com.catis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.entity.Vehicule;
import com.catis.repository.VehiculeRepository;

@Service
public class VehiculeService {

    @Autowired
    private VehiculeRepository vehiculeRepo;

    public List<Vehicule> vehiculeList() {
        List<Vehicule> vehicules = new ArrayList<>();
        vehiculeRepo.findByActiveStatusTrue().forEach(vehicules::add);
        return vehicules;
    }

    public Vehicule addVehicule(Vehicule vehicule) {
        return vehiculeRepo.save(vehicule);
    }


    public void deleteById(UUID id) {
         vehiculeRepo.deleteById(id);
    }



    public Vehicule findById(UUID id) {
        return vehiculeRepo.findById(id).get();
    }

    public List<Vehicule> findByChassis(String chassis) {
        return vehiculeRepo.findByChassisStartsWithIgnoreCase(chassis);
    }

}
