package com.catis.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
		Date now = new Date();
		DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String start = "F"
		+ 	
		formatter.format(now)
		;
		
		return start;
	}
}
