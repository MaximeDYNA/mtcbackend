package com.catis.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.CarteGrise;
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
	public boolean viensPourContreVisite(String imCha) {
		return	!visiteRepository.findByContreVisiteFalseAndCarteGriseNumImmatriculationIgnoreCaseOrCarteGrise_Vehicule_ChassisIgnoreCase(imCha, imCha)
				.stream()
				.filter(visites -> Duration.between(visites.getDateFin(), LocalDateTime.now() ).toDays() <= 15)
				.collect(Collectors.toList()).isEmpty();
		 
	}
	public Visite ajouterVisite(CarteGrise cg) {
		Visite visite = new Visite();
		if(viensPourContreVisite(cg.getNumImmatriculation())) {
			visite.setContreVisite(true);
			visite.setCarteGrise(cg);
			visite.setDateDebut(LocalDateTime.now());
			visite.setStatut("à mettre à jour");
	
		}
		else {
			visite.setContreVisite(false);
			visite.setCarteGrise(cg);
			visite.setDateDebut(LocalDateTime.now());
			visite.setStatut("à mettre à jour");
		}
			
		return visiteRepository.save(visite);
	}
	public boolean visiteEncours(String imCha) {
		return !visiteRepository.findByCarteGriseNumImmatriculationIgnoreCaseOrCarteGrise_Vehicule_ChassisIgnoreCase(imCha, imCha)
				.stream().filter(visites -> visites.getDateFin()==null).collect(Collectors.toList())
				.isEmpty();
	}
}
