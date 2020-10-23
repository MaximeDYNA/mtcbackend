package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="t_categorieproduit")
public class CategorieProduit {
	
	@Id
	private Long categorieProduitId;
	private String libelle;
	private String description;
	
	@OneToMany(mappedBy="categorieProduit")
	@JsonIgnore
	Set<Produit> produits;

	
	public CategorieProduit() {
		super();
		// TODO Auto-generated constructor stub
	}


	


	public CategorieProduit(Long categorieProduitId, String libelle, String description, Set<Produit> produits) {
		super();
		this.categorieProduitId = categorieProduitId;
		this.libelle = libelle;
		this.description = description;
		this.produits = produits;
	}




	public Long getCategorieProduitId() {
		return categorieProduitId;
	}





	public void setCategorieProduitId(Long categorieProduitId) {
		this.categorieProduitId = categorieProduitId;
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


	public Set<Produit> getProduits() {
		return produits;
	}


	public void setProduits(Set<Produit> produits) {
		this.produits = produits;
	}
	
	

}
