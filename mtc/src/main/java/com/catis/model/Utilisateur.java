package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_utilisateur")
public class Utilisateur {
	@Id
	private String idUtilisateur;
	private String login;
	private String motDePasse;
	private String idOrganisation;
	
	@ManyToOne
	@JoinColumn(name ="idPartenaire")
	private Partenaire partenaire;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="user")
	@JsonIgnore
	private Set<Caissier> caissiers;
	@OneToMany(fetch = FetchType.LAZY, mappedBy="utilisateur")
	@JsonIgnore
	private Set<Controleur> controleurs;


	public Utilisateur(String idUtilisateur, String login, String motDePasse, String idOrganisation,
			Partenaire partenaire, Set<Caissier> caissiers, Set<Controleur> controleurs) {
		super();
		this.idUtilisateur = idUtilisateur;
		this.login = login;
		this.motDePasse = motDePasse;
		this.idOrganisation = idOrganisation;
		this.partenaire = partenaire;
		this.caissiers = caissiers;
		this.controleurs = controleurs;
	}

	public Utilisateur() {
	}

	public String getIdUtilisateur() {
		return idUtilisateur;
	}

	public void setIdUtilisateur(String idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	public Partenaire getPartenaire() {
		return partenaire;
	}

	public void setPartenaire(Partenaire partenaire) {
		this.partenaire = partenaire;
	}

	public Set<Caissier> getCaissiers() {
		return caissiers;
	}

	public void setCaissiers(Set<Caissier> caissiers) {
		this.caissiers = caissiers;
	}

	public Set<Controleur> getControleurs() {
		return controleurs;
	}

	public void setControleurs(Set<Controleur> controleurs) {
		this.controleurs = controleurs;
	}

	public String getIdOrganisation() {
		return idOrganisation;
	}

	public void setIdOrganisation(String idOrganisation) {
		this.idOrganisation = idOrganisation;
	}
	
	
}
