package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_mesure")
public class Mesure {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long idMesure;
	private String code;
	private String description;
	private String idOrganisation;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="mesure")
	@JsonIgnore
	private Set<ValeurTest> valeurtests;

	@ManyToOne
	private Formule formule;
	
	@ManyToOne
	private CategorieTestVehicule categorieTestVehicule;

	
	
	public Mesure() {
		super();
		// TODO Auto-generated constructor stub
	}



	public Mesure(Long idMesure, String code, String description, String idOrganisation, Set<ValeurTest> valeurtests,
			Formule formule, CategorieTestVehicule categorieTestVehicule) {
		super();
		this.idMesure = idMesure;
		this.code = code;
		this.description = description;
		this.idOrganisation = idOrganisation;
		this.valeurtests = valeurtests;
		this.formule = formule;
		this.categorieTestVehicule = categorieTestVehicule;
	}



	public Long getIdMesure() {
		return idMesure;
	}



	public void setIdMesure(Long idMesure) {
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



	public Formule getFormule() {
		return formule;
	}



	public void setFormule(Formule formule) {
		this.formule = formule;
	}



	public CategorieTestVehicule getCategorieTestVehicule() {
		return categorieTestVehicule;
	}



	public void setCategorieTestVehicule(CategorieTestVehicule categorieTestVehicule) {
		this.categorieTestVehicule = categorieTestVehicule;
	}
	
}
