package com.catis.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * @author AubryYvan
 */
@Entity
public class Seuil {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private double value;

	private char operande;

	private String codeMessage;

	private String decision;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "seuil")
	private Set<RapportDeVisite> rapportDeVisites;

	@ManyToOne(cascade = CascadeType.ALL)
	private Classification classification;

	@ManyToOne
	private Formule formule;

	public Seuil() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Seuil(Long id, double value, char operande, String codeMessage, String decision,
			Set<RapportDeVisite> rapportDeVisites, Classification classification, Formule formule) {
		super();
		this.id = id;
		this.value = value;
		this.operande = operande;
		this.codeMessage = codeMessage;
		this.decision = decision;
		this.rapportDeVisites = rapportDeVisites;
		this.classification = classification;
		this.formule = formule;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public char getOperande() {
		return operande;
	}

	public void setOperande(char operande) {
		this.operande = operande;
	}

	public String getCodeMessage() {
		return codeMessage;
	}

	public void setCodeMessage(String codeMessage) {
		this.codeMessage = codeMessage;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

	public Set<RapportDeVisite> getRapportDeVisites() {
		return rapportDeVisites;
	}

	public void setRapportDeVisites(Set<RapportDeVisite> rapportDeVisites) {
		this.rapportDeVisites = rapportDeVisites;
	}

	public Classification getClassification() {
		return classification;
	}

	public void setClassification(Classification classification) {
		this.classification = classification;
	}

	public Formule getFormule() {
		return formule;
	}

	public void setFormule(Formule formule) {
		this.formule = formule;
	}
}