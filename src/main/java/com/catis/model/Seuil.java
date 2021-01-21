package com.catis.model;

import java.util.Set;

import javax.persistence.*;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.catis.model.configuration.JournalData;

/**
 * @author AubryYvan
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Seuil extends JournalData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private double value;

	private String operande;

	private String codeMessage;

	private boolean decision;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "seuil")
	private Set<RapportDeVisite> rapportDeVisites;

	@ManyToOne
	private Formule formule;

	@ManyToMany
	private Set<Lexique> lexiques;

	public Seuil() {
		// TODO Auto-generated constructor stub
	}

	public Seuil(Long id, double value, String operande, String codeMessage, boolean decision, Set<RapportDeVisite> rapportDeVisites, Formule formule, Set<Lexique> lexiques) {
		this.id = id;
		this.value = value;
		this.operande = operande;
		this.codeMessage = codeMessage;
		this.decision = decision;
		this.rapportDeVisites = rapportDeVisites;
		this.formule = formule;
		this.lexiques = lexiques;
	}

	public Set<Lexique> getLexiques() {
		return lexiques;
	}

	public void setLexiques(Set<Lexique> lexiques) {
		this.lexiques = lexiques;
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

	public String getCodeMessage() {
		return codeMessage;
	}

	public void setCodeMessage(String codeMessage) {
		this.codeMessage = codeMessage;
	}

	public String getOperande() {
		return operande;
	}

	public void setOperande(String operande) {
		this.operande = operande;
	}

	public boolean isDecision() {
		return decision;
	}

	public void setDecision(boolean decision) {
		this.decision = decision;
	}

	public Set<RapportDeVisite> getRapportDeVisites() {
		return rapportDeVisites;
	}

	public void setRapportDeVisites(Set<RapportDeVisite> rapportDeVisites) {
		this.rapportDeVisites = rapportDeVisites;
	}


	public Formule getFormule() {
		return formule;
	}

	public void setFormule(Formule formule) {
		this.formule = formule;
	}
}