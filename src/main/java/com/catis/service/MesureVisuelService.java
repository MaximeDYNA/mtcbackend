package com.catis.service;

import java.util.ArrayList;
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
	
	
	public List<lexique_inspection> addMesureVisuel(List<lexique_inspection>  lexiques) {
		
		List<lexique_inspection> savedLexique = new ArrayList<>();
		 lexiqueinspectionrepository.saveAll(lexiques).forEach(savedLexique::add);
		return savedLexique;
		
	
	}
	
	public MesureVisuel addDataInspection(MesureVisuel mesurevisuels) {
		
		return mesurevisuel.save(mesurevisuels);
		
	
	}
	public List<String> ImagePathList(Long visiteId){
		
		MesureVisuel m =mesurevisuel.findByInspection_VisiteIdVisite(visiteId);
		List<String> paths = new ArrayList<>();
		paths.add(m.getImage1());
		paths.add(m.getImage2());
		
		return paths;
	}

	
}
