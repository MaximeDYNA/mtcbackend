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
@Table(name = "t_caissiercaisse")
public class CaissierCaisse {
	@Id
	private String idCaissierCaisse;
	private String idOrganisation;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idCaisse")
	private Caisse caisse;
	
	@ManyToOne
	@JoinColumn(name="idCaissier")
	private Caissier caissier;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="caissierCaisse")
	@JsonIgnore
	private Set<OperationCaisse> operationCaisse;
	
	

	
	public CaissierCaisse() {
		
	}


	public CaissierCaisse(String idCaissierCaisse, Caisse caisse, Caissier caissier,
			Set<OperationCaisse> operationCaisse, String idOrganisation) {
		super();
		this.idCaissierCaisse = idCaissierCaisse;
		this.caisse = caisse;
		this.caissier = caissier;
		this.operationCaisse = operationCaisse;
		this.idOrganisation = idOrganisation;
	}


	public String getIdCaissierCaisse() {
		return idCaissierCaisse;
	}

	public void setIdCaissierCaisse(String idCaissierCaisse) {
		this.idCaissierCaisse = idCaissierCaisse;
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

	public String getIdOrganisation() {
		return idOrganisation;
	}

	public void setIdOrganisation(String idOrganisation) {
		this.idOrganisation = idOrganisation;
	}


	public Set<OperationCaisse> getOperationCaisse() {
		return operationCaisse;
	}


	public void setOperationCaisse(Set<OperationCaisse> operationCaisse) {
		this.operationCaisse = operationCaisse;
	}


	
}
