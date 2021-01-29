package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.Taxe;

public interface TaxeRepository extends CrudRepository<Taxe, Long> {

    Taxe findByNom(String nom);
}
