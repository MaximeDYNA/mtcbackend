package com.catis.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.catis.Controller.exception.VisiteEnCoursException;
import com.catis.model.CarteGrise;
import com.catis.model.Control;
import com.catis.model.Control.StatusType;
import com.catis.model.Visite;
import com.catis.repository.ControlRepository;
import com.catis.repository.VisiteRepository;

@Service

public class VisiteService {
	@Autowired
	private VisiteRepository visiteRepository;
	
	@Autowired
	private ControlRepository controlRepository;
	
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
	
	public Visite ajouterVisite(CarteGrise cg, double montantTotal, double montantEncaisse) throws VisiteEnCoursException {
		Visite visite = new Visite();
		if(montantEncaisse < montantTotal) {
			visite.setStatut(9);
		}
		else
			visite.setStatut(0);
		
		if(isVisiteInitial(cg.getNumImmatriculation())){
			visite.setContreVisite(false);
			visite.setEncours(true);
			visite.setCarteGrise(cg);
			visite.setDateDebut(LocalDateTime.now());
			Control control = new Control();
			List<Visite> visites = new ArrayList<>();
			visites.add(visite);
			control.setCarteGrise(cg);
			control.setStatus(StatusType.INITIALIZED);
			control.setVisites(visites);
			visite.setControl(control);
	
		}else {
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
	public boolean isVisiteInitial(String ref) throws VisiteEnCoursException {
		List<Visite> visites = findByReference(ref);
		Visite visite = visites.stream().max(Comparator.comparing(Visite::getCreatedDate))
										.orElse(null);
		if(visite != null) {
			if(visite.getControl().getActiveStatus().equals("INITIALIZED")) {
				throw new VisiteEnCoursException();
			}
			if(visite.getControl().getActiveStatus().equals("VALIDATED")) {
				return true;
			}
			if(visite.getControl().getActiveStatus().equals("REJECTED")) {
				LocalDateTime now = LocalDateTime.now();
				if(visite.getControl().getContreVDelayAt().isAfter(now)) {
					return false;
				}
				if(visite.getControl().getContreVDelayAt().isBefore(now) || visite.getControl().getContreVDelayAt().equals(now)) {
					return true;
				}
			}
		}
		return true;
	}
	public boolean isVehiculeExist(String ref) {
		if(findByReference(ref).isEmpty())
			return false;
		return true;
	}
}
