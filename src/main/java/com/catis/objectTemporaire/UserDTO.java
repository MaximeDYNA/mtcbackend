package com.catis.objectTemporaire;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class UserDTO {
    private String id;
    private String nom;
    private String prenom;
    private String login;
    private String email;
    private String adresse;
    private String tel;
    private UUID organisanionId;
    private Set<String> roles;


    public UserDTO() {
        super();
        // TODO Auto-generated constructor stub
    }


    public UserDTO(String nom, String prenom, String email, String adresse, String tel, UUID organisanionId,
                   Set<String> roles) {
        super();
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.adresse = adresse;
        this.tel = tel;
        this.organisanionId = organisanionId;
        this.roles = roles;
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


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getAdresse() {
        return adresse;
    }


    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }


    public String getTel() {
        return tel;
    }


    public void setTel(String tel) {
        this.tel = tel;
    }





    public Set<String> getRoles() {
        return roles;
    }


    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }


    public String getLogin() {
        return login;
    }


    public void setLogin(String login) {
        this.login = login;
    }


    public String getId() {
        return id;
    }

    public UUID getOrganisanionId() {
        return organisanionId;
    }

    public void setOrganisanionId(UUID organisanionId) {
        this.organisanionId = organisanionId;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", adresse='" + adresse + '\'' +
                ", tel='" + tel + '\'' +
                ", organisanionId='" + organisanionId + '\'' +
                ", roles=" + roles +
                '}';
    }
}
