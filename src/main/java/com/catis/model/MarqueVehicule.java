package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_marquevehicule")
public class MarqueVehicule extends JournalData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long marqueVehiculeId;
	private String libelle;
	private String description;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "marqueVehicule")
	@JsonIgnore
	Set<Vehicule> vehicule;

	public MarqueVehicule() {

	}

	public MarqueVehicule(Long marqueVehiculeId, String description, Set<Vehicule> vehicule) {

		this.marqueVehiculeId = marqueVehiculeId;
		this.description = description;
		this.vehicule = vehicule;
	}

	public Long getMarqueVehiculeId() {
		return marqueVehiculeId;
	}

	public void setMarqueVehiculeId(Long marqueVehiculeId) {
		this.marqueVehiculeId = marqueVehiculeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Vehicule> getVehicule() {
		return vehicule;
	}

	public void setVehicule(Set<Vehicule> vehicule) {
		this.vehicule = vehicule;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

}
