package com.catis.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Caisse;


import java.util.List;
import java.util.UUID;

public interface CaisseRepository extends CrudRepository<Caisse, UUID> {
    List<Caisse> findByActiveStatusTrue(Pageable pageable);
    List<Caisse> findByActiveStatusTrue();

}
