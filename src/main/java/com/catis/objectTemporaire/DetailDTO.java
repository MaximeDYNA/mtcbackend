package com.catis.objectTemporaire;

public class DetailDTO {

	private String reference;
	private double prix;
	public DetailDTO(String reference, double prix) {
		super();
		this.reference = reference;
		this.prix = prix;
	}
	public DetailDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public double getPrix() {
		return prix;
	}
	public void setPrix(double prix) {
		this.prix = prix;
	}
	
}
