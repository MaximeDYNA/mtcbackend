package com.catis.objectTemporaire;

public class RecapDTO {

	private Long caissierId;
	private String dateDebut;
	private String dateFin;
	public RecapDTO(Long caissierId, String dateDebut, String dateFin) {
		super();
		this.caissierId = caissierId;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
	}
	public Long getCaissierId() {
		return caissierId;
	}
	public void setCaissierId(Long caissierId) {
		this.caissierId = caissierId;
	}
	public String getDateDebut() {
		return dateDebut;
	}
	public void setDateDebut(String dateDebut) {
		this.dateDebut = dateDebut;
	}
	public String getDateFin() {
		return dateFin;
	}
	public void setDateFin(String dateFin) {
		this.dateFin = dateFin;
	}
	
	
}
