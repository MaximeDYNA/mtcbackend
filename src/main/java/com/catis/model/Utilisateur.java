package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_utilisateur")
@EntityListeners(AuditingEntityListener.class)
public class Utilisateur extends JournalData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long utilisateurId;
	private String login;
	private String motDePasse;
	@ManyToOne
	@JsonIgnore
	private Organisation organisation;

	@ManyToOne
	private Partenaire partenaire;

	public Utilisateur() {
	}

	public Utilisateur(Long utilisateurId, String login, String motDePasse, Organisation organisation,
			Partenaire partenaire) {

		this.utilisateurId = utilisateurId;
		this.login = login;
		this.motDePasse = motDePasse;
		this.organisation = organisation;
		this.partenaire = partenaire;
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

	public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

}
