package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.Taxe;

import java.util.List;

public interface TaxeRepository extends CrudRepository<Taxe, Long> {

    Taxe findByNom(String nom);
    List<Taxe> findByActiveStatusTrue();
}
