package com.catis.model;


import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_contact")
@EntityListeners(AuditingEntityListener.class)
public class Contact extends JournalData {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long contactId;
	
	private String description;
	
	
	@ManyToOne
	private Partenaire partenaire;

	@ManyToOne
	private Client client;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="contact")
	@JsonIgnore
	private Set<Vente> ventes;
	
	public Contact() {
		
	}

	public Contact(Long contactId, String description, Partenaire partenaire, Set<Vente> ventes) {
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

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
	



	
}
