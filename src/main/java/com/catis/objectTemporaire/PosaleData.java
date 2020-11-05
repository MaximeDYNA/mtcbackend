package com.catis.objectTemporaire;

public class PosaleData {
	

 	private String reference;
	private boolean status;
	private Long holdId;
	private Long produitId;
	private Long sessionCaisseId;
	public PosaleData(String reference, boolean status, Long holdId, Long produitId, Long sessionCaisseId) {
		super();
		this.reference = reference;
		this.status = status;
		this.holdId = holdId;
		this.produitId = produitId;
		this.sessionCaisseId = sessionCaisseId;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public Long getHoldId() {
		return holdId;
	}
	public void setHoldId(Long holdId) {
		this.holdId = holdId;
	}
	public Long getProduitId() {
		return produitId;
	}
	public void setProduitId(Long produitId) {
		this.produitId = produitId;
	}
	public Long getSessionCaisseId() {
		return sessionCaisseId;
	}
	public void setSessionCaisseId(Long sessionCaisseId) {
		this.sessionCaisseId = sessionCaisseId;
	}
	
	

}
