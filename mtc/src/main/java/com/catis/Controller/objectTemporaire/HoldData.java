package com.catis.Controller.objectTemporaire;

public class HoldData {

	private Long sessionCaisseId;
	private Long number;
	public HoldData() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public HoldData(Long sessionCaisseId, Long number) {
		super();
		this.sessionCaisseId = sessionCaisseId;
		this.number = number;
	}

	public Long getSessionCaisseId() {
		return sessionCaisseId;
	}

	public void setSessionCaisseId(Long sessionCaisseId) {
		this.sessionCaisseId = sessionCaisseId;
	}

	public Long getNumber() {
		return number;
	}
	public void setNumber(Long number) {
		this.number = number;
	}
	
}
