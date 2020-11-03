package com.catis.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.Taxe;
import com.catis.model.TaxeProduit;

public interface TaxeProduitRepository extends CrudRepository<TaxeProduit, Long>{

	List<TaxeProduit> findByProduit_ProduitId(Long produitId);
	List<TaxeProduit> findByProduit_LibelleIgnoreCase(String libelle);
}
