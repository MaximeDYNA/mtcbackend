package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_utilisateur")
public class Utilisateur {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long utilisateurId;
	private String login;
	private String motDePasse;
	private String idOrganisation;
	
	@ManyToOne
	private Partenaire partenaire;
	
	

	

	public Utilisateur(Long utilisateurId, String login, String motDePasse, String idOrganisation,
			Partenaire partenaire ) {
		super();
		utilisateurId = utilisateurId;
		this.login = login;
		this.motDePasse = motDePasse;
		this.idOrganisation = idOrganisation;
		this.partenaire = partenaire;
		
	}


	public Utilisateur() {
	}

	
	public Long getUtilisateurId() {
		return utilisateurId;
	}


	public void setUtilisateurId(Long utilisateurId) {
		utilisateurId = utilisateurId;
	}


	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	@JsonIgnore
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

	
	

	public String getIdOrganisation() {
		return idOrganisation;
	}

	public void setIdOrganisation(String idOrganisation) {
		this.idOrganisation = idOrganisation;
	}
	
	
}
