package com.catis.objectTemporaire;

public class OpenData {

	private Long userId;
	private double montantOuverture;
	private double montantFermeture;
	
	public OpenData(Long userId, double montantOuverture, double montantFermeture) {
		super();
		this.userId = userId;
		this.montantOuverture = montantOuverture;
		this.montantFermeture = montantFermeture;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public double getMontantOuverture() {
		return montantOuverture;
	}

	public void setMontantOuverture(double montantOuverture) {
		this.montantOuverture = montantOuverture;
	}

	public double getMontantFermeture() {
		return montantFermeture;
	}

	public void setMontantFermeture(double montantFermeture) {
		this.montantFermeture = montantFermeture;
	}
	
	
	
}
