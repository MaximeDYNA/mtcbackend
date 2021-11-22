package com.catis.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Taxe;

import java.util.List;
import java.util.UUID;

public interface TaxeRepository extends CrudRepository<Taxe, UUID> {

    Taxe findByNom(String nom);
    List<Taxe> findByActiveStatusTrue();
    List<Taxe> findByActiveStatusTrue(Pageable pageable);
}
