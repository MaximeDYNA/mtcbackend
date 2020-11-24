package com.catis.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	public List<Vente> findAll() {
		List<Vente> ventes = new ArrayList<>();
		venteRepository.findAll().forEach(ventes::add);
		return ventes;			
	}
	public Vente findById(Long id) {
		return venteRepository.findById(id).get();
	}
	public Vente findByVisite(Long id) {
		return venteRepository.findByVisite_IdVisite(id);
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
