package com.catis.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.catis.model.CaissierCaisse;
import com.catis.repository.CaissierCaisseRepository;

@Service
public class CaissierCaisseService {
	
	@Autowired
	private CaissierCaisseRepository caissierCaisseRepository;
	
	public void addCaissierCaisse(CaissierCaisse caissiercaisse) {
		caissierCaisseRepository.save(caissiercaisse);
	}
	public void updateCaissierCaisse(CaissierCaisse caissierCaisse) {
		caissierCaisseRepository.save(caissierCaisse);
	}
	public List<CaissierCaisse> findAllCaissierCaisse() {
		List<CaissierCaisse> caissiercaisses = new ArrayList<>();
		caissierCaisseRepository.findAll().forEach(caissiercaisses::add);
		return caissiercaisses;
	}
	public CaissierCaisse findCaisseById(String idCaissierCaisse) {
		return caissierCaisseRepository.findById(idCaissierCaisse).get();
	}
	public void deleteCaissierCaisseById(String idCaissierCaisse) {
		caissierCaisseRepository.deleteById(idCaissierCaisse);
	}
	
}
