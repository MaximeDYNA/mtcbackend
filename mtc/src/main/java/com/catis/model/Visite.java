package com.catis.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="t_visite")
public class Visite {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idVisite;
	private boolean contreVisite;
	private Date dateDebut;
	private Date dateFin;
	private String statut;
	private String idOrganisation;
	
	@ManyToOne
	@JoinColumn(name="idCaissier")
	private Caissier caissier;
	
	@ManyToOne
	private CarteGrise carteGrise;

	public Visite() {
	
	}


	public Visite(Long idVisite, boolean contreVisite, Date dateDebut, Date dateFin, String statut,
			String idOrganisation, Caissier caissier, CarteGrise carteGrise) {
		super();
		this.idVisite = idVisite;
		this.contreVisite = contreVisite;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.statut = statut;
		this.idOrganisation = idOrganisation;
		this.caissier = caissier;
		this.carteGrise = carteGrise;
	}









	public boolean isContreVisite() {
		return contreVisite;
	}









	public void setContreVisite(boolean contreVisite) {
		this.contreVisite = contreVisite;
	}









	public Long getIdVisite() {
		return idVisite;
	}



	public void setIdVisite(Long idVisite) {
		this.idVisite = idVisite;
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
