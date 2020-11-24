package com.catis.objectTemporaire;

import java.time.LocalDateTime;

public class CategorieProduitView {

	private String nom;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;
	
	public CategorieProduitView() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public CategorieProduitView(String nom, LocalDateTime createdDate, LocalDateTime modifiedDate) {
		super();
		this.nom = nom;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
	}

	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	
}
