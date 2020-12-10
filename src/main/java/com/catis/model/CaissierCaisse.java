package com.catis.model;



import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import javax.persistence.Table;


@Entity
@Table(name = "t_caissiercaisse")
public class CaissierCaisse {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long caissier_caisse_id;
	
	private String idOrganisation;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="caisse_id")
	private Caisse caisse;
	
	@ManyToOne
	@JoinColumn(name="caissier_id")
	private Caissier caissier;
	
	
	
	public CaissierCaisse() {
		
	}

	public CaissierCaisse(Long caissier_caisse_id, String idOrganisation, Caisse caisse, Caissier caissier) {
		super();
		this.caissier_caisse_id = caissier_caisse_id;
		this.idOrganisation = idOrganisation;
		this.caisse = caisse;
		this.caissier = caissier;
		
	}

	public Long getCaissier_caisse_id() {
		return caissier_caisse_id;
	}

	public void setCaissier_caisse_id(Long caissier_caisse_id) {
		this.caissier_caisse_id = caissier_caisse_id;
	}

	public String getIdOrganisation() {
		return idOrganisation;
	}

	public void setIdOrganisation(String idOrganisation) {
		this.idOrganisation = idOrganisation;
	}

	public Caisse getCaisse() {
		return caisse;
	}

	public void setCaisse(Caisse caisse) {
		this.caisse = caisse;
	}

	public Caissier getCaissier() {
		return caissier;
	}

	public void setCaissier(Caissier caissier) {
		this.caissier = caissier;
	}

	

	
}
