package com.catis.objectTemporaire;

public class VehiculeByLineDTO {

	private Long carteGriseId;
	private String ref;
	private Long idInspection;
	private Long idCategorie;
	
	
	public VehiculeByLineDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public VehiculeByLineDTO(Long carteGriseId, String ref, Long idInspection, Long idCategorie) {
		super();
		this.carteGriseId = carteGriseId;
		this.ref = ref;
		this.idInspection = idInspection;
		this.idCategorie = idCategorie;
	}


	public Long getCarteGriseId() {
		return carteGriseId;
	}
	public void setCarteGriseId(Long carteGriseId) {
		this.carteGriseId = carteGriseId;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public Long getIdInspection() {
		return idInspection;
	}
	public void setIdInspection(Long idInspection) {
		this.idInspection = idInspection;
	}


	public Long getIdCategorie() {
		return idCategorie;
	}


	public void setIdCategorie(Long idCategorie) {
		this.idCategorie = idCategorie;
	}
	
	
}
