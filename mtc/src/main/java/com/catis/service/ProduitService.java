package com.catis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.Produit;
import com.catis.repository.ProduitRepository;

@Service
public class ProduitService {

	@Autowired
	private ProduitRepository produitRepository;

	public List<Produit> findAllProduit(){
		List<Produit> produits = new ArrayList<>();
		produitRepository.findAll().forEach(produits::add);
		return produits;
	}
	public List<Produit> findByCategorieProduit(String id){
		return produitRepository.findByCategorieProduit_CategorieProduitId(id);
	}
	
}
