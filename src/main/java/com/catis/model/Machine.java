package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="t_machine")
public class Machine {
	@Id
	private String idMachine;
	private String numSerie; // numéro de série
	private String fabriquant;
	private String model;
	private String idOrganisation;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="machine")
	@JsonIgnore
	private Set<LigneMachine> ligneMachines;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="machine")
	@JsonIgnore
	private Set<ValeurTest> valeurtests;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="machine")
	@JsonIgnore
	private Set<CategorieTestMachine> categorieTestMachine;
	
	public Machine() {

	}

	public Machine(String idMachine, String numSerie, String fabriquant, String model, String idOrganisation,
			Set<LigneMachine> ligneMachines, Set<ValeurTest> valeurtests,
			Set<CategorieTestMachine> categorieTestMachine) {
		super();
		this.idMachine = idMachine;
		this.numSerie = numSerie;
		this.fabriquant = fabriquant;
		this.model = model;
		this.idOrganisation = idOrganisation;
		this.ligneMachines = ligneMachines;
		this.valeurtests = valeurtests;
		this.categorieTestMachine = categorieTestMachine;
	}






	public String getIdMachine() {
		return idMachine;
	}


	public void setIdMachine(String idMachine) {
		this.idMachine = idMachine;
	}


	public String getNumSerie() {
		return numSerie;
	}


	public void setNumSerie(String numSerie) {
		this.numSerie = numSerie;
	}


	public String getFabriquant() {
		return fabriquant;
	}


	public void setFabriquant(String fabriquant) {
		this.fabriquant = fabriquant;
	}


	public String getModel() {
		return model;
	}


	public void setModel(String model) {
		this.model = model;
	}


	public String getIdOrganisation() {
		return idOrganisation;
	}


	public void setIdOrganisation(String idOrganisation) {
		this.idOrganisation = idOrganisation;
	}


	public Set<LigneMachine> getLigneMachines() {
		return ligneMachines;
	}


	public void setLigneMachines(Set<LigneMachine> ligneMachines) {
		this.ligneMachines = ligneMachines;
	}




	public Set<ValeurTest> getValeurtests() {
		return valeurtests;
	}




	public void setValeurtests(Set<ValeurTest> valeurtests) {
		this.valeurtests = valeurtests;
	}






	public Set<CategorieTestMachine> getCategorieTestMachine() {
		return categorieTestMachine;
	}






	public void setCategorieTestMachine(Set<CategorieTestMachine> categorieTestMachine) {
		this.categorieTestMachine = categorieTestMachine;
	}
	
}
