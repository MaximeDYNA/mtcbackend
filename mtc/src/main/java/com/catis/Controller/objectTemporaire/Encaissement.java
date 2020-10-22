package com.catis.Controller.objectTemporaire;

import java.util.List;

public class Encaissement {

	private long clientId;
	private long vendeurId;
	private long contactid;
	private double montantTotal;
	private double montantEncaisse;
	private String numeroTicket;
	private long sessionCaisseId;
	private List<ProduitVue> produitVue;
	
	

	public Encaissement(long clientId, long vendeurId, long contactid, double montantTotal, double montantEncaisse,
			String numeroTicket, long sessionCaisseId, List<ProduitVue> produitVue) {
		super();
		this.clientId = clientId;
		this.vendeurId = vendeurId;
		this.contactid = contactid;
		this.montantTotal = montantTotal;
		this.montantEncaisse = montantEncaisse;
		this.numeroTicket = numeroTicket;
		this.sessionCaisseId = sessionCaisseId;
		this.produitVue = produitVue;
	}

	public Encaissement() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public long getVendeurId() {
		return vendeurId;
	}

	public void setVendeurId(long vendeurId) {
		this.vendeurId = vendeurId;
	}

	public long getContactid() {
		return contactid;
	}

	public void setContactid(long contactid) {
		this.contactid = contactid;
	}

	public double getMontantTotal() {
		return montantTotal;
	}

	public void setMontantTotal(double montantTotal) {
		this.montantTotal = montantTotal;
	}

	public double getMontantEncaisse() {
		return montantEncaisse;
	}

	public void setMontantEncaisse(double montantEncaisse) {
		this.montantEncaisse = montantEncaisse;
	}

	public String getNumeroTicket() {
		return numeroTicket;
	}

	public void setNumeroTicket(String numeroTicket) {
		this.numeroTicket = numeroTicket;
	}

	public long getSessionCaisseId() {
		return sessionCaisseId;
	}

	public void setSessionCaisseId(long sessionCaisseId) {
		this.sessionCaisseId = sessionCaisseId;
	}

	public List<ProduitVue> getProduitVue() {
		return produitVue;
	}

	public void setProduitVue(List<ProduitVue> produitVue) {
		this.produitVue = produitVue;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	
	
	
	
}
