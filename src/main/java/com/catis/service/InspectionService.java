package com.catis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.Inspection;
import com.catis.repository.InspectionRepository;

@Service	
public class InspectionService {

	@Autowired
	private InspectionRepository inspectionR;
	
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
}