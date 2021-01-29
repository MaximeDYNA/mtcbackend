package com.catis.objectTemporaire;

import java.util.Date;
import java.util.List;

import com.catis.model.Client;
import com.catis.model.Partenaire;

public class ClientPartenaire {
    private long clientId;
    private long contactId;
    ;
    private long vendeurId;
    private String variants;
    private String nom;
    private String email;
    private String prenom;
    private String dateNaiss; // date de naissance
    private String lieuDeNaiss; // lieu de naissance
    private String passport;
    private String permiDeConduire;
    private String cni;
    private String telephone;
    private Partenaire partenaire;


    public ClientPartenaire(long clientId, long contactId, long vendeurId, String variants, String nom, String email,
                            String prenom, String dateNaiss, String lieuDeNaiss, String passport, String permiDeConduire, String cni,
                            String telephone, Partenaire partenaire) {
        super();
        this.clientId = clientId;
        this.contactId = contactId;
        this.vendeurId = vendeurId;
        this.variants = variants;
        this.nom = nom;
        this.email = email;
        this.prenom = prenom;
        this.dateNaiss = dateNaiss;
        this.lieuDeNaiss = lieuDeNaiss;
        this.passport = passport;
        this.permiDeConduire = permiDeConduire;
        this.cni = cni;
        this.telephone = telephone;
        this.partenaire = partenaire;
    }

    public ClientPartenaire() {
        super();
        // TODO Auto-generated constructor stub
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getVariants() {
        return variants;
    }

    public void setVariants(String variants) {
        this.variants = variants;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getDateNaiss() {
        return dateNaiss;
    }

    public void setDateNaiss(String dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public String getLieuDeNaiss() {
        return lieuDeNaiss;
    }

    public void setLieuDeNaiss(String lieuDeNaiss) {
        this.lieuDeNaiss = lieuDeNaiss;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getPermiDeConduire() {
        return permiDeConduire;
    }

    public void setPermiDeConduire(String permiDeConduire) {
        this.permiDeConduire = permiDeConduire;
    }

    public String getCni() {
        return cni;
    }

    public void setCni(String cni) {
        this.cni = cni;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Partenaire getPartenaire() {
        return partenaire;
    }

    public void setPartenaire(Partenaire partenaire) {
        this.partenaire = partenaire;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public long getVendeurId() {
        return vendeurId;
    }

    public void setVendeurId(long vendeurId) {
        this.vendeurId = vendeurId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
