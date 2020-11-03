package com.catis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.CategorieProduit;
import com.catis.repository.CategorieProduitRepository;

@Service
public class CategorieProduitService {

	@Autowired
	private CategorieProduitRepository categoProduitRepository;
	
	public List<CategorieProduit> listeCategorieProduit(){
		List<CategorieProduit> categorieProduites = new ArrayList<>();
		categoProduitRepository.findAll().forEach(categorieProduites::add);
		return categorieProduites;
	}
}
