package com.catis.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_adresse")
public class Adresse extends JournalData{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long adresseId;
	private String nom;
	private String description;
	
	@ManyToOne
	private Organisation organisation;
	
	@ManyToOne
	private Partenaire partenaire;
	
	@ManyToOne
	private Pays pays;
	
	@ManyToOne
	private DivisionPays divisionPays;
		
	
	public Adresse() {
	}

	

	public Adresse(Long adresseId, String nom, String description, Organisation organisation, Pays pays,
			DivisionPays divisionPays) {
		super();
		this.adresseId = adresseId;
		this.nom = nom;
		this.description = description;
		this.organisation = organisation;
		this.pays = pays;
		this.divisionPays = divisionPays;
	}



	

	public Long getAdresseId() {
		return adresseId;
	}



	public void setAdresseId(Long adresseId) {
		this.adresseId = adresseId;
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

	public DivisionPays getDivisionPays() {
		return divisionPays;
	}

	public void setDivisionPays(DivisionPays divisionPays) {
		this.divisionPays = divisionPays;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}



	public Partenaire getPartenaire() {
		return partenaire;
	}



	public void setPartenaire(Partenaire partenaire) {
		this.partenaire = partenaire;
	}

	
}
