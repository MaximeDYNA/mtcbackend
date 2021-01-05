package com.catis.objectTemporaire;

public class TestList {

	private String libelle;
	private String valeur;
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public String getValeur() {
		return valeur;
	}
	public void setValeur(String valeur) {
		this.valeur = valeur;
	}
	public TestList(String libelle, String valeur) {
		super();
		this.libelle = libelle;
		this.valeur = valeur;
	}
	public TestList() {
		super();
		// TODO Auto-generated constructor stub
	}

}
