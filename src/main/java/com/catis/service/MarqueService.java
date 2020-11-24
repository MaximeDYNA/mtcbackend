package com.catis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.MarqueVehicule;
import com.catis.repository.MarqueVehiculeRepository;

@Service
public class MarqueService {

	@Autowired
	private MarqueVehiculeRepository marqueRepo;
	
	public List<MarqueVehicule> marqueList(){
		List<MarqueVehicule> marques = new ArrayList<>();
		marqueRepo.findAll().forEach(marques::add);
		return marques;
	}
	public MarqueVehicule findById(Long id){
		
		return marqueRepo.findById(id).get();
	}
	public MarqueVehicule addMarque(MarqueVehicule marque){
		
		return marqueRepo.save(marque);
	}
}
