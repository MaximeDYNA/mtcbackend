package com.catis.model.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import com.catis.objectTemporaire.CaissierPOJO;
import com.catis.objectTemporaire.ProprietairePOJO;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_partenaire")
@Audited
public class Partenaire extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partenaireId;
    @NotEmpty

    private String nom;

    private String prenom;
    @Column(nullable = true)

    private Date dateNaiss; // date de naissance

    private String lieuDeNaiss; // lieu de naissance

    private String passport;

    private String permiDeConduire;

    private String cni;

    private String telephone;

    private String email;
    @Column(unique = true)

    private String numeroContribuable;

    @OneToOne(mappedBy = "partenaire")
    @JsonIgnore
    private Client client;

    @OneToOne(mappedBy = "partenaire")
    @JsonIgnore
    private Contact contact;

    @OneToOne(mappedBy = "partenaire")
    @JsonIgnore
    private ProprietaireVehicule proprietaireVehicule;

    @OneToOne(mappedBy = "partenaire")
    @JsonIgnore
    private Controleur controleur;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "partenaire")
    @JsonIgnore
    Set<Adresse> adresses;

    public Set<Adresse> getAdresses() {
        return adresses;
    }

    public void setAdresses(Set<Adresse> adresses) {
        this.adresses = adresses;
    }

    @OneToMany(mappedBy = "partenaire")
    @JsonIgnore
    Set<Controleur> controleurs;

    @OneToMany(mappedBy = "partenaire")
    @JsonIgnore
    Set<Caissier> caissiers;

    @OneToMany(mappedBy = "partenaire")
    @JsonIgnore
    Set<Vendeur> vendeurs;

    public Partenaire() {
    }

    public Partenaire(long id, String nom, String prenom, Date dateNaiss, String lieuDeNaiss, String passport,
                      String permiDeConduire, String cni, Set<Controleur> controleurs,
                      Set<Caissier> caissiers) {

        this.partenaireId = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaiss = dateNaiss;
        this.lieuDeNaiss = lieuDeNaiss;
        this.passport = passport;
        this.permiDeConduire = permiDeConduire;
        this.cni = cni;
        this.controleurs = controleurs;
        this.caissiers = caissiers;
    }
    public Partenaire(CaissierPOJO caissierPOJO){
        this.nom = caissierPOJO == null ? null : caissierPOJO.getNom();
        this.prenom = caissierPOJO== null ? null : caissierPOJO.getPrenom();
        this.lieuDeNaiss = caissierPOJO== null ? null : caissierPOJO.getLieuDeNaiss();
        this.permiDeConduire = caissierPOJO== null ?null: caissierPOJO.getPermiDeConduire();
        this.passport = caissierPOJO == null ? null : caissierPOJO.getPassport();
        this.cni = caissierPOJO == null ? null : caissierPOJO.getCni();
        this.telephone = caissierPOJO== null ? null : caissierPOJO.getTelephone();
        this.email = caissierPOJO == null ? null : caissierPOJO.getEmail();
    }
    public Partenaire(ProprietairePOJO caissierPOJO){

        this.nom = caissierPOJO == null ? null : caissierPOJO.getNom();

        this.prenom = caissierPOJO== null ? null : caissierPOJO.getPrenom();
        this.lieuDeNaiss = caissierPOJO== null ? null : caissierPOJO.getLieuDeNaiss();
        this.permiDeConduire = caissierPOJO== null ?null: caissierPOJO.getPermiDeConduire();
        this.passport = caissierPOJO == null ? null : caissierPOJO.getPassport();
        this.cni = caissierPOJO == null ? null : caissierPOJO.getCni();
        this.telephone = caissierPOJO== null ? null : caissierPOJO.getTelephone();
        this.email = caissierPOJO == null ? null : caissierPOJO.getEmail();
        this.partenaireId
                = caissierPOJO == null ? null :
                caissierPOJO
                        .getPartenaireId();
    }

    public ProprietaireVehicule getProprietaireVehicule() {
        return proprietaireVehicule;
    }

    public void setProprietaireVehicule(ProprietaireVehicule proprietaireVehicule) {
        this.proprietaireVehicule = proprietaireVehicule;
    }

    public Long getPartenaireId() {
        return partenaireId;
    }

    public void setPartenaireId(Long id) {
        this.partenaireId = id;
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

    public Date getDateNaiss() {
        return dateNaiss;
    }

    public void setDateNaiss(Date dateNaiss) {
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

    public Set<Controleur> getControleurs() {
        return controleurs;
    }

    public void setControleurs(Set<Controleur> controleurs) {
        this.controleurs = controleurs;
    }

    public Set<Caissier> getCaissiers() {
        return caissiers;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setCaissiers(Set<Caissier> caissiers) {
        this.caissiers = caissiers;
    }


    public Set<Vendeur> getVendeurs() {
        return vendeurs;
    }

    public void setVendeurs(Set<Vendeur> vendeurs) {
        this.vendeurs = vendeurs;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumeroContribuable() {
        return numeroContribuable;
    }

    public void setNumeroContribuable(String numeroContribuable) {
        this.numeroContribuable = numeroContribuable;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Contact getContact() {
        return contact;
    }


    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return nom + " " + prenom;
    }

}
