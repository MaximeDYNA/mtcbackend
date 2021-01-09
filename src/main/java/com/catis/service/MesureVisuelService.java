package com.catis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.MesureVisuel;
import com.catis.model.lexique_inspection;
import com.catis.repository.LexiqueInspectionRepository;
import com.catis.repository.MesureVisuelRepository;

@Service
public class MesureVisuelService {
	
	@Autowired
	private MesureVisuelRepository mesurevisuel;
	
	@Autowired
	private LexiqueInspectionRepository lexiqueinspectionrepository;
	
	
	public Iterable<lexique_inspection> addMesureVisuel(List<lexique_inspection>  lexiques) {
		
		return lexiqueinspectionrepository.saveAll(lexiques);
		
	
	}
	
	public MesureVisuel addDataInspection(MesureVisuel mesurevisuels) {
		
		return mesurevisuel.save(mesurevisuels);
		
	
	}

	
}
