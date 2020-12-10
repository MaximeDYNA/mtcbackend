package com.catis.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Formule {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String description;
	
	@OneToMany(mappedBy = "formule")
	private Set<Mesure> mesures;
	
	@OneToMany(mappedBy = "formule", cascade = CascadeType.ALL)
	private Set<Seuil> seuils;

	public Formule() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Formule(Long id, String description, Set<Mesure> mesures, Set<Seuil> seuils) {
		super();
		this.id = id;
		this.description = description;
		this.mesures = mesures;
		this.seuils = seuils;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Mesure> getMesures() {
		return mesures;
	}

	public void setMesures(Set<Mesure> mesures) {
		this.mesures = mesures;
	}

	public Set<Seuil> getSeuils() {
		return seuils;
	}

	public void setSeuils(Set<Seuil> seuils) {
		this.seuils = seuils;
	}
	
}