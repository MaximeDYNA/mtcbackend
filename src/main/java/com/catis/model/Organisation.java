package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_organisation")
public class Organisation {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long organisationId;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="organisation")
	@JsonIgnore
	Set<Adresse> adresse;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="organisation")
	@JsonIgnore
	Set<Partenaire> partenaires;
	
	private String patente;
	private String statutJurique;
	private String numeroDeContribuable;
	private String idOrganisationParent;
	
	public Organisation() {
		
	}

	public Organisation(Long organisationId, Set<Adresse> adresse, String patente, String statutJurique,
			String numeroDeContribuable, String idOrganisationParent) {
		super();
		this.organisationId = organisationId;
		this.adresse = adresse;
		this.patente = patente;
		this.statutJurique = statutJurique;
		this.numeroDeContribuable = numeroDeContribuable;
		this.idOrganisationParent = idOrganisationParent;
	}








	public Long getOrganisationId() {
		return organisationId;
	}








	public void setOrganisationId(Long organisationId) {
		this.organisationId = organisationId;
	}








	public String getIdOrganisationParent() {
		return idOrganisationParent;
	}

	public void setIdOrganisationParent(String idOrganisationParent) {
		this.idOrganisationParent = idOrganisationParent;
	}

	public String getPatente() {
		return patente;
	}
	public void setPatente(String patente) {
		this.patente = patente;
	}
	public String getStatutJurique() {
		return statutJurique;
	}
	public void setStatutJurique(String statutJurique) {
		this.statutJurique = statutJurique;
	}
	public String getNumeroDeContribuable() {
		return numeroDeContribuable;
	}
	public void setNumeroDeContribuable(String numeroDeContribuable) {
		this.numeroDeContribuable = numeroDeContribuable;
	}

	public Set<Adresse> getAdresse() {
		return adresse;
	}

	public void setAdresse(Set<Adresse> adresse) {
		this.adresse = adresse;
	}



	public Set<Partenaire> getPartenaires() {
		return partenaires;
	}



	public void setPartenaire(Set<Partenaire> partenaires) {
		this.partenaires = partenaires;
	}



	
}
