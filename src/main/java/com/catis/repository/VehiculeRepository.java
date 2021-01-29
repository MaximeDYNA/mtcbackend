package com.catis.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.Vehicule;

public interface VehiculeRepository extends CrudRepository<Vehicule, Long> {

    List<Vehicule> findByChassisStartsWithIgnoreCase(String chassis);
}
