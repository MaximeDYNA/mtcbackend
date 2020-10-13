package com.catis.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_taxe")
public class Taxe {
	
	@Id
	private String idTaxe;
	
	private String nom;
	private String description;
	private String idOrganisation;
	
	
	public Taxe(String idTaxe, String nom, String description, String idOrganisation) {
		super();
		this.idTaxe = idTaxe;
		this.nom = nom;
		this.description = description;
		this.idOrganisation = idOrganisation;
	}
	
	public Taxe() {

	}

	public String getIdTaxe() {
		return idTaxe;
	}
	public void setIdTaxe(String idTaxe) {
		this.idTaxe = idTaxe;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
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
	
	
}
