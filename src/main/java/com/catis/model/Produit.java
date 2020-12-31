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
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.catis.objectTemporaire.ProduitView;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_produit")
@EntityListeners(AuditingEntityListener.class)
public class Produit extends JournalData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long produitId;
	private String libelle;
	private String description;
	private double prix;
	private int delaiValidite;
	private String img;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "produit")
	@JsonIgnore
	private Set<DetailVente> detailVente;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "produit")
	@JsonIgnore
	private Set<CarteGrise> carteGrise;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "produit")
	@JsonIgnore
	private Set<TaxeProduit> taxeProduit;

	@OneToMany(mappedBy = "produit")
	@JsonIgnore
	private Set<Posales> posales;

	@ManyToOne
	private CategorieProduit categorieProduit;

	@ManyToOne
	private CategorieVehicule categorieVehicule;

	public CategorieVehicule getCategorieVehicule() {
		return categorieVehicule;
	}

	public void setCategorieVehicule(CategorieVehicule categorieVehicule) {
		this.categorieVehicule = categorieVehicule;
	}

	public Produit() {

		// TODO Auto-generated constructor stub
	}

	public Produit(Long produitId, String libelle, String description, double prix, int delaiValidite, String img,
			Set<DetailVente> detailVente, Set<CarteGrise> carteGrise, Set<TaxeProduit> taxeProduit,
			Set<Posales> posales, CategorieProduit categorieProduit) {

		this.produitId = produitId;
		this.libelle = libelle;
		this.description = description;
		this.prix = prix;
		this.delaiValidite = delaiValidite;
		this.img = img;
		this.detailVente = detailVente;
		this.carteGrise = carteGrise;
		this.taxeProduit = taxeProduit;
		this.posales = posales;
		this.categorieProduit = categorieProduit;
	}

	public Long getProduitId() {
		return produitId;
	}

	public void setProduit_id(Long produitId) {
		this.produitId = produitId;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		this.prix = prix;
	}

	public int getDelaiValidite() {
		return delaiValidite;
	}

	public void setDelaiValidite(int delaiValidite) {
		this.delaiValidite = delaiValidite;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Set<DetailVente> getDetailVente() {
		return detailVente;
	}

	public void setDetailVente(Set<DetailVente> detailVente) {
		this.detailVente = detailVente;
	}

	public Set<CarteGrise> getCarteGrise() {
		return carteGrise;
	}

	public void setCarteGrise(Set<CarteGrise> carteGrise) {
		this.carteGrise = carteGrise;
	}

	public Set<TaxeProduit> getTaxeProduit() {
		return taxeProduit;
	}

	public void setTaxeProduit(Set<TaxeProduit> taxeProduit) {
		this.taxeProduit = taxeProduit;
	}

	public Set<Posales> getPosales() {
		return posales;
	}

	public void setPosales(Set<Posales> posales) {
		this.posales = posales;
	}

	public CategorieProduit getCategorieProduit() {
		return categorieProduit;
	}

	public void setCategorieProduit(CategorieProduit categorieProduit) {
		this.categorieProduit = categorieProduit;
	}

}
