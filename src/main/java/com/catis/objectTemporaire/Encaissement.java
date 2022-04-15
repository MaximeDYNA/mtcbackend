package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Encaissement {

    private String clientId;
    private String vendeurId;
    private String contactId;
    private int type;
    private String document;
    private double montantTotal;
    private double montantEncaisse;
    private double montantHT;
    private String numeroTicket;
    private String nomclient;
    private String numeroclient;
    private String nomcontacts;
    private String numerocontacts;
    private UUID sessionCaisseId;
    private String lang;
    private List<ProduitVue> produitVue;




    public void setProduitVue(List<ProduitVue> produitVue) {
        this.produitVue = produitVue;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getMontantHT() {
        return montantHT;
    }

    public void setMontantHT(double montantHT) {
        this.montantHT = montantHT;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getNomclient() {
        return nomclient;
    }

    public void setNomclient(String nomclient) {
        this.nomclient = nomclient;
    }

    public String getNumeroclient() {
        return numeroclient;
    }

    public void setNumeroclient(String numeroclient) {
        this.numeroclient = numeroclient;
    }

    public String getNomcontacts() {
        return nomcontacts;
    }

    public void setNomcontacts(String nomcontacts) {
        this.nomcontacts = nomcontacts;
    }

    public String getNumerocontacts() {
        return numerocontacts;
    }

    public void setNumerocontacts(String numerocontacts) {
        this.numerocontacts = numerocontacts;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }
}
