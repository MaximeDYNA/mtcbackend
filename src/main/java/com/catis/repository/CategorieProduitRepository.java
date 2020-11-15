package com.catis.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.CategorieProduit;

public interface CategorieProduitRepository extends CrudRepository<CategorieProduit, String> {
	
	List<CategorieProduit> findByLibelle(String libelle);
	
}
