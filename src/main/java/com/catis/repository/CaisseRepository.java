package com.catis.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Caisse;


import java.util.List;

public interface CaisseRepository extends CrudRepository<Caisse, Long> {
    List<Caisse> findByActiveStatusTrue(Pageable pageable);
    List<Caisse> findByActiveStatusTrue();

}
