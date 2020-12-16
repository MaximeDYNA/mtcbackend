package com.catis.model;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

@Entity
@Table(name="t_taxeproduit")
@EntityListeners(AuditingEntityListener.class)
public class TaxeProduit extends JournalData {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long TaxeProduitId;
	@ManyToOne
	private Taxe taxe;
	@ManyToOne
	private Produit produit;
	
	public TaxeProduit(Taxe taxe, Produit produit) {
		
		this.taxe = taxe;
		this.produit = produit;
	}
	
	public TaxeProduit() {
		
		// TODO Auto-generated constructor stub
	}

	public Taxe getTaxe() {
		return taxe;
	}
	public void setTaxe(Taxe taxe) {
		this.taxe = taxe;
	}
	public Produit getProduit() {
		return produit;
	}
	public void setProduit(Produit produit) {
		this.produit = produit;
	}
	public Long getTaxeProduitId() {
		return TaxeProduitId;
	}
	public void setTaxeProduitId(Long taxeProduitId) {
		TaxeProduitId = taxeProduitId;
	}
	
	

}
