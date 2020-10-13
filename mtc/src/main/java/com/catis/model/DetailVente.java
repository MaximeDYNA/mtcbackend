package com.catis.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="t_detailvente")
public class DetailVente {
	//table pivot entre produit et vente
	@Id
	private String idDetailVente;
	private String idOrganisation;
	
	@ManyToOne
	@JoinColumn(name="idProduit")
	private Produit produit;
	
	@ManyToOne
	@JoinColumn(name="idVente")
	private Vente vente;

	public DetailVente() {
		
	}

	

	public DetailVente(String idDetailVente, String idOrganisation, Produit produit, Vente vente) {
		super();
		this.idDetailVente = idDetailVente;
		this.idOrganisation = idOrganisation;
		this.produit = produit;
		this.vente = vente;
	}



	public String getIdDetailVente() {
		return idDetailVente;
	}



	public void setIdDetailVente(String idDetailVente) {
		this.idDetailVente = idDetailVente;
	}

	public String getIdOrganisation() {
		return idOrganisation;
	}

	public void setIdOrganisation(String idOrganisation) {
		this.idOrganisation = idOrganisation;
	}

	public Produit getProduit() {
		return produit;
	}

	public void setProduit(Produit produit) {
		this.produit = produit;
	}

	public Vente getVente() {
		return vente;
	}

	public void setVente(Vente vente) {
		this.vente = vente;
	}

}
