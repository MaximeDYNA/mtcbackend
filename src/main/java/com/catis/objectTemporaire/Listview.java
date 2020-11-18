package com.catis.objectTemporaire;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.catis.model.Produit;

public class Listview {

	private Long id;
    private Produit categorie; 
    private String type;
    private String reference;
    private String client; 
    private String date;
    private String statut;

	public Listview(Long id, Produit categorie, String type, String reference, String client, String date,
			String statut) {
		super();
		this.id = id;
		this.categorie = categorie;
		this.type = type;
		this.reference = reference;
		this.client = client;
		this.date = date;
		this.statut = statut;
	}
	public Listview() {
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Produit getCategorie() {
		return categorie;
	}
	public void setCategorie(Produit categorie) {
		this.categorie = categorie;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	
	public String getDate() {
		
		return date;
	}
	public void setDate(LocalDateTime date) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		this.date = date.format(formatter);
	}
	public String getStatut() {
		return statut;
	}
	public void setStatut(String statut) {
		
		this.statut = "<span class=\"badge badge-primary\">"+statut+"</span>";
	}
    
}
