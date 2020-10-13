package com.catis.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_contact")
public class Contact {

	@Id
	private String idContact;
	
	private String mobile1;
	private String mobile2;
	private String fixe;
	private String faxe;
	private String email;
	
	@ManyToOne
	@JoinColumn(name="idOrganisation")
	private Organisation organisation;
	
	@ManyToOne
	@JoinColumn(name="id_partenaire")
	private Partenaire partenaire;
	
	public Contact() {
		
	}
	public Contact(String idContact, String mobile1, String mobile2, String fixe, String faxe, String email,
			Organisation organisation, Partenaire partenaire) {
		super();
		this.idContact = idContact;
		this.mobile1 = mobile1;
		this.mobile2 = mobile2;
		this.fixe = fixe;
		this.faxe = faxe;
		this.email = email;
		this.organisation = organisation;
		this.partenaire = partenaire;
	}
	public String getIdContact() {
		return idContact;
	}
	public void setIdContact(String idContact) {
		this.idContact = idContact;
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
	
}
