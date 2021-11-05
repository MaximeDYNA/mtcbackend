package com.catis.objectTemporaire;

import java.util.List;

public class Encaissement {

    private long clientId;
    private long vendeurId;
    private long contactId;
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
    private long sessionCaisseId;
    private String lang;
    private List<ProduitVue> produitVue;


    public Encaissement(long clientId, long vendeurId, long contactid, double montantTotal, double montantEncaisse,
                        String numeroTicket, long sessionCaisseId, List<ProduitVue> produitVue) {
        super();
        this.clientId = clientId;
        this.vendeurId = vendeurId;
        this.contactId = contactid;
        this.montantTotal = montantTotal;
        this.montantEncaisse = montantEncaisse;
        this.numeroTicket = numeroTicket;
        this.sessionCaisseId = sessionCaisseId;
        this.produitVue = produitVue;
    }

    public Encaissement() {
        super();
        // TODO Auto-generated constructor stub
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getVendeurId() {
        return vendeurId;
    }

    public void setVendeurId(long vendeurId) {
        this.vendeurId = vendeurId;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public double getMontantEncaisse() {
        return montantEncaisse;
    }

    public void setMontantEncaisse(double montantEncaisse) {
        this.montantEncaisse = montantEncaisse;
    }

    public String getNumeroTicket() {
        return numeroTicket;
    }

    public void setNumeroTicket(String numeroTicket) {
        this.numeroTicket = numeroTicket;
    }

    public long getSessionCaisseId() {
        return sessionCaisseId;
    }

    public void setSessionCaisseId(long sessionCaisseId) {
        this.sessionCaisseId = sessionCaisseId;
    }

    public List<ProduitVue> getProduitVue() {
        return produitVue;
    }

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
