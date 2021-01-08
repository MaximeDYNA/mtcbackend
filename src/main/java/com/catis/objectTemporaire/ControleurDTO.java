package com.catis.objectTemporaire;

public class ControleurDTO {

	private String id;
	private String nom;
	private String prenom;
	private String email;
	private String organisationId;
	public ControleurDTO(String nom, String prenom, String email) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
	}
	public ControleurDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrganisationId() {
		return organisationId;
	}
	public void setOrganisationId(String organisationId) {
		this.organisationId = organisationId;
	}
	
	
	
}
