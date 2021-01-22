package com.catis.objectTemporaire;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.catis.model.Produit;

public class Listview {

	private Long id;
    private Produit categorie; 
    private String type;
    private String reference;
    private String client; 
    private String date;
    private String statut;
    private List<ExpectedMeasure> measures;

    public Listview() {
		measures.add(new ExpectedMeasure("Freinage", "<span class=\"badge badge-primary\">icon</span>" , false));
		measures.add(new ExpectedMeasure("Ripage", "<span class=\"badge badge-primary\">icon</span>" , false));
		measures.add(new ExpectedMeasure("Pollution", "<span class=\"badge badge-primary\">icon</span>" , false));
		measures.add(new ExpectedMeasure("Règlophare", "<span class=\"badge badge-primary\">icon</span>" , false));
		measures.add(new ExpectedMeasure("Suspension", "<span class=\"badge badge-primary\">icon</span>" , false));
		measures.add(new ExpectedMeasure("Visuels", "<span class=\"badge badge-primary\">icon</span>" , false));
	}
    
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
		switch(statut) {
		  case "maj":
			  this.statut = "<span class=\"badge badge-primary\">"+statut+"</span>";
		    break;
		  case "A inspecter":
			  this.statut = "<span class=\"badge badge-warning\">"+statut+"</span>";
		    break;
		  case "En cours test":
			  this.statut = "<span class=\"badge badge-light\">"+statut+"</span>";
			  break;
		  case "A signer":
			  this.statut = "<span class=\"badge badge-info\">"+statut+"</span>";
			  break;
		  case "A imprimer":
			  this.statut = "<span class=\"badge badge-success\">"+statut+"</span>";
			  break;
		  case "A enregister":
			  this.statut = "<span class=\"badge badge-dark\">"+statut+"</span>";
			  break;
		  case "A certifier":
			  this.statut = "<span class=\"badge badge-primary\">"+statut+"</span>";
			  break;
		  case "Accepté":
			  this.statut = "<span class=\"badge badge-success\">"+statut+"</span>";
			  break;
		  case "Refusé":
			  this.statut = "<span class=\"badge badge-danger\">"+statut+"</span>";
			  break;
		  default:
			  this.statut = "<span class=\"badge badge-warning\">"+statut+"</span>";
		}
		
	}
    
}
