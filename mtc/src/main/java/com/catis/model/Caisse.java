package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_caisse")
public class Caisse {
	@Id
	private String idCaisse;
	private String description;
	private String idOrganisation; 
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="caisse")
	@JsonIgnore
	private Set<CaissierCaisse> caissiercaisses;
	
	public Caisse() {
	}

	public Caisse(String idCaisse, String description, String idOrganisation, Set<CaissierCaisse> caissiercaisses) {
		super();
		this.idCaisse = idCaisse;
		this.description = description;
		this.idOrganisation = idOrganisation;
		this.caissiercaisses = caissiercaisses;
	}



	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdCaisse() {
		return idCaisse;
	}

	public void setIdCaisse(String idCaisse) {
		this.idCaisse = idCaisse;
	}

	public String getIdOrganisation() {
		return idOrganisation;
	}

	public void setIdOrganisation(String idOrganisation) {
		this.idOrganisation = idOrganisation;
	}



	public Set<CaissierCaisse> getCaissiercaisses() {
		return caissiercaisses;
	}



	public void setCaissiercaisses(Set<CaissierCaisse> caissiercaisses) {
		this.caissiercaisses = caissiercaisses;
	}

	

}
