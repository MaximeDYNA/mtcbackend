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
@Table(name = "t_organisation")
@EntityListeners(AuditingEntityListener.class)
public class Organisation extends JournalData {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long organisationId;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="organisation")
	@JsonIgnore
	Set<Adresse> adresse;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="organisation")
	@JsonIgnore
	Set<Partenaire> partenaires;
	
	@OneToMany(mappedBy = "parentOrganisation")
	private Set<Organisation> childOrganisations; 

	@ManyToOne
	private Organisation parentOrganisation;
	
	private String patente;
	private String statutJurique;
	private String numeroDeContribuable;
	
	public Organisation() {
		
	}

	public Organisation(Long organisationId, Set<Adresse> adresse, String patente, String statutJurique,
			String numeroDeContribuable) {
		super();
		this.organisationId = organisationId;
		this.adresse = adresse;
		this.patente = patente;
		this.statutJurique = statutJurique;
		this.numeroDeContribuable = numeroDeContribuable;
	}








	public Long getOrganisationId() {
		return organisationId;
	}








	public void setOrganisationId(Long organisationId) {
		this.organisationId = organisationId;
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
