package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="t_controleur")
public class Controleur {
	@Id
	private String idControleur;
	private String agremment;
	private int score;
	private String idOrganisation;
	
	@ManyToOne(optional = true, fetch = FetchType.LAZY)//id utilisateur optionel
	@JoinColumn(name="idUtilisateur")
	private Utilisateur utilisateur;
	
	@ManyToOne
	private Partenaire partenaire;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="controleur")
	@JsonIgnore
	private Set<Inspection> inspections;

	public Controleur() {

	}

	public Controleur(String idControleur, String agremment, int score, String idOrganisation, Utilisateur utilisateur,
			Partenaire partenaire, Set<Inspection> inspections) {
		super();
		this.idControleur = idControleur;
		this.agremment = agremment;
		this.score = score;
		this.idOrganisation = idOrganisation;
		this.utilisateur = utilisateur;
		this.partenaire = partenaire;
		this.inspections = inspections;
	}

	public String getIdControleur() {
		return idControleur;
	}

	public void setIdControleur(String idControleur) {
		this.idControleur = idControleur;
	}

	public String getAgremment() {
		return agremment;
	}

	public void setAgremment(String agremment) {
		this.agremment = agremment;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getIdOrganisation() {
		return idOrganisation;
	}

	public void setIdOrganisation(String idOrganisation) {
		this.idOrganisation = idOrganisation;
	}

	

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}





	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}





	public Set<Inspection> getInspections() {
		return inspections;
	}





	public void setInspections(Set<Inspection> inspections) {
		this.inspections = inspections;
	}





	public Partenaire getPartenaire() {
		return partenaire;
	}

	public void setPartenaire(Partenaire partenaire) {
		this.partenaire = partenaire;
	}
	
	
}
