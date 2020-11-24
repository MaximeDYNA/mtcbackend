package com.catis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.ProprietaireVehicule;
import com.catis.repository.ProprietaireVehiculeRepository;

@Service
public class ProprietaireVehiculeService {

	@Autowired
	private ProprietaireVehiculeRepository pvr;	

	public List<ProprietaireVehicule> findAll(){
		List<ProprietaireVehicule> proprietaires = new ArrayList<>();
		pvr.findAll().forEach(proprietaires::add);
		return proprietaires;
	}
	public ProprietaireVehicule addProprietaire(ProprietaireVehicule p){
		return pvr.save(p);
	}
	public ProprietaireVehicule findById(Long id){
		return pvr.findById(id).get();
	}
}
