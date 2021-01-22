package com.catis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.MesureVisuel;
import com.catis.repository.MesureVisuelRepository;

@Service
public class MesureVisuelService {
	
	@Autowired
	private MesureVisuelRepository mesurevisuel;
	
	
	public MesureVisuel addDataInspection(MesureVisuel mesurevisuels) {
		MesureVisuel m = mesurevisuel.save(mesurevisuels);
		return m;
		
	}
	public List<String> ImagePathList(Long visiteId){
		
		MesureVisuel m =mesurevisuel.findByInspection_VisiteIdVisite(visiteId);
		List<String> paths = new ArrayList<>();
		paths.add(m.getImage1());
		paths.add(m.getImage2());
		
		return paths;
	}
}