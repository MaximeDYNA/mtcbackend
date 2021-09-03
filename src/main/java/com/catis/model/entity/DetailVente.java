package com.catis.model.entity;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_detailvente")
@Audited
public class DetailVente extends JournalData {
    // table pivot entre produit et vente
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idDetailVente;
    private String reference;
    private double prix;

    @ManyToOne
    private Produit produit;

    @ManyToOne
    @JsonIgnore
    private Vente vente;

    public DetailVente() {

    }

    public DetailVente(long idDetailVente, String reference, Produit produit, Vente vente) {
        this.idDetailVente = idDetailVente;
        this.reference = reference;
        this.produit = produit;
        this.vente = vente;
    }

    public long getIdDetailVente() {
        return idDetailVente;
    }

    public void setIdDetailVente(long idDetailVente) {
        this.idDetailVente = idDetailVente;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Vente getVente() {
        return vente;
    }

    public void setVente(Vente vente) {
        this.vente = vente;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetailVente)) return false;
        DetailVente that = (DetailVente) o;
        return getIdDetailVente() == that.getIdDetailVente();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getIdDetailVente());
    }
}
