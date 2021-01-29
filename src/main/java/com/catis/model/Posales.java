package com.catis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

@Entity
@Table(name = "t_posales")
@EntityListeners(AuditingEntityListener.class)
public class Posales extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long posalesId;

    @ManyToOne
    private Produit produit;

    private boolean status;

    @ManyToOne
    private SessionCaisse sessionCaisse;

    @ManyToOne
    private Hold hold;

    @NotNull
    @NotEmpty(message = "La référence ne peut être vide")
    @Column(unique = true, nullable = false)
    private String reference;

    public Posales() {
    }

    public Posales(Long posalesId, Produit produit, boolean status, SessionCaisse sessionCaisse, Hold hold,
                   String reference) {

        this.posalesId = posalesId;
        this.produit = produit;
        this.status = status;
        this.sessionCaisse = sessionCaisse;
        this.hold = hold;
        this.reference = reference;
    }

    public Long getPosalesId() {
        return posalesId;
    }

    public void setPosalesId(Long posalesId) {
        this.posalesId = posalesId;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @JsonIgnore
    public SessionCaisse getSessionCaisse() {
        return sessionCaisse;
    }

    public void setSessionCaisse(SessionCaisse sessionCaisse) {
        this.sessionCaisse = sessionCaisse;
    }

    public Hold getHold() {
        return hold;
    }

    public void setHold(Hold hold) {
        this.hold = hold;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

}
