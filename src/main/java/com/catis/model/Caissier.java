package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long caissier_id;
	
	private String codeCaissier;
	
	@ManyToOne
	private Organisation organisation;

	@ManyToOne
	private Partenaire partenaire;
	
	@ManyToOne(optional = true)//id utilisateur optionel
	@JoinColumn(name="idUtilisateur")
	private Utilisateur user;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="caissier")
	@JsonIgnore
	private Set<CaissierCaisse> caissierCaisses;


	
	public Caissier() {
	}

	

	public Caissier(Long caissier_id, String codeCaissier, Organisation organisation, Partenaire partenaire,
			Utilisateur user, Set<CaissierCaisse> caissierCaisses) {

		this.caissier_id = caissier_id;
		this.codeCaissier = codeCaissier;
		this.organisation = organisation;
		this.partenaire = partenaire;
		this.user = user;
		this.caissierCaisses = caissierCaisses;
	}



	public Long getCaissier_id() {
		return caissier_id;
	}

	public void setCaissier_id(Long idCaissier) {
		this.caissier_id = idCaissier;
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


	public Set<CaissierCaisse> getCaissierCaisses() {
		return caissierCaisses;
	}


	public void setCaissierCaisses(Set<CaissierCaisse> caissierCaisses) {
		this.caissierCaisses = caissierCaisses;
	}






	
	
}
