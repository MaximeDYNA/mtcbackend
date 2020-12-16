package com.catis.model;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.catis.objectTemporaire.CarteGriseReceived;

@Entity
@Table(name="t_energie")
@EntityListeners(AuditingEntityListener.class)
public class Energie extends JournalData{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long energieId;
	private String libelle;
	
	public Energie() {
	
		// TODO Auto-generated constructor stub
	}
	public Energie(CarteGriseReceived cgr) {
		this.energieId = cgr.getEnergieId();
	}
	public Energie(Long energieId, String libelle) {
		this.energieId = energieId;
		this.libelle = libelle;
	}
	public Long getEnergieId() {
		return energieId;
	}
	public void setEnergieId(Long energieId) {
		this.energieId = energieId;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	
	
}
