
package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_vente")

public class Vente extends JournalData{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idVente;
	private double montantTotal;
	private double montantHT;
	private int statut;
	@ManyToOne
	private Client client;
	
	@ManyToOne
	private Vendeur vendeur;
	
	@ManyToOne
	private Contact contact;
	
	@OneToOne
	private Visite visite;
	@ManyToOne
	private SessionCaisse sessionCaisse;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="vente")
	@JsonIgnore
	private Set<OperationCaisse> operationCaisse;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="vente")
	@JsonIgnore
	private Set<DetailVente> detailventes;
	
	private String numFacture;
	
	
	public Vente() {
		super();
		// TODO Auto-generated constructor stub
	}




	




	public Vente(Long idVente, double montantTotal, double montantHT, Client client, Vendeur vendeur,
			@NotEmpty @NotNull Contact contact, SessionCaisse sessionCaisse, Set<OperationCaisse> operationCaisse,
			Set<DetailVente> detailventes, String numFacture) {
		super();
		this.idVente = idVente;
		this.montantTotal = montantTotal;
		this.montantHT = montantHT;
		this.client = client;
		this.vendeur = vendeur;
		this.contact = contact;
		this.sessionCaisse = sessionCaisse;
		this.operationCaisse = operationCaisse;
		this.detailventes = detailventes;
		this.numFacture = numFacture;
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




	public String getNumFacture() {
		return numFacture;
	}




	public void setNumFacture(String numFacture) {
		this.numFacture = numFacture;
	}




	public void setIdVente(Long idVente) {
		this.idVente = idVente;
	}









	public double getMontantHT() {
		return montantHT;
	}









	public void setMontantHT(double montantHT) {
		this.montantHT = montantHT;
	}









	public Visite getVisite() {
		return visite;
	}









	public void setVisite(Visite visite) {
		this.visite = visite;
	}

	public int getStatut() {
		return statut;
	}

	public void setStatut(int statut) {
		this.statut = statut;
	}

	public String getLibelleStatut() {
		if(this.statut ==0)
			return "payé";
		else if(this.statut ==1) {
			return "partiellement payé";
		}
		else if(this.statut == 2) {
			return "impayé";
		}
		else
			return "statut erroné";
	}

}
