package com.catis.objectTemporaire;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ListViewCatProduit {

	private String libelle;
	private String createdDate;
	private String modifiedDate;
	private String activeStatus;
	
	
	public ListViewCatProduit(String libelle, String createdDate, String modifiedDate, String activeStatus) {
		super();
		this.libelle = libelle;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.activeStatus = activeStatus;
	}


	public ListViewCatProduit() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDateTime dateCrea) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		this.createdDate = dateCrea.format(formatter);
		 
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(LocalDateTime dateModif) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		this.modifiedDate = dateModif.format(formatter);
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public String getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	
	
}
