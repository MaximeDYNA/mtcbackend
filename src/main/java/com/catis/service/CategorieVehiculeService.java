package com.catis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.CategorieVehicule;
import com.catis.repository.CategorieVehiculeRepo;

@Service
public class CategorieVehiculeService {

    @Autowired
    private CategorieVehiculeRepo categorieVehiculeRepo;


    public CategorieVehicule findByType(String type) {
        return categorieVehiculeRepo.findByType(type);
    }

    public CategorieVehicule findById(Long id) {
        return categorieVehiculeRepo.findById(id).get();
    }
}
