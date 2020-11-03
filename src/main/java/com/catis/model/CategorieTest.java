package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_categorietest")
public class CategorieTest {
	
	@Id
	private String idCategorieTest;
	private String libelle;
	private String description;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="categorietest")
	@JsonIgnore
	Set<Mesure> mesures;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="categorieTest")
	@JsonIgnore
	Set<CategorieTestMachine> categorieTestMachine;

	public CategorieTest() {
	}

	public CategorieTest(String idCategorieTest, String libelle, String description, Set<Mesure> mesures,
			Set<CategorieTestMachine> categorieTestMachine) {
		this.idCategorieTest = idCategorieTest;
		this.libelle = libelle;
		this.description = description;
		this.mesures = mesures;
		this.categorieTestMachine = categorieTestMachine;
	}

	public String getIdCategorieTest() {
		return idCategorieTest;
	}

	public void setIdCategorieTest(String idCategorieTest) {
		this.idCategorieTest = idCategorieTest;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
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

	public Set<CategorieTestMachine> getCategorieTestMachine() {
		return categorieTestMachine;
	}

	public void setCategorieTestMachine(Set<CategorieTestMachine> categorieTestMachine) {
		this.categorieTestMachine = categorieTestMachine;
	}
	
}
