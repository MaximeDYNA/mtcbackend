package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_vendeur")
public class Vendeur {
	//entité capable d'avoir des commisions sur une vente
	@Id
	private String idVendeur;
	private String description;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="vendeur")
	@JsonIgnore
	private Set<Vente> ventes;
	
	
	public Vendeur() {
	}

	public Vendeur(String idVendeur, String description, Set<Vente> ventes) {
		super();
		this.idVendeur = idVendeur;
		this.description = description;
		this.ventes = ventes;
	}

	public String getIdVendeur() {
		return idVendeur;
	}

	public void setIdVendeur(String idVendeur) {
		this.idVendeur = idVendeur;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Vente> getVentes() {
		return ventes;
	}

	public void setVentes(Set<Vente> ventes) {
		this.ventes = ventes;
	}
	
	
}
