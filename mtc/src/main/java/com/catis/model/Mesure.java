package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_mesure")

public class Mesure {
	//type de test à effectuer pour une catégorie de test
	@Id
	private String idMesure;
	private String code;
	private String description;
	private String idOrganisation;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="mesure")
	@JsonIgnore
	private Set<ValeurTest> valeurtests;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idCategorieTest")
	private CategorieTest categorietest;
	
	public Mesure() {

	}


	public Mesure(String idMesure, String code, String description, String idOrganisation, Set<ValeurTest> valeurtests,
			CategorieTest categorietest) {
		super();
		this.idMesure = idMesure;
		this.code = code;
		this.description = description;
		this.idOrganisation = idOrganisation;
		this.valeurtests = valeurtests;
		this.categorietest = categorietest;
	}



	public String getIdMesure() {
		return idMesure;
	}
	public void setIdMesure(String idMesure) {
		this.idMesure = idMesure;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIdOrganisation() {
		return idOrganisation;
	}
	public void setIdOrganisation(String idOrganisation) {
		this.idOrganisation = idOrganisation;
	}
	public Set<ValeurTest> getValeurtests() {
		return valeurtests;
	}
	public void setValeurtests(Set<ValeurTest> valeurtests) {
		this.valeurtests = valeurtests;
	}


	public CategorieTest getCategorietest() {
		return categorietest;
	}


	public void setCategorietest(CategorieTest categorietest) {
		this.categorietest = categorietest;
	}
	
	
	
}
