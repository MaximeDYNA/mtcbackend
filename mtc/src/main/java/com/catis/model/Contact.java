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
	
	private String mobile1;
	private String mobile2;
	private String fixe;
	private String faxe;
	private String email;
	
	@ManyToOne
	private Partenaire partenaire;

	@OneToMany(fetch = FetchType.LAZY, mappedBy="contact")
	@JsonIgnore
	private Set<Vente> ventes;
	
	public Contact() {
		
	}
	
	public Contact(long contactId, String mobile1, String mobile2, String fixe, String faxe, String email,
			Partenaire partenaire) {
		super();
		this.contactId = contactId;
		this.mobile1 = mobile1;
		this.mobile2 = mobile2;
		this.fixe = fixe;
		this.faxe = faxe;
		this.email = email;
		this.partenaire = partenaire;
	}

	public Long getContactId() {
		return contactId;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}

	public String getMobile1() {
		return mobile1;
	}

	public void setMobile1(String mobile1) {
		this.mobile1 = mobile1;
	}

	public String getMobile2() {
		return mobile2;
	}

	public void setMobile2(String mobile2) {
		this.mobile2 = mobile2;
	}

	public String getFixe() {
		return fixe;
	}

	public void setFixe(String fixe) {
		this.fixe = fixe;
	}

	public String getFaxe() {
		return faxe;
	}

	public void setFaxe(String faxe) {
		this.faxe = faxe;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
