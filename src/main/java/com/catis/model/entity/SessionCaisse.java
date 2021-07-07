package com.catis.model.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_sessioncaisse")
@EntityListeners(AuditingEntityListener.class)
@Audited
public class SessionCaisse extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionCaisseId;

    private Date dateHeureOuverture;

    private Date dateHeureFermeture;


    @ManyToOne
    private Caissier caissier;

    private double montantOuverture;

    private double montantfermeture;

    private boolean active;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sessionCaisse")
    @JsonIgnore
    Set<OperationCaisse> operationCaisse;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sessionCaisse")
    @JsonIgnore
    private Set<Vente> vente;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sessionCaisse")
    @JsonIgnore
    Set<Hold> holds;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sessionCaisse")
    @JsonIgnore
    private Set<Posales> posales;

    public SessionCaisse() {

    }

    public SessionCaisse(Long sessionCaisseId, Date dateHeureOuverture, Date dateHeureFermeture,
                         Organisation organisationId, Utilisateur user, double montantOuverture, boolean active,
                         Set<OperationCaisse> operationCaisse, Set<Vente> vente, Set<Hold> holds, Set<Posales> posales) {

        this.sessionCaisseId = sessionCaisseId;
        this.dateHeureOuverture = dateHeureOuverture;
        this.dateHeureFermeture = dateHeureFermeture;


        this.montantOuverture = montantOuverture;
        this.active = active;
        this.operationCaisse = operationCaisse;
        this.vente = vente;
        this.holds = holds;
        this.posales = posales;
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

    public Long getSessionCaisseId() {
        return sessionCaisseId;
    }

    public void setSessionCaisseId(Long sessionCaisseId) {
        this.sessionCaisseId = sessionCaisseId;
    }

    public Set<Vente> getVente() {
        return vente;
    }

    public void setVente(Set<Vente> vente) {
        this.vente = vente;
    }

    public Set<Hold> getHolds() {
        return holds;
    }

    public void setHolds(Set<Hold> holds) {
        this.holds = holds;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Posales> getPosales() {
        return posales;
    }

    public void setPosales(Set<Posales> posales) {
        this.posales = posales;
    }

    public double getMontantfermeture() {
        return montantfermeture;
    }

    public void setMontantfermeture(double montantfermeture) {
        this.montantfermeture = montantfermeture;
    }

}