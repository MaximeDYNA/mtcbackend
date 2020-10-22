package com.catis.Controller.objectTemporaire;

public class ProduitVue {

	private long produitId;
	private String reference;
	
	public ProduitVue(long produitId, String reference) {
		super();
		this.produitId = produitId;
		this.reference = reference;
	}

	public ProduitVue() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}

	public long getProduitId() {
		return produitId;
	}

	public void setProduitId(long produitId) {
		this.produitId = produitId;
	}
	
}
