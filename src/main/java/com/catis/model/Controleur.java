package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="t_controleur")
@EntityListeners(AuditingEntityListener.class)
public class Controleur extends JournalData{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long idControleur;
	private String agremment;
	private int score;
	
	@ManyToOne(optional = true)//id utilisateur optionel
	private Utilisateur utilisateur;
	
	@ManyToOne
	private Partenaire partenaire;
	
	@ManyToOne
	private Organisation organisation;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="controleur")
	@JsonIgnore
	private Set<Inspection> inspections;

	public Controleur() {

	}

	
	public Controleur(Long idControleur, String agremment, int score, Utilisateur utilisateur, Partenaire partenaire,
			Organisation organisation, Set<Inspection> inspections) {
		this.idControleur = idControleur;
		this.agremment = agremment;
		this.score = score;
		this.utilisateur = utilisateur;
		this.partenaire = partenaire;
		this.organisation = organisation;
		this.inspections = inspections;
	}


	public Long getIdControleur() {
		return idControleur;
	}

	public void setIdControleur(Long idControleur) {
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

	public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}
	
	
}
