package com.catis.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="t_proprietairevehicule")
public class ProprietaireVehicule {

	@Id
	private String idProprietaireVehicule;
	private String idOrganisation;
	
	@ManyToOne
	@JoinColumn(name="idPartenaire")
	private Partenaire partenaire;
	private String description;
	
	public ProprietaireVehicule() {

	}

	public ProprietaireVehicule(String idProprietaireVehicule, String idOrganisation, Partenaire partenaire,
			String description) {
		super();
		this.idProprietaireVehicule = idProprietaireVehicule;
		this.idOrganisation = idOrganisation;
		this.partenaire = partenaire;
		this.description = description;
	}

	public String getIdProprietaireVehicule() {
		return idProprietaireVehicule;
	}

	public void setIdProprietaireVehicule(String idProprietaireVehicule) {
		this.idProprietaireVehicule = idProprietaireVehicule;
	}

	public String getIdOrganisation() {
		return idOrganisation;
	}

	public void setIdOrganisation(String idOrganisation) {
		this.idOrganisation = idOrganisation;
	}

	public Partenaire getPartenaire() {
		return partenaire;
	}

	public void setPartenaire(Partenaire partenaire) {
		this.partenaire = partenaire;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
