package com.catis.repository.nativeQueries;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.catis.model.entity.Vehicule;

public interface VehiculeCustomRepository {
    Page<Vehicule> findByChassisContaining(String chassis, Pageable pageable);
}
