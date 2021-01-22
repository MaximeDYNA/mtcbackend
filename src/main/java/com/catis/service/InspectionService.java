package com.catis.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.Inspection;
import com.catis.model.Visite;
import com.catis.repository.InspectionRepository;

@Service	
public class InspectionService {

	@Autowired
	private InspectionRepository inspectionR;
	
	@Autowired
	private VisiteService visiteService;
	
	public Inspection addInspection(Inspection inspection) {
		return inspectionR.save(inspection);
	}

	public List<Inspection> findAllInspection() {
		List<Inspection> inspections = new ArrayList<>();
		inspectionR.findAll().forEach(inspections::add);
		return inspections;
	}
	public Inspection findInspectionById(Long id) {
	
		return inspectionR.findById(id).get();
	}
	public Inspection findInspectionByVisite(Long idvisite) {
		return inspectionR.findByVisite_IdVisite(idvisite);
	}
	public Inspection findLastByRef(String ref) {
		List<Inspection> inspections = inspectionR.findByVisite_CarteGrise_numImmatriculationOrVisite_CarteGrise_Vehicule_Chassis(ref, ref);
		Inspection inspection = inspections.stream().max(Comparator.comparing(Inspection::getCreatedDate))
				.orElse(null);
		return inspection;
	}
	public Inspection setSignature(Long id, String signature) {
			Inspection inspection = findInspectionByVisite(id);
			Visite visite = visiteService.findById(id);
			visite.setStatut(4);
			inspection.setVisite(visite);
		return	inspectionR.save(inspection);
		 
	}
}