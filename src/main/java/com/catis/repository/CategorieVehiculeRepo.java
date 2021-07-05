package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.CategorieVehicule;

public interface CategorieVehiculeRepo extends CrudRepository<CategorieVehicule, Long> {

    CategorieVehicule findByType(String type);
}
