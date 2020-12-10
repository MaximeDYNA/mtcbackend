package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="t_machine")
public class Machine {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long idMachine;
	private String numSerie; // numéro de série
	private String fabriquant;
	private String model;
	private String idOrganisation;

	@OneToMany(fetch = FetchType.LAZY, mappedBy="machine")
	@JsonIgnore
	private Set<LigneMachine> ligneMachines;

	@OneToMany(fetch = FetchType.LAZY, mappedBy="machine")
	@JsonIgnore
	private Set<CategorieTestMachine> categorieTestMachine;

	@OneToMany(mappedBy = "machine")
	private Set<GieglanFile> gieglanFiles;

	public Machine() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Machine(Long idMachine, String numSerie, String fabriquant, String model, String idOrganisation,
			Set<LigneMachine> ligneMachines, Set<CategorieTestMachine> categorieTestMachine,
			Set<GieglanFile> gieglanFiles) {
		super();
		this.idMachine = idMachine;
		this.numSerie = numSerie;
		this.fabriquant = fabriquant;
		this.model = model;
		this.idOrganisation = idOrganisation;
		this.ligneMachines = ligneMachines;
		this.categorieTestMachine = categorieTestMachine;
		this.gieglanFiles = gieglanFiles;
	}

	public Long getIdMachine() {
		return idMachine;
	}

	public void setIdMachine(Long idMachine) {
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

	public Set<CategorieTestMachine> getCategorieTestMachine() {
		return categorieTestMachine;
	}

	public void setCategorieTestMachine(Set<CategorieTestMachine> categorieTestMachine) {
		this.categorieTestMachine = categorieTestMachine;
	}

	public Set<GieglanFile> getGieglanFiles() {
		return gieglanFiles;
	}

	public void setGieglanFiles(Set<GieglanFile> gieglanFiles) {
		this.gieglanFiles = gieglanFiles;
	}
}
