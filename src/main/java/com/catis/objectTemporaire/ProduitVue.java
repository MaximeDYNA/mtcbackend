package com.catis.objectTemporaire;

public class ProduitVue {

	private Long produitId;
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

	public Long getProduitId() {
		return produitId;
	}

	public void setProduitId(Long produitId) {
		this.produitId = produitId;
	}
	
}
