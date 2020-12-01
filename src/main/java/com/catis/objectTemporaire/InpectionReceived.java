package com.catis.objectTemporaire;

import java.util.Date;



public class InpectionReceived {

	private Long idInspection;
	private Date dateDebut;
	private Date dateFin;
	private String signature; // chemin image signature du controleur
	private Long produitId;
	private double kilometrage;
	private String chassis;
	private int essieux;
	private String position;
	private Long controleurId;
	private Long ligneId;
	private Long visiteId;
	
	
	public InpectionReceived() {
		super();
		// TODO Auto-generated constructor stub
	}


	public InpectionReceived(Long idInspection, Date dateDebut, Date dateFin, String signature, Long produitId,
			double kilometrage, String chassis, int essieux, String position, Long controleurId, Long ligneId,
			Long visiteId) {
		super();
		this.idInspection = idInspection;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.signature = signature;
		this.produitId = produitId;
		this.kilometrage = kilometrage;
		this.chassis = chassis;
		this.essieux = essieux;
		this.position = position;
		this.controleurId = controleurId;
		this.ligneId = ligneId;
		this.visiteId = visiteId;
	}


	public Long getIdInspection() {
		return idInspection;
	}


	public void setIdInspection(Long idInspection) {
		this.idInspection = idInspection;
	}


	public Date getDateDebut() {
		return dateDebut;
	}


	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}


	public Date getDateFin() {
		return dateFin;
	}


	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}


	public String getSignature() {
		return signature;
	}


	public void setSignature(String signature) {
		this.signature = signature;
	}


	public Long getProduitId() {
		return produitId;
	}


	public void setProduitId(Long produitId) {
		this.produitId = produitId;
	}


	public double getKilometrage() {
		return kilometrage;
	}


	public void setKilometrage(double kilometrage) {
		this.kilometrage = kilometrage;
	}


	public String getChassis() {
		return chassis;
	}


	public void setChassis(String chassis) {
		this.chassis = chassis;
	}


	public int getEssieux() {
		return essieux;
	}


	public void setEssieux(int essieux) {
		this.essieux = essieux;
	}


	public String getPosition() {
		return position;
	}


	public void setPosition(String position) {
		this.position = position;
	}


	public Long getControleurId() {
		return controleurId;
	}


	public void setControleurId(Long controleurId) {
		this.controleurId = controleurId;
	}


	public Long getLigneId() {
		return ligneId;
	}


	public void setLigneId(Long ligneId) {
		this.ligneId = ligneId;
	}


	public Long getVisiteId() {
		return visiteId;
	}


	public void setVisiteId(Long visiteId) {
		this.visiteId = visiteId;
	}
	
	
}
