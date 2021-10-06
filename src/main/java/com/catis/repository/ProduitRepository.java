package com.catis.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Produit;

public interface ProduitRepository extends CrudRepository<Produit, Long> {

    List<Produit> findByCategorieProduit_CategorieProduitId(Long id);

    List<Produit> findByActiveStatusTrue();
    List<Produit> findByActiveStatusTrue(Pageable pageable);

    Produit findByLibelleIgnoreCase(String libelle);
}
