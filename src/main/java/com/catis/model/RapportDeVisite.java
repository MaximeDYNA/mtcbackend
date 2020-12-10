package com.catis.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author AubryYvan
 */
@Entity
public class RapportDeVisite {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Double result;

	private String decision;

	private String codeMessage;

	@ManyToOne(cascade = CascadeType.ALL)
	private Visite visite;

	@ManyToOne(cascade = CascadeType.ALL)
	private Seuil seuil;

	@ManyToOne
	private GieglanFile gieglanFile;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getResult() {
		return result;
	}

	public void setResult(Double result) {
		this.result = result;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

	public String getCodeMessage() {
		return codeMessage;
	}

	public void setCodeMessage(String codeMessage) {
		this.codeMessage = codeMessage;
	}

	public Visite getVisite() {
		return visite;
	}

	public void setVisite(Visite visite) {
		this.visite = visite;
	}

	public Seuil getSeuil() {
		return seuil;
	}

	public void setSeuil(Seuil seuil) {
		this.seuil = seuil;
	}

	public GieglanFile getGieglanFile() {
		return gieglanFile;
	}

	public void setGieglanFile(GieglanFile gieglanFile) {
		this.gieglanFile = gieglanFile;
	}

	public RapportDeVisite(Long id, Double result, String decision, String codeMessage, Visite visite, Seuil seuil,
			GieglanFile gieglanFile) {
		super();
		this.id = id;
		this.result = result;
		this.decision = decision;
		this.codeMessage = codeMessage;
		this.visite = visite;
		this.seuil = seuil;
		this.gieglanFile = gieglanFile;
	}

	public RapportDeVisite() {
		super();
		// TODO Auto-generated constructor stub
	}
}