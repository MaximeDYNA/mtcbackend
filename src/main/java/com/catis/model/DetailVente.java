package com.catis.model;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_detailvente")
public class DetailVente extends JournalData {
	// table pivot entre produit et vente
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idDetailVente;
	private String reference;

	@ManyToOne
	@JoinColumn(name = "idProduit")
	private Produit produit;

	@ManyToOne
	private Vente vente;

	public DetailVente() {

	}

	public DetailVente(long idDetailVente, String reference, Produit produit, Vente vente) {
		this.idDetailVente = idDetailVente;
		this.reference = reference;
		this.produit = produit;
		this.vente = vente;
	}

	public long getIdDetailVente() {
		return idDetailVente;
	}

	public void setIdDetailVente(long idDetailVente) {
		this.idDetailVente = idDetailVente;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Produit getProduit() {
		return produit;
	}

	public void setProduit(Produit produit) {
		this.produit = produit;
	}

	public Vente getVente() {
		return vente;
	}

	public void setVente(Vente vente) {
		this.vente = vente;
	}

}
