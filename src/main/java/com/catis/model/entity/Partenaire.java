package com.catis.model.entity;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import com.catis.objectTemporaire.CaissierPOJO;
import com.catis.objectTemporaire.ProprietairePOJO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_partenaire")
@Audited
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Partenaire extends JournalData {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID partenaireId;

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


    @Override
    public String toString() {
        return nom + " " + prenom;
    }

}
