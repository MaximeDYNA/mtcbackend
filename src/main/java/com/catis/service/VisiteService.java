package com.catis.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;

import com.catis.Controller.ApiResponseHandler;
import com.catis.Controller.VisiteController;
import com.catis.Controller.exception.VisiteEnCoursException;
import com.catis.model.CarteGrise;
import com.catis.model.Control;
import com.catis.model.Control.StatusType;
import com.catis.model.Visite;
import com.catis.objectTemporaire.Listview;
import com.catis.repository.ControlRepository;
import com.catis.repository.VisiteRepository;

import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;

@Service

public class VisiteService {
	@Autowired
	private VisiteRepository visiteRepository;
	@Autowired
	private ProduitService ps;
	@Autowired
	private VenteService venteService;
	
	private final FluxSink<Visite> sink;
	private final FluxProcessor<Visite, Visite> processor;
	
	private static Logger log  = LoggerFactory.getLogger(VisiteController.class);
	
	
	public VisiteService() {
		processor = DirectProcessor.<Visite>create().serialize();
		sink = processor.sink();
		
	}
	public Flux<ServerSentEvent<ResponseEntity<Object>>> refreshVisiteAfterAdd(){

		log.info("Liste des visites en cours");
		List<Listview> listVisit = new ArrayList<>();
		for(Visite visite: enCoursVisitList()) {
			Listview lv = new Listview();
			lv.setCategorie(ps.findByImmatriculation(visite.getCarteGrise()
					.getNumImmatriculation()));
			
			if (venteService.findByVisite(visite.getIdVisite())
					 == null)
				lv.setClient(null);
			else
			lv.setClient(venteService.findByVisite(visite.getIdVisite())
					.getClient()
					.getPartenaire()
					.getNom());
			lv.setDate(visite.getDateDebut());
			lv.setReference(visite.getCarteGrise().getNumImmatriculation());
			lv.setStatut(visite.statutRender(visite.getStatut()));
			lv.setType(visite.typeRender());
			listVisit.add(lv);
			lv.setId(visite.getIdVisite());
			
		}
		//return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Affichage en mode liste des visites", listVisit);
		ResponseEntity<Object> o = ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Affichage en mode liste des visites", listVisit);
		return processor
				.map(sequence -> ServerSentEvent.<ResponseEntity<Object>> builder()
				          .event("new_visit")
				          .data( o)
				          .build());
				
	}
	public Flux<ServerSentEvent<ResponseEntity<Object>>> refreshVisiteAfterEdit(Visite visite){
		ResponseEntity<Object> o = ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Affichage en mode liste des visites", visite);
		return processor
				.map(sequence -> ServerSentEvent.<ResponseEntity<Object>> builder()
				          .event("edit_visit")
				          .data( o)
				          .build());		
	}
	
	@Autowired
	private ControlRepository controlRepository;
	
	public List<Visite> findAll(){
		List<Visite> visites = new ArrayList<Visite>();
		visiteRepository.findAll().forEach(visites::add);
		return visites;
	}
	public Visite approuver(Visite visite){
		visite.setStatut(0);
		Visite v =visiteRepository.save(visite);
		refreshVisiteAfterEdit(v);
		return v;
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
		Visite v =visiteRepository.save(visite);
		refreshVisiteAfterAdd();
		return v;
	}
	public Visite modifierVisite(Visite visite) {
		Visite v = visiteRepository.save(visite);
		refreshVisiteAfterEdit(v);
		return v;
	}
	public boolean visiteEncours(String imCha) {
		return !visiteRepository.findByCarteGriseNumImmatriculationIgnoreCaseOrCarteGrise_Vehicule_ChassisIgnoreCase(imCha, imCha)
				.stream().filter(visites -> visites.getDateFin()==null).collect(Collectors.toList())
				.isEmpty();
	}
	public List<Visite> enCoursVisitList(){
		List<Visite> visiteEnCours = new ArrayList<>();
		visiteRepository.findByEncoursTrueOrderByCreatedDateDesc().forEach(visiteEnCours::add);
		return visiteEnCours;
	}
	public void terminerInspection(Long visiteId) {
		Visite visite = new Visite();
		visite = visiteRepository.findById(visiteId).get();
		visite.setEncours(false);
		visite.setDateFin(LocalDateTime.now());	
		visite.setStatut(4);
		visite = visiteRepository.save(visite);
		refreshVisiteAfterEdit(visite);
		
	}
	public List<Visite> listParStatus(int status){
		return visiteRepository.findByEncoursTrueAndStatut(status, Sort.by(Sort.Direction.DESC, "dateDebut"));
	}
	
	public void commencerInspection(Long visiteId) {
		Visite visite = new Visite();
		visite = visiteRepository.findById(visiteId).get();
		visite.setDateFin(LocalDateTime.now());	
		visite.setStatut(2);
		visite = visiteRepository.save(visite);
		refreshVisiteAfterEdit(visite);
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
