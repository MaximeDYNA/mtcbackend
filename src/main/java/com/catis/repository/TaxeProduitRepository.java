package com.catis.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.TaxeProduit;

public interface TaxeProduitRepository extends CrudRepository<TaxeProduit, UUID> {

    List<TaxeProduit> findByProduit_ProduitId(UUID produitId);

    List<TaxeProduit> findByProduit_LibelleIgnoreCase(String libelle);

    List<TaxeProduit> findByTaxe_taxeIdAndProduit_produitId(UUID taxeId, UUID produitId);

    void deleteByTaxe_taxeId(UUID taxeId);

}
