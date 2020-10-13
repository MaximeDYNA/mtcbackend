package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_pays")
public class Pays {
	@Id
	private String idPays;
	private String nomPays;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="Pays")
	@JsonIgnore
	Set <DivisionPays> divisionPays;

	public Pays() {
		
	}

	public Pays(String idPays, String nomPays, Set<DivisionPays> divisionPays) {
		super();
		this.idPays = idPays;
		this.nomPays = nomPays;
		this.divisionPays = divisionPays;
	}

	public String getIdPays() {
		return idPays;
	}

	public void setIdPays(String idPays) {
		this.idPays = idPays;
	}

	public String getNomPays() {
		return nomPays;
	}

	public void setNomPays(String nomPays) {
		this.nomPays = nomPays;
	}



	public Set<DivisionPays> getDivisionPays() {
		return divisionPays;
	}



	public void setDivisionPays(Set<DivisionPays> divisionPays) {
		this.divisionPays = divisionPays;
	}


	
	
}
