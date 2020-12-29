package com.catis.objectTemporaire;

import java.util.List;

import com.catis.model.Lexique;

public class LexiqueReceived {

	private String nom;
	private List<LexiquePOJO> rows;
	
	public LexiqueReceived() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LexiqueReceived(String nom, List<LexiquePOJO> rows) {
		super();
		this.nom = nom;
		this.rows = rows;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public List<LexiquePOJO> getRows() {
		return rows;
	}
	public void setRows(List<LexiquePOJO> rows) {
		this.rows = rows;
	}
	
}
