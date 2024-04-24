package com.catis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.entity.Vehicule;
import com.catis.objectTemporaire.VehicleSearch;
import com.catis.repository.VehiculeRepository;
import com.catis.repository.VehiculeSearchService;


// impliment caching here
@Service
public class VehiculeService {

    @Autowired
    private VehiculeRepository vehiculeRepo;

    // @Autowired
    // private VehiculeMapper vehiculeMapper;

    @Autowired
    private VehiculeSearchService vehiculesearch;

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

    // public List<Vehicule> findByChassis(String chassis) {
    public List<VehicleSearch> findByChassis(String chassis) {
        System.out.println("findByCha in vehicule index elastic search");
        List<VehicleSearch> searchresults = vehiculesearch.findByChassisStartsWithIgnoreCase(chassis);
        System.out.println("elastic results: " + searchresults.toString());
        return searchresults;
        // return vehiculeRepo.findByChassisStartsWithIgnoreCase(chassis);
    }

}
