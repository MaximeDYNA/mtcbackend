package com.catis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.catis.dtoprojections.VehiculeDTO;
import com.catis.model.entity.Vehicule;
import com.catis.objectTemporaire.VehicleSearch;
import com.catis.repository.VehiculeRepository;
import com.catis.repository.VehiculeSearchService;


@Service
public class VehiculeService {

    @Autowired
    private VehiculeRepository vehiculeRepo;


    @Autowired
    private VehiculeSearchService vehiculesearch;

    public List<Vehicule> vehiculeList() {
        List<Vehicule> vehicules = new ArrayList<>();
        vehiculeRepo.findByActiveStatusTrue().forEach(vehicules::add);
        return vehicules;
    }
    // flemming implimented 
    public List<Vehicule> vehiculeListSearch(String nom, Pageable pageable) {
        List<Vehicule> vehicules = new ArrayList<>();
        vehiculeRepo.findByActiveStatusTrueAndChassisStartsWithIgnoreCase(nom, pageable).forEach(vehicules::add);
        return vehicules;
    }
    // flemming implimented
    public Page<Vehicule> vehiculeListSearchPage(String nom, Pageable pageable) {
        return vehiculeRepo.findByChassisContaining(nom, pageable);
    }

    // flemming implimented
    public List<VehiculeDTO> getVehicules(String chassis, Pageable pageable) {
        List<VehiculeDTO> vehicules = vehiculeRepo.findByActiveStatusTrueAndChassisStartsWithIgnoreCaseDTO(chassis, pageable);
        vehicules.forEach(v -> {
            System.out.println("ID: " + v.getVehiculeId() + ", Chassis: " + v.getChassis() + ", Organisation: " + v.getOrganisationNom());
        });
        return vehicules;
    }

    public List<Vehicule> vehiculeListPage(Pageable pageable) {
        List<Vehicule> vehicules = new ArrayList<>();
        vehiculeRepo.findByActiveStatusTrue(pageable).forEach(vehicules::add);
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
