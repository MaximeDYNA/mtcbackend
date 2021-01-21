package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_caissier")
public class Caissier extends JournalData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long caissierId;

	private String codeCaissier;

	@ManyToOne
	private Organisation organisation;

	@ManyToOne
	private Partenaire partenaire;
	
	@ManyToOne
	private Caisse caisse;

	@ManyToOne(optional = true) // id utilisateur optionel
	private Utilisateur user;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "caissier")
	@JsonIgnore
	private Set<SessionCaisse> sessionCaisses;

	public Caissier() {
	}

	
	

	

	public Caissier(Long caissierId, String codeCaissier, Organisation organisation, Partenaire partenaire,
			Caisse caisse, Utilisateur user, Set<SessionCaisse> sessionCaisses) {
		super();
		this.caissierId = caissierId;
		this.codeCaissier = codeCaissier;
		this.organisation = organisation;
		this.partenaire = partenaire;
		this.caisse = caisse;
		this.user = user;
		this.sessionCaisses = sessionCaisses;
	}






	public Long getCaissierId() {
		return caissierId;
	}






	public void setCaissierId(Long caissierId) {
		this.caissierId = caissierId;
	}






	public String getCodeCaissier() {
		return codeCaissier;
	}

	public void setCodeCaissier(String codeCaissier) {
		this.codeCaissier = codeCaissier;
	}

	public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	public Partenaire getPartenaire() {
		return partenaire;
	}

	public void setPartenaire(Partenaire partenaire) {
		this.partenaire = partenaire;
	}

	public Utilisateur getUser() {
		return user;
	}

	public void setUser(Utilisateur user) {
		this.user = user;
	}


	public Caisse getCaisse() {
		return caisse;
	}


	public void setCaisse(Caisse caisse) {
		this.caisse = caisse;
	}


	public Set<SessionCaisse> getSessionCaisses() {
		return sessionCaisses;
	}


	public void setSessionCaisses(Set<SessionCaisse> sessionCaisses) {
		this.sessionCaisses = sessionCaisses;
	}

	
}
