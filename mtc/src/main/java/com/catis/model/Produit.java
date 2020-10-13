package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_produit")
public class Produit {

	@Id
	private String idProduit;
	private String code;
	private String description;
	private double prix;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="produit")
	@JsonIgnore
	private Set<DetailVente> detailVente;
	
	public Produit() {
		
	}
	
	public Produit(String idProduit, String code, String description, double prix, Set<DetailVente> detailVente) {
		super();
		this.idProduit = idProduit;
		this.code = code;
		this.description = description;
		this.prix = prix;
		this.detailVente = detailVente;
	}

	public String getIdProduit() {
		return idProduit;
	}
	public void setIdProduit(String idProduit) {
		this.idProduit = idProduit;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	
	
}
