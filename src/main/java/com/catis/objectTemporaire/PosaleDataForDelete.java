package com.catis.objectTemporaire;

public class PosaleDataForDelete {

	private String reference;
	private Long sessionCaisseId;
	public PosaleDataForDelete(String reference, Long sessionCaisseId) {
		super();
		this.reference = reference;
		this.sessionCaisseId = sessionCaisseId;
	}
	public PosaleDataForDelete() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public Long getSessionCaisseId() {
		return sessionCaisseId;
	}
	public void setSessionCaisseId(Long sessionCaisseId) {
		this.sessionCaisseId = sessionCaisseId;
	}
	
}
