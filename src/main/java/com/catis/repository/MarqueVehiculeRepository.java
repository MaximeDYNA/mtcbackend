package com.catis.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.MarqueVehicule;

import java.util.List;

public interface MarqueVehiculeRepository extends CrudRepository<MarqueVehicule, Long> {

    List<MarqueVehicule> findByActiveStatusTrueOrderByLibelleAsc();
    List<MarqueVehicule> findByActiveStatusTrue(Pageable pageable);
}
