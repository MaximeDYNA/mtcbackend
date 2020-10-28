package com.catis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.catis.model.Visite;

import com.catis.repository.VisiteRepository;

@Service
public class VisiteService {
	@Autowired
	private VisiteRepository visiteRepository;
	
	public List<Visite> findByReference(String ref){
		return visiteRepository.findByCarteGriseNumImmatriculationIgnoreCaseOrCarteGrise_Vehicule_ChassisIgnoreCase(ref, ref);
	}
	public Visite findById(Long i) {
		return visiteRepository.findById(i).get();
	}
	
}
