package com.catis.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.CategorieProduit;

public interface CategorieProduitRepository extends CrudRepository<CategorieProduit, Long> {

    List<CategorieProduit> findByLibelle(String libelle);
    List<CategorieProduit> findByActiveStatusTrue();

}
