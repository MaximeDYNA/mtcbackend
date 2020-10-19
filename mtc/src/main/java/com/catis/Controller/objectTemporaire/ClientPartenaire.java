package com.catis.Controller.objectTemporaire;

import java.util.Date;

public class ClientPartenaire {
	private String nom;
	private String prenom;
	private Date dateNaiss;
	private String lieuDeNaiss;
	private String passport; 
	private String permiDeConduire;
	private String cni;
	private String descriptionClient;
	private String idorganisation;
	
	public ClientPartenaire(String nom, String prenom, Date dateNaiss, String lieuDeNaiss, String passport,
			String permiDeConduire, String cni, String descriptionClient, String idorganisation) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.dateNaiss = dateNaiss;
		this.lieuDeNaiss = lieuDeNaiss;
		this.passport = passport;
		this.permiDeConduire = permiDeConduire;
		this.cni = cni;
		this.descriptionClient = descriptionClient;
		this.idorganisation = idorganisation;
	}
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public Date getDateNaiss() {
		return dateNaiss;
	}
	public void setDateNaiss(Date dateNaiss) {
		this.dateNaiss = dateNaiss;
	}
	public String getLieuDeNaiss() {
		return lieuDeNaiss;
	}
	public void setLieuDeNaiss(String lieuDeNaiss) {
		this.lieuDeNaiss = lieuDeNaiss;
	}
	public String getPassport() {
		return passport;
	}
	public void setPassport(String passport) {
		this.passport = passport;
	}
	public String getPermiDeConduire() {
		return permiDeConduire;
	}
	public void setPermiDeConduire(String permiDeConduire) {
		this.permiDeConduire = permiDeConduire;
	}
	public String getCni() {
		return cni;
	}
	public void setCni(String cni) {
		this.cni = cni;
	}
	public String getDescriptionClient() {
		return descriptionClient;
	}
	public void setDescriptionClient(String descriptionClient) {
		this.descriptionClient = descriptionClient;
	}
	public String getIdorganisation() {
		return idorganisation;
	}
	public void setIdorganisation(String idorganisation) {
		this.idorganisation = idorganisation;
	}

}
