package com.catis.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
			visite.setEncours(true);
			visite.setCarteGrise(cg);
			visite.setDateDebut(LocalDateTime.now());
			visite.setStatut(0);
	
		}
		else {
			visite.setContreVisite(false);
			visite.setCarteGrise(cg);
			visite.setDateDebut(LocalDateTime.now());
			visite.setStatut(0);
		}
			
		return visiteRepository.save(visite);
	}
	public boolean visiteEncours(String imCha) {
		return !visiteRepository.findByCarteGriseNumImmatriculationIgnoreCaseOrCarteGrise_Vehicule_ChassisIgnoreCase(imCha, imCha)
				.stream().filter(visites -> visites.getDateFin()==null).collect(Collectors.toList())
				.isEmpty();
	}
	public List<Visite> enCoursVisitList(){
		List<Visite> visiteEnCours = new ArrayList<>();
		visiteRepository.findByEncoursTrue().forEach(visiteEnCours::add);
		return visiteEnCours;
	}
	public void terminerInspection(Long visiteId) {
		Visite visite = new Visite();
		visite = visiteRepository.findById(visiteId).get();
		visite.setEncours(false);
		visite.setDateFin(LocalDateTime.now());	
		visite.setStatut(4);
		}
}
