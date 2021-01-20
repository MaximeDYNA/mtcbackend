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
	private String keycloakId;
	@ManyToOne
	@JsonIgnore
	private Organisation organisation;

	

	public Utilisateur() {
	}



	public Utilisateur(Long utilisateurId, String keycloakId, Organisation organisation) {
		super();
		this.utilisateurId = utilisateurId;
		this.keycloakId = keycloakId;
		this.organisation = organisation;
	}



	public Long getUtilisateurId() {
		return utilisateurId;
	}



	public void setUtilisateurId(Long utilisateurId) {
		this.utilisateurId = utilisateurId;
	}



	public String getKeycloakId() {
		return keycloakId;
	}



	public void setKeycloakId(String keycloakId) {
		this.keycloakId = keycloakId;
	}



	public Organisation getOrganisation() {
		return organisation;
	}



	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	
}
