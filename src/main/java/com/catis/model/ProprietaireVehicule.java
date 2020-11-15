package com.catis.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="t_proprietairevehicule")
public class ProprietaireVehicule extends JournalData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long proprietaireVehiculeId;
	private String idOrganisation;
	
	@ManyToOne
	private Partenaire partenaire;
	private String description;
	
	public ProprietaireVehicule() {

	}



	



	public ProprietaireVehicule(Long proprietaireVehiculeId, String idOrganisation, Partenaire partenaire,
			String description) {
		super();
		this.proprietaireVehiculeId = proprietaireVehiculeId;
		this.idOrganisation = idOrganisation;
		this.partenaire = partenaire;
		this.description = description;
	}







	public Long getProprietaireVehiculeId() {
		return proprietaireVehiculeId;
	}







	public void setProprietaireVehiculeId(Long proprietaireVehiculeId) {
		this.proprietaireVehiculeId = proprietaireVehiculeId;
	}







	public String getIdOrganisation() {
		return idOrganisation;
	}

	public void setIdOrganisation(String idOrganisation) {
		this.idOrganisation = idOrganisation;
	}

	public Partenaire getPartenaire() {
		return partenaire;
	}

	public void setPartenaire(Partenaire partenaire) {
		this.partenaire = partenaire;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
