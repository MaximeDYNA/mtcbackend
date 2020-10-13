package com.catis.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_partenaire")
public class Partenaire {
	@Id
	private String id_partenaire;
	private String nom;
	private String prenom;
	private Date dateNaiss; // date de naissance
	private String lieuDeNaiss; // lieu de naissance
	private String passport; 
	private String permiDeConduire;
	private String cni;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="partenaire")
	@JsonIgnore
	Set<ProprietaireVehicule> proprietaireVehicule;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="partenaire")
	@JsonIgnore
	Set<Utilisateur> utilisateurs;
	
	@OneToMany(mappedBy="partenaire")
	@JsonIgnore
	Set<Contact> contacts;
	
	@OneToMany(mappedBy="partenaire")
	@JsonIgnore
	Set<Controleur> controleurs;
	
	@OneToMany(mappedBy="partenaire")
	@JsonIgnore
	Set<Caissier> caissiers;
	

	
	public Partenaire() {
	}

	



	public Partenaire(String id_partenaire, String nom, String prenom, Date dateNaiss, String lieuDeNaiss,
			String passport, String permiDeConduire, String cni, Set<ProprietaireVehicule> proprietaireVehicule,
			Set<Utilisateur> utilisateurs, Set<Contact> contacts, Set<Controleur> controleurs,
			Set<Caissier> caissiers) {
		super();
		this.id_partenaire = id_partenaire;
		this.nom = nom;
		this.prenom = prenom;
		this.dateNaiss = dateNaiss;
		this.lieuDeNaiss = lieuDeNaiss;
		this.passport = passport;
		this.permiDeConduire = permiDeConduire;
		this.cni = cni;
		this.proprietaireVehicule = proprietaireVehicule;
		this.utilisateurs = utilisateurs;
		this.contacts = contacts;
		this.controleurs = controleurs;
		this.caissiers = caissiers;
	}





	public Set<ProprietaireVehicule> getProprietaireVehicule() {
		return proprietaireVehicule;
	}

	public void setProprietaireVehicule(Set<ProprietaireVehicule> proprietaireVehicule) {
		this.proprietaireVehicule = proprietaireVehicule;
	}

	public String getId_partenaire() {
		return id_partenaire;
	}
	public void setId_partenaire(String id_partenaire) {
		this.id_partenaire = id_partenaire;
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



	public Set<Utilisateur> getUtilisateurs() {
		return utilisateurs;
	}



	public void setUtilisateurs(Set<Utilisateur> utilisateurs) {
		this.utilisateurs = utilisateurs;
	}



	public Set<Contact> getContacts() {
		return contacts;
	}



	public void setContacts(Set<Contact> contacts) {
		this.contacts = contacts;
	}





	public Set<Controleur> getControleurs() {
		return controleurs;
	}





	public void setControleurs(Set<Controleur> controleurs) {
		this.controleurs = controleurs;
	}





	public Set<Caissier> getCaissiers() {
		return caissiers;
	}





	public void setCaissiers(Set<Caissier> caissiers) {
		this.caissiers = caissiers;
	}
	
	
}
