package com.catis.model;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class VerbalProcess extends JournalData {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String reference;

	private String signature; //qrc

	@OneToOne
	private Visite visite;

	
	public VerbalProcess() {
	
		// TODO Auto-generated constructor stub
	}


	public VerbalProcess(Long id, String reference, String signature, Visite visite) {
		
		this.id = id;
		this.reference = reference;
		this.signature = signature;
		this.visite = visite;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getReference() {
		return reference;
	}


	public void setReference(String reference) {
		this.reference = reference;
	}


	public String getSignature() {
		return signature;
	}


	public void setSignature(String signature) {
		this.signature = signature;
	}


	public Visite getVisite() {
		return visite;
	}


	public void setVisite(Visite visite) {
		this.visite = visite;
	}
	
	
}