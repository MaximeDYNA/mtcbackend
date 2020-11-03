package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="t_ligne")
public class Ligne {
	
	@Id
	private String idLigne;
	private String description;
	private String idOrganisation;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="ligne")
	@JsonIgnore
	private Set<LigneMachine> ligneMachines;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="ligne")
	@JsonIgnore
	private Set<Inspection> inspections;

	
	public Ligne() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Ligne(String idLigne, String description, String idOrganisation, Set<LigneMachine> ligneMachines,
			Set<Inspection> inspections) {
		this.idLigne = idLigne;
		this.description = description;
		this.idOrganisation = idOrganisation;
		this.ligneMachines = ligneMachines;
		this.inspections = inspections;
	}

	public String getIdLigne() {
		return idLigne;
	}

	public void setIdLigne(String idLigne) {
		this.idLigne = idLigne;
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

	public Set<LigneMachine> getLigneMachines() {
		return ligneMachines;
	}

	public void setLigneMachines(Set<LigneMachine> ligneMachines) {
		this.ligneMachines = ligneMachines;
	}

	public Set<Inspection> getInspections() {
		return inspections;
	}

	public void setInspections(Set<Inspection> inspections) {
		this.inspections = inspections;
	}
	
	
}
