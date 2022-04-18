package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.CategorieVehicule;

import java.util.UUID;

public interface CategorieVehiculeRepo extends CrudRepository<CategorieVehicule, UUID> {

    CategorieVehicule findByType(String type);
}
