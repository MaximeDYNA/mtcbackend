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
	private String caisse_id;
	private String description;
	private String idOrganisation; 
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="caisse")
	@JsonIgnore
	private Set<CaissierCaisse> caissiercaisses;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="caisse")
	@JsonIgnore
	private Set<SessionCaisse> sessionCaisse;
	
	public Caisse() {
	}

	



	public Caisse(String caisse_id, String description, String idOrganisation, Set<CaissierCaisse> caissiercaisses,
			Set<SessionCaisse> sessionCaisse) {
		super();
		this.caisse_id = caisse_id;
		this.description = description;
		this.idOrganisation = idOrganisation;
		this.caissiercaisses = caissiercaisses;
		this.sessionCaisse = sessionCaisse;
	}





	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdCaisse() {
		return caisse_id;
	}

	public void setIdCaisse(String idCaisse) {
		this.caisse_id = idCaisse;
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





	public String getCaisse_id() {
		return caisse_id;
	}





	public void setCaisse_id(String caisse_id) {
		this.caisse_id = caisse_id;
	}





	public Set<SessionCaisse> getSessionCaisse() {
		return sessionCaisse;
	}





	public void setSessionCaisse(Set<SessionCaisse> sessionCaisse) {
		this.sessionCaisse = sessionCaisse;
	}

	

}
