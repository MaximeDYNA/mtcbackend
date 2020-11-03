package com.catis.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="t_taxeproduit")
public class TaxeProduit {
	@Id
	private Long TaxeProduitId;
	@ManyToOne
	private Taxe taxe;
	@ManyToOne
	private Produit produit;
	
	public TaxeProduit(Taxe taxe, Produit produit) {
		super();
		this.taxe = taxe;
		this.produit = produit;
	}
	
	public TaxeProduit() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Taxe getTaxe() {
		return taxe;
	}
	public void setTaxe(Taxe taxe) {
		this.taxe = taxe;
	}
	public Produit getProduit() {
		return produit;
	}
	public void setProduit(Produit produit) {
		this.produit = produit;
	}
	public Long getTaxeProduitId() {
		return TaxeProduitId;
	}
	public void setTaxeProduitId(Long taxeProduitId) {
		TaxeProduitId = taxeProduitId;
	}
	
	

}
