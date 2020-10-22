
package com.catis.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_vente")

public class Vente {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idVente;
	private double montantTotal;

	@ManyToOne
	private Client client;
	
	@ManyToOne
	private Vendeur vendeur;
	
	@ManyToOne
	private Contact contact;
	
	@ManyToOne
	private SessionCaisse sessionCaisse;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="vente")
	@JsonIgnore
	private Set<OperationCaisse> operationCaisse;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="vente")
	@JsonIgnore
	private Set<DetailVente> detailventes;
	
	
	
	
	public Vente() {
		super();
		// TODO Auto-generated constructor stub
	}




	public Vente(Long idVente, double montantTotal, Set<OperationCaisse> operationCaisse,
			Set<DetailVente> detailventes) {
		super();
		this.idVente = idVente;
		this.montantTotal = montantTotal;
		this.operationCaisse = operationCaisse;
		this.detailventes = detailventes;
	}




	public Set<OperationCaisse> getOperationCaisse() {
		return operationCaisse;
	}


	public void setOperationCaisse(Set<OperationCaisse> operationCaisse) {
		this.operationCaisse = operationCaisse;
	}




	public Long getIdVente() {
		return idVente;
	}




	public Client getClient() {
		return client;
	}




	public void setClient(Client client) {
		this.client = client;
	}




	public void setIdVente(long idVente) {
		this.idVente = idVente;
	}




	public double getMontantTotal() {
		return montantTotal;
	}


	public void setMontantTotal(double montantTotal) {
		this.montantTotal = montantTotal;
	}


	public Set<DetailVente> getDetailventes() {
		return detailventes;
	}

	public void setDetailventes(Set<DetailVente> detailventes) {
		this.detailventes = detailventes;
	}




	public Vendeur getVendeur() {
		return vendeur;
	}




	public void setVendeur(Vendeur vendeur) {
		this.vendeur = vendeur;
	}




	public Contact getContact() {
		return contact;
	}




	public void setContact(Contact contact) {
		this.contact = contact;
	}




	public SessionCaisse getSessionCaisse() {
		return sessionCaisse;
	}




	public void setSessionCaisse(SessionCaisse sessionCaisse) {
		this.sessionCaisse = sessionCaisse;
	}



}
