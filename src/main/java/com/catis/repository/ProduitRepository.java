package com.catis.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Produit;

public interface ProduitRepository extends CrudRepository<Produit, UUID> {

    List<Produit> findByCategorieProduit_CategorieProduitId(UUID id);

    List<Produit> findByActiveStatusTrue();
    List<Produit> findByActiveStatusTrue(Pageable pageable);

    Produit findByLibelleIgnoreCase(String libelle);
}
