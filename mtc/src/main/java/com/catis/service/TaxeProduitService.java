package com.catis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.Taxe;
import com.catis.model.TaxeProduit;
import com.catis.repository.TaxeProduitRepository;

@Service
public class TaxeProduitService {

	@Autowired
	private TaxeProduitRepository taxeProduitRepository;
	
	public List<TaxeProduit> findByProduitId(Long produitId){
		return taxeProduitRepository.findByProduit_ProduitId(produitId);
	}
	public List<TaxeProduit> findByLibelle(String reference){
		return taxeProduitRepository.findByProduit_LibelleIgnoreCase(reference);
	}
}
