package com.catis.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_operationdecaisse")
public class OperationCaisse {
	@Id
	private String idOperationDeCaisse;
	private String libelle;
	private double montant;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idCaissierCaisse")
	private CaissierCaisse caissierCaisse;
	
	@ManyToOne
	@JoinColumn(name="idVente")
	private Vente vente;
	
	@ManyToOne
	@JoinColumn(name="idTaxe")
	private Taxe taxe;
	
	private String idOrganisation;
	
	

	public OperationCaisse() {

	}

	

	public OperationCaisse(String idOperationDeCaisse, String libelle, double montant, CaissierCaisse caissierCaisse,
			Vente vente, Taxe taxe, String idOrganisation) {
		super();
		this.idOperationDeCaisse = idOperationDeCaisse;
		this.libelle = libelle;
		this.montant = montant;
		this.caissierCaisse = caissierCaisse;
		this.vente = vente;
		this.taxe = taxe;
		this.idOrganisation = idOrganisation;
	}



	public String getIdOperationDeCaisse() {
		return idOperationDeCaisse;
	}

	public void setIdOperationDeCaisse(String idOperationDeCaisse) {
		this.idOperationDeCaisse = idOperationDeCaisse;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
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



	public String getIdOrganisation() {
		return idOrganisation;
	}



	public void setIdOrganisation(String idOrganisation) {
		this.idOrganisation = idOrganisation;
	}

	
	
	
}
