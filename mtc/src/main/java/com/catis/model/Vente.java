
package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	private String idVente;
	private double montantTotal;
	
	private String idOrganisation;
	
	@ManyToOne
	@JoinColumn(name="idCaissierCaisse")
	private CaissierCaisse caissierCaisse;
	@ManyToOne
	@JoinColumn(name="idClient")
	private Client client;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="vente")
	@JsonIgnore
	private Set<OperationCaisse> operationCaisse;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="vente")
	@JsonIgnore
	private Set<DetailVente> detailventes;
	
	@ManyToOne(optional = true, fetch = FetchType.LAZY)//id utilisateur optionel
	@JoinColumn(name="idVendeur")
	private Vendeur vendeur;
	
	public Vente() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Vente(String idVente, double montantTotal, String idOrganisation,
			CaissierCaisse caissierCaisse, Client client, Set<OperationCaisse> operationCaisse,
			Set<DetailVente> detailventes, Vendeur vendeur) {
		super();
		this.idVente = idVente;
		this.montantTotal = montantTotal;
	
		this.idOrganisation = idOrganisation;
		this.caissierCaisse = caissierCaisse;
		this.client = client;
		this.operationCaisse = operationCaisse;
		this.detailventes = detailventes;
		this.vendeur = vendeur;
	}

	public Set<OperationCaisse> getOperationCaisse() {
		return operationCaisse;
	}


	public void setOperationCaisse(Set<OperationCaisse> operationCaisse) {
		this.operationCaisse = operationCaisse;
	}


	public String getIdVente() {
		return idVente;
	}


	public void setIdVente(String idVente) {
		this.idVente = idVente;
	}


	public double getMontantTotal() {
		return montantTotal;
	}


	public void setMontantTotal(double montantTotal) {
		this.montantTotal = montantTotal;
	}


	


	


	public String getIdOrganisation() {
		return idOrganisation;
	}


	public void setIdOrganisation(String idOrganisation) {
		this.idOrganisation = idOrganisation;
	}


	public CaissierCaisse getCaissierCaisse() {
		return caissierCaisse;
	}


	public void setCaissierCaisse(CaissierCaisse caissierCaisse) {
		this.caissierCaisse = caissierCaisse;
	}


	public Client getClient() {
		return client;
	}


	public void setClient(Client client) {
		this.client = client;
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
	
	
}
