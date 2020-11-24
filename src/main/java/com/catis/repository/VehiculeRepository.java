package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.Vehicule;

public interface VehiculeRepository extends CrudRepository<Vehicule, Long> {

	Vehicule findByChassis(String chassis);
}
