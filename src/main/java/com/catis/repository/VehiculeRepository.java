package com.catis.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Vehicule;

public interface VehiculeRepository extends CrudRepository<Vehicule, UUID> {

    List<Vehicule> findByChassisStartsWithIgnoreCase(String chassis);
    List<Vehicule> findByActiveStatusTrue();
}
