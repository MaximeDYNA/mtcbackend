package com.catis.model;

import java.util.Date;
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
@Table(name="t_sessioncaisse")
public class SessionCaisse {
	
	@Id
	private String session_caisse_id;
	
	@ManyToOne
	@JoinColumn(name="caisse_id")
	private Caisse caisse;
	
	@ManyToOne
	@JoinColumn(name="caissier_id")
	private Caissier caissier;
	
	private Date dateHeureOuverture;
	private Date dateHeureFermeture;
	private String idOrganisation;
	private double montantOuverture;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="sessionCaisse")
	@JsonIgnore
	Set <OperationCaisse> operationCaisse;
	
	public SessionCaisse() {
	
	}





	public SessionCaisse(String session_caisse_id, Caisse caisse, Caissier caissier, Date dateHeureOuverture,
			Date dateHeureFermeture, String idOrganisation, double montantOuverture,
			Set<OperationCaisse> operationCaisse) {
		super();
		this.session_caisse_id = session_caisse_id;
		this.caisse = caisse;
		this.caissier = caissier;
		this.dateHeureOuverture = dateHeureOuverture;
		this.dateHeureFermeture = dateHeureFermeture;
		this.idOrganisation = idOrganisation;
		this.montantOuverture = montantOuverture;
		this.operationCaisse = operationCaisse;
	}





	public String getIdSessionCaisse() {
		return session_caisse_id;
	}

	public void setIdSessionCaisse(String idSessionCaisse) {
		this.session_caisse_id = idSessionCaisse;
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





	public Date getDateHeureOuverture() {
		return dateHeureOuverture;
	}

	public void setDateHeureOuverture(Date dateHeureOuverture) {
		this.dateHeureOuverture = dateHeureOuverture;
	}

	public Date getDateHeureFermeture() {
		return dateHeureFermeture;
	}

	public void setDateHeureFermeture(Date dateHeureFermeture) {
		this.dateHeureFermeture = dateHeureFermeture;
	}

	public String getIdOrganisation() {
		return idOrganisation;
	}

	public void setIdOrganisation(String idOrganisation) {
		this.idOrganisation = idOrganisation;
	}

	public double getMontantOuverture() {
		return montantOuverture;
	}

	public void setMontantOuverture(double montantOuverture) {
		this.montantOuverture = montantOuverture;
	}



	public Set<OperationCaisse> getOperationCaisse() {
		return operationCaisse;
	}



	public void setOperationCaisse(Set<OperationCaisse> operationCaisse) {
		this.operationCaisse = operationCaisse;
	}



	public String getSession_caisse_id() {
		return session_caisse_id;
	}



	public void setSession_caisse_id(String session_caisse_id) {
		this.session_caisse_id = session_caisse_id;
	}
	
	
	
}
