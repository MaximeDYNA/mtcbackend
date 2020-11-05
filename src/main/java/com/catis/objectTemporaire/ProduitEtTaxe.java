package com.catis.objectTemporaire;

import java.util.List;

import com.catis.model.Produit;
import com.catis.model.Taxe;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel(description="produits et ses taxes")
public class ProduitEtTaxe {

	@ApiModelProperty(notes = "le produit")
	private Produit produit;
	@ApiModelProperty(notes = "les taxes")
	private List<Taxe> taxe;

	public ProduitEtTaxe() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ProduitEtTaxe(Produit produit, List<Taxe> taxe) {
		super();
		this.produit = produit;
		this.taxe = taxe;
	}
	public Produit getProduit() {
		return produit;
	}
	public void setProduit(Produit produit) {
		this.produit = produit;
	}
	public List<Taxe> getTaxe() {
		return taxe;
	}
	public void setTaxe(List<Taxe> taxe) {
		this.taxe = taxe;
	}
	
	
	
	
}
