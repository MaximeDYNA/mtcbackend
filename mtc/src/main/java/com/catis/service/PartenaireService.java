package com.catis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.catis.model.Partenaire;
import com.catis.repository.PartenaireRepository;

@Service
public class PartenaireService {
	@Autowired
	private PartenaireRepository partenaireRepository;
	
	public void addPartenaire(Partenaire partenaire) {
		partenaireRepository.save(partenaire);
	}
	public void updatePartenaire(Partenaire partenaire) {
		partenaireRepository.save(partenaire);
	}
	public List<Partenaire> findAllPartenaire() {
		List<Partenaire> partenaires = new ArrayList<>();
		partenaireRepository.findAll().forEach(partenaires::add);
		return partenaires;
	}
	public Partenaire findPartenaireById(String idPartenaire) {
		return partenaireRepository.findById(idPartenaire).get();
	}
	public List<Partenaire> findPartenaireByNom(String nom) {
		return partenaireRepository
				.findByNomStartsWithIgnoreCaseOrPrenomStartsWithIgnoreCaseOrPassportStartsWithIgnoreCaseOrTelephoneStartsWithIgnoreCase(nom, nom, nom, nom);
	}
	public void deletePartenaireById(String idPartenaire) {
		partenaireRepository.deleteById(idPartenaire);
	}
	public boolean partenaireAlreadyExist(Partenaire partenaire) {
		
		return
		findPartenaireByNom(partenaire.getNom()).stream()
		.filter(part -> part.getPrenom().equals(partenaire.getPrenom()))
		.filter(part -> part.getCni().equals(partenaire.getCni()))
		.collect(Collectors.toList())
		.isEmpty();
	}
}
