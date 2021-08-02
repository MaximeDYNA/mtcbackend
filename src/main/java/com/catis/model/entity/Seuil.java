package com.catis.model.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

/**
 * @author AubryYvan
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
public class Seuil extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double value;

    private String operande;

    private String codeMessage;

    private boolean decision;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seuil")
    @JsonIgnore
    private Set<RapportDeVisite> rapportDeVisites;

    @ManyToOne
    private Lexique lexique;

    @ManyToOne
    private Formule formule;

    @ManyToMany(mappedBy = "seuils")
    private Set<Produit> produits = new HashSet<>();

    public Seuil() {

        // TODO Auto-generated constructor stub
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


    public Seuil(Long id, double value, String operande, String codeMessage, boolean decision,
                 Set<RapportDeVisite> rapportDeVisites, Classification classification, Formule formule) {
        super();
        this.id = id;
        this.value = value;
        this.operande = operande;
        this.codeMessage = codeMessage;
        this.decision = decision;
        this.rapportDeVisites = rapportDeVisites;

        this.formule = formule;
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


    public Lexique getLexique() {
        return lexique;
    }


    public void setLexique(Lexique lexique) {
        this.lexique = lexique;
    }
}