package com.catis.model;


import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_contact")
public class Contact {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long contactId;
	
	private String description;
	
	
	@ManyToOne
	private Partenaire partenaire;

	@OneToMany(fetch = FetchType.LAZY, mappedBy="contact")
	@JsonIgnore
	private Set<Vente> ventes;
	
	public Contact() {
		
	}

	public Contact(Long contactId, String description, Partenaire partenaire, Set<Vente> ventes) {
		super();
		this.contactId = contactId;
		this.description = description;
		this.partenaire = partenaire;
		this.ventes = ventes;
	}

	public Long getContactId() {
		return contactId;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Partenaire getPartenaire() {
		return partenaire;
	}

	public void setPartenaire(Partenaire partenaire) {
		this.partenaire = partenaire;
	}

	public Set<Vente> getVentes() {
		return ventes;
	}

	public void setVentes(Set<Vente> ventes) {
		this.ventes = ventes;
	}
	
	



	
}
