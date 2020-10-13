package com.catis.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="t_visite")
public class Visite {

	@Id
	private String idVisite;
	private String nature;
	private Date dateDebut;
	private Date dateFin;
	private String statut;
	private String idOrganisation;
	
	@ManyToOne
	@JoinColumn(name="idCaissier")
	private Caissier caissier;
	
	@ManyToOne
	@JoinColumn(name="idCarteGrise")
	private CarteGrise carteGrise;

	public Visite() {
	
	}

	public Visite(String idVisite, String nature, Date dateDebut, Date dateFin, String statut, String idOrganisation,
			Caissier caissier, CarteGrise carteGrise) {
		super();
		this.idVisite = idVisite;
		this.nature = nature;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.statut = statut;
		this.idOrganisation = idOrganisation;
		this.caissier = caissier;
		this.carteGrise = carteGrise;
	}

	public String getIdVisite() {
		return idVisite;
	}

	public void setIdVisite(String idVisite) {
		this.idVisite = idVisite;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public String getIdOrganisation() {
		return idOrganisation;
	}

	public void setIdOrganisation(String idOrganisation) {
		this.idOrganisation = idOrganisation;
	}

	public Caissier getCaissier() {
		return caissier;
	}

	public void setCaissier(Caissier caissier) {
		this.caissier = caissier;
	}

	public CarteGrise getCarteGrise() {
		return carteGrise;
	}

	public void setCarteGrise(CarteGrise carteGrise) {
		this.carteGrise = carteGrise;
	}
	
	
}
