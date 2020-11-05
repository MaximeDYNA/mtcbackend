package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "t_vendeur")
public class Vendeur {
	//entité capable d'avoir des commisions sur une vente
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long vendeurId;
	
	private String description;

	@ManyToOne
	private Partenaire partenaire;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="vendeur")
	@JsonIgnore
	private Set<Vente> ventes;
	
	public Vendeur() {
	}

	public Vendeur(long vendeurId, String description, Partenaire partenaire) {
		super();
		this.vendeurId = vendeurId;
		this.description = description;
		this.partenaire = partenaire;
	}

	public Long getVendeurId() {
		return vendeurId;
	}


	public void setVendeurId(Long vendeurId) {
		this.vendeurId = vendeurId;
	}

	public Partenaire getPartenaire() {
		return partenaire;
	}

	public void setPartenaire(Partenaire partenaire) {
		this.partenaire = partenaire;
	}

	public Set<Vente> getVentes() {
		return ventes;
	}

	public void setVentes(Set<Vente> ventes) {
		this.ventes = ventes;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}