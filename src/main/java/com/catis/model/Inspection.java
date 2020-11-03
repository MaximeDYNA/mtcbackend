package com.catis.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="t_inspection")
public class Inspection {
	@Id
	private String idInspection;
	private Date dateDebut;
	private Date dateFin;
	private String signature; // chemin image signature du controleur
	private String idOrganisation;
	
	@ManyToOne
	@JoinColumn(name="idControleur")
	private Controleur controleur;

	@ManyToOne
	@JoinColumn(name="idLigne")
	private Ligne ligne;
	
	@OneToOne
	@JoinColumn(name="idVisite")
	private Visite visite;

	public Inspection() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Inspection(String idInspection, Date dateDebut, Date dateFin, String signature, String idOrganisation,
			Controleur controleur, Ligne ligne, Visite visite) {
		super();
		this.idInspection = idInspection;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.signature = signature;
		this.idOrganisation = idOrganisation;
		this.controleur = controleur;
		this.ligne = ligne;
		this.visite = visite;
	}

	public String getIdInspection() {
		return idInspection;
	}

	public void setIdInspection(String idInspection) {
		this.idInspection = idInspection;
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

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getIdOrganisation() {
		return idOrganisation;
	}

	public void setIdOrganisation(String idOrganisation) {
		this.idOrganisation = idOrganisation;
	}

	public Controleur getControleur() {
		return controleur;
	}

	public void setControleur(Controleur controleur) {
		this.controleur = controleur;
	}

	public Ligne getLigne() {
		return ligne;
	}

	public void setLigne(Ligne ligne) {
		this.ligne = ligne;
	}

	public Visite getVisite() {
		return visite;
	}

	public void setVisite(Visite visite) {
		this.visite = visite;
	}
	
	
}
