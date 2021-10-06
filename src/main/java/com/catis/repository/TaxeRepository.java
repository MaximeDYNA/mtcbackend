package com.catis.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Taxe;

import java.util.List;

public interface TaxeRepository extends CrudRepository<Taxe, Long> {

    Taxe findByNom(String nom);
    List<Taxe> findByActiveStatusTrue();
    List<Taxe> findByActiveStatusTrue(Pageable pageable);
}
