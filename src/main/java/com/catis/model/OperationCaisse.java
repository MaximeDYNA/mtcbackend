package com.catis.model;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

@Entity
@Table(name = "t_operationdecaisse")
@EntityListeners(AuditingEntityListener.class)
public class OperationCaisse extends JournalData {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long operationDeCaisseId;
	
	private boolean type;
	private double montant;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idCaissierCaisse")
	private CaissierCaisse caissierCaisse;
	
	@ManyToOne
	private Vente vente;
	
	@ManyToOne
	@JoinColumn(name="idTaxe")
	private Taxe taxe;
	
	@ManyToOne
	private SessionCaisse sessionCaisse;
	
	private String numeroTicket;
	
	

	public OperationCaisse() {

	}



	



	public OperationCaisse(long operationDeCaisseId, boolean type, double montant, CaissierCaisse caissierCaisse,
			Vente vente, Taxe taxe, SessionCaisse sessionCaisse, String numeroTicket) {
		super();
		this.operationDeCaisseId = operationDeCaisseId;
		this.type = type;
		this.montant = montant;
		this.caissierCaisse = caissierCaisse;
		this.vente = vente;
		this.taxe = taxe;
		this.sessionCaisse = sessionCaisse;
		this.numeroTicket = numeroTicket;
	}







	public long getOperationDeCaisseId() {
		return operationDeCaisseId;
	}



	public void setOperationDeCaisseId(long operationDeCaisseId) {
		this.operationDeCaisseId = operationDeCaisseId;
	}



	
	public boolean isType() {
		return type;
	}







	public void setType(boolean type) {
		this.type = type;
	}







	public double getMontant() {
		return montant;
	}

	public void setMontant(double montant) {
		this.montant = montant;
	}

	public CaissierCaisse getCaissierCaisse() {
		return caissierCaisse;
	}

	public void setCaissierCaisse(CaissierCaisse caissierCaisse) {
		this.caissierCaisse = caissierCaisse;
	}

	public Vente getVente() {
		return vente;
	}

	public void setVente(Vente vente) {
		this.vente = vente;
	}

	public Taxe getTaxe() {
		return taxe;
	}

	public void setTaxe(Taxe taxe) {
		this.taxe = taxe;
	}









	public String getNumeroTicket() {
		return numeroTicket;
	}










	public void setNumeroTicket(String numeroTicket) {
		this.numeroTicket = numeroTicket;
	}










	public SessionCaisse getSessionCaisse() {
		return sessionCaisse;
	}







	public void setSessionCaisse(SessionCaisse sessionCaisse) {
		this.sessionCaisse = sessionCaisse;
	}

	
	public String getLibelle() {
		if(type) {
			return "Décaissement";
		}
		return "Encaissement";
	}
	
}
