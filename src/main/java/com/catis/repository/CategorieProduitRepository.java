package com.catis.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.CategorieProduit;

public interface CategorieProduitRepository extends CrudRepository<CategorieProduit, UUID> {

    List<CategorieProduit> findByLibelle(String libelle);
    List<CategorieProduit> findByActiveStatusTrue();
    List<CategorieProduit> findByActiveStatusTrue(Pageable pageable);

}
