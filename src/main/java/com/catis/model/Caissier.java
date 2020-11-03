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
@Table(name = "t_caissier")
public class Caissier {
	@Id
	private String caissier_id;
	private String codeCaissier;
	private String idOrganisation;

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

	public Caissier(String caissier_id, String codeCaissier, String idOrganisation, Partenaire partenaire,
			Utilisateur user, Set<CaissierCaisse> caissierCaisses) {
		super();
		this.caissier_id = caissier_id;
		this.codeCaissier = codeCaissier;
		this.idOrganisation = idOrganisation;
		this.partenaire = partenaire;
		this.user = user;
		this.caissierCaisses = caissierCaisses;
		
	}

	public String getCaissier_id() {
		return caissier_id;
	}

	public void setCaissier_id(String idCaissier) {
		this.caissier_id = idCaissier;
	}

	public String getCodeCaissier() {
		return codeCaissier;
	}

	public void setCodeCaissier(String codeCaissier) {
		this.codeCaissier = codeCaissier;
	}

	public String getIdOrganisation() {
		return idOrganisation;
	}

	public void setIdOrganisation(String idOrganisation) {
		this.idOrganisation = idOrganisation;
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
