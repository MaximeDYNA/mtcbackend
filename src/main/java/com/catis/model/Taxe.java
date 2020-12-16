package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="t_taxe")
@EntityListeners(AuditingEntityListener.class)
public class Taxe extends JournalData{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long taxeId;
	
	private String nom;
	private String description;
	private double valeur;
	
	private boolean incluse;
	
	@OneToMany(mappedBy="taxe")
	@JsonIgnore
	private Set<TaxeProduit> taxeProduit;

	public Taxe(Long taxeId, String nom, String description, double valeur, Set<TaxeProduit> taxeProduit) {
		
		this.taxeId = taxeId;
		this.nom = nom;
		this.description = description;
		this.valeur = valeur;
		this.taxeProduit = taxeProduit;
	}

	public Taxe() {

	}

	public Long getTaxeId() {
		return taxeId;
	}


	public void setTaxeId(Long taxeId) {
		this.taxeId = taxeId;
	}

	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public double getValeur() {
		return valeur;
	}

	public void setValeur(double valeur) {
		this.valeur = valeur;
	}

	public Set<TaxeProduit> getTaxeProduit() {
		return taxeProduit;
	}

	public void setTaxeProduit(Set<TaxeProduit> taxeProduit) {
		this.taxeProduit = taxeProduit;
	}



	public boolean isIncluse() {
		return incluse;
	}



	public void setIncluse(boolean incluse) {
		this.incluse = incluse;
	}
	
	
}
