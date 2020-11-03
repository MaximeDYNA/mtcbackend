package com.catis.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_adresse")
public class Adresse {

	@Id
	private String idAdresse;
	
	private String description;
	@ManyToOne
	@JoinColumn(name="idOrganisation")
	private Organisation organisation;
	
	@ManyToOne
	@JoinColumn(name="idPays")
	private Pays pays;

	public Adresse() {
	}

	public Adresse(String idAdresse, String description, Organisation organisation, Pays pays) {
		super();
		this.idAdresse = idAdresse;
		this.description = description;
		this.organisation = organisation;
		this.pays = pays;
	}

	public String getIdAdresse() {
		return idAdresse;
	}

	public void setIdAdresse(String idAdresse) {
		this.idAdresse = idAdresse;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Pays getPays() {
		return pays;
	}

	public void setPays(Pays pays) {
		this.pays = pays;
	}



	public Organisation getOrganisation() {
		return organisation;
	}



	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}
	
	
}
