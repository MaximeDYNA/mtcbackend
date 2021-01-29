package com.catis.model.sqlresponse;

public class ClientPartenaire {

    private long clientId;
    private String nom;
    private String prenom;
    private String telephone;

    public ClientPartenaire(long clientId, String nom, String prenom, String telephone) {
        super();
        this.clientId = clientId;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

}
