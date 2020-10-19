package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_produit")
public class Produit {

	@Id
	private String produit_id;
	private String libelle;
	private String description;
	private double prix;
	private int delaiValidite;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="produit")
	@JsonIgnore
	private Set<DetailVente> detailVente;
	
	@ManyToOne
	@JoinColumn(name="categorieProduitId")
	private CategorieProduit categorieProduit;
	
	public Produit() {
		
	}

	public Produit(String produit_id, String libelle, String description, double prix, int delaiValidite,
			Set<DetailVente> detailVente, CategorieProduit categorieProduit) {
		super();
		this.produit_id = produit_id;
		this.libelle = libelle;
		this.description = description;
		this.prix = prix;
		this.delaiValidite = delaiValidite;
		this.detailVente = detailVente;
		this.categorieProduit = categorieProduit;
	}

	public String getProduit_id() {
		return produit_id;
	}

	public void setProduit_id(String produit_id) {
		this.produit_id = produit_id;
	}

	public CategorieProduit getCategorieProduit() {
		return categorieProduit;
	}

	public void setCategorieProduit(CategorieProduit categorieProduit) {
		this.categorieProduit = categorieProduit;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPrix() {
		return prix;
	}
	public void setPrix(double prix) {
		this.prix = prix;
	}

	public Set<DetailVente> getDetailVente() {
		return detailVente;
	}

	public void setDetailVente(Set<DetailVente> detailVente) {
		this.detailVente = detailVente;
	}



	public int getDelaiValidite() {
		return delaiValidite;
	}



	public void setDelaiValidite(int delaiValidite) {
		this.delaiValidite = delaiValidite;
	}
	
	
}
