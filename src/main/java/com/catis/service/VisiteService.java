package com.catis.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityListeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Service;

import com.catis.model.CarteGrise;
import com.catis.model.Visite;

import com.catis.repository.VisiteRepository;

@Service

public class VisiteService {
	@Autowired
	private VisiteRepository visiteRepository;
	
	public List<Visite> findAll(){
		List<Visite> visites = new ArrayList<Visite>();
		visiteRepository.findAll().forEach(visites::add);
		return visites;
	}
	public Visite approuver(Visite visite){
		visite.setStatut(0);
		visiteRepository.save(visite);
		return visiteRepository.save(visite);
	}
	
	public List<Visite> findByReference(String ref){
		return visiteRepository.findByCarteGriseNumImmatriculationIgnoreCaseOrCarteGrise_Vehicule_ChassisIgnoreCase(ref, ref);
	}
	public Visite findById(Long i) {
		return visiteRepository.findById(i).get();
	}
	public boolean viensPourContreVisite(String imCha) {
		try {
			return!visiteRepository.findByContreVisiteFalseAndCarteGriseNumImmatriculationIgnoreCaseOrCarteGrise_Vehicule_ChassisIgnoreCase(imCha, imCha)
				.stream()
				.filter(visites -> Duration.between(visites.getDateFin(), LocalDateTime.now() ).toDays() <= 15)
				.collect(Collectors.toList()).isEmpty();
		} catch (Exception e) {
			return false;
		}
	}
	
	public Visite ajouterVisite(CarteGrise cg, double montantTotal, double montantEncaisse) {
		Visite visite = new Visite();
		if(montantEncaisse < montantTotal) {
			visite.setStatut(9);
		}
		else
			visite.setStatut(0);
		
		if(viensPourContreVisite(cg.getNumImmatriculation())) {
			visite.setContreVisite(true);
			visite.setEncours(true);
			visite.setCarteGrise(cg);
			visite.setDateDebut(LocalDateTime.now());
			
	
		}
		else {
			visite.setContreVisite(false);
			visite.setEncours(true);
			visite.setCarteGrise(cg);
			visite.setDateDebut(LocalDateTime.now());
			
		}
			if(cg.getProduit().getProduitId()==1) {
				return visite;
			}
		return visiteRepository.save(visite);
	}
	public Visite modifierVisite(Visite visite) {
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
	public List<Visite> listParStatus(int status){
		return visiteRepository.findByEncoursTrueAndStatut(status, Sort.by(Sort.Direction.DESC, "dateDebut"));
	}
	public void commencerInspection(Long visiteId) {
		Visite visite = new Visite();
		visite = visiteRepository.findById(visiteId).get();
		visite.setDateFin(LocalDateTime.now());	
		visite.setStatut(2);
		visiteRepository.save(visite);
	}
	
}
