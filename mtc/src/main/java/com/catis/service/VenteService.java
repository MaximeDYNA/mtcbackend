package com.catis.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.Vente;
import com.catis.repository.VenteRepository;

@Service
public class VenteService {

	@Autowired
	private VenteRepository venteRepository;
	
	public Vente addVente(Vente vente) {
		return venteRepository.save(vente);			
	}
	public Vente findById(Long id) {
		return venteRepository.findById(id).get();
	}
	public String genererNumFacture() {
		LocalDate now = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddhhmmssSSSS");
		String start = "F" + now.format(formatter);
		
		return start;
	}
}
