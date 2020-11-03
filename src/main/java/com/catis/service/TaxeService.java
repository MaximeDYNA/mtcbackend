package com.catis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.catis.model.Taxe;
import com.catis.model.TaxeProduit;
import com.catis.repository.TaxeProduitRepository;

@Service
public class TaxeService {

	@Autowired
	private TaxeProduitRepository taxeProduitRepository;
	
	public List<Taxe> taxListByLibelle(String libelle){
		List<Taxe> taxes = new ArrayList<>();
		
		for(TaxeProduit tp : taxeProduitRepository.findByProduit_LibelleIgnoreCase(libelle)) {
			taxes.add(tp.getTaxe());
		}
		
		return taxes;
	}
	
}
