package com.catis.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.Produit;

public interface ProduitRepository extends CrudRepository<Produit, String> {

	List<Produit> findByCategorieProduit_CategorieProduitId(String id);
}
