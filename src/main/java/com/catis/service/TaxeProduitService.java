package com.catis.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.entity.TaxeProduit;
import com.catis.repository.TaxeProduitRepository;

@Service
public class TaxeProduitService {

    @Autowired
    private TaxeProduitRepository taxeProduitRepository;

    public List<TaxeProduit> findByProduitId(UUID produitId) {
        return taxeProduitRepository.findByProduit_ProduitId(produitId);
    }

    public List<TaxeProduit> findByLibelle(String reference) {
        return taxeProduitRepository.findByProduit_LibelleIgnoreCase(reference);
    }

    public void deleteBytaxeId(UUID taxeId) {
         taxeProduitRepository.deleteById(taxeId);
    }

    public Optional<TaxeProduit> findBytaxeIdAndProduitId(UUID taxeId, UUID produitId) {
        return taxeProduitRepository.findByTaxe_taxeIdAndProduit_produitId(taxeId, produitId).stream().findFirst();
    }
}
