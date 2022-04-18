package com.catis.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.MarqueVehicule;

import java.util.List;
import java.util.UUID;

public interface MarqueVehiculeRepository extends CrudRepository<MarqueVehicule, UUID> {

    List<MarqueVehicule> findByActiveStatusTrueOrderByLibelleAsc();
    List<MarqueVehicule> findByActiveStatusTrue(Pageable pageable);
}
