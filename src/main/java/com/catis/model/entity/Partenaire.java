package com.catis.model.entity;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import com.catis.objectTemporaire.CaissierPOJO;
import com.catis.objectTemporaire.PartenaireSearch;
import com.catis.objectTemporaire.ProprietairePOJO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;


@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "t_partenaire")
@Audited
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Partenaire extends JournalData {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    
    @Column(name = "id", updatable = false, nullable = false)
    private UUID partenaireId;

    private String nom;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String prenom;
    @Column(nullable = true)

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date dateNaiss; // date de naissance

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lieuDeNaiss; // lieu de naissance

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String passport;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String permiDeConduire;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cni;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String telephone;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;
    @Column(unique = true)

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String numeroContribuable;

    
   
    @OneToOne(mappedBy = "partenaire",fetch = FetchType.LAZY)
    @JsonIgnore
    private Client client;


    @OneToOne(mappedBy = "partenaire",fetch = FetchType.LAZY)
    @JsonIgnore
    private Contact contact;

   
    @OneToOne(mappedBy = "partenaire",fetch = FetchType.LAZY)
    @JsonIgnore
    private ProprietaireVehicule proprietaireVehicule;

    @OneToOne(mappedBy = "partenaire",fetch = FetchType.LAZY)
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

    @OneToMany(mappedBy = "partenaire",fetch = FetchType.LAZY)
    @JsonIgnore
    Set<Caissier> caissiers;

    @OneToMany(mappedBy = "partenaire")
    @JsonIgnore
    Set<Vendeur> vendeurs;

    @Transient
    private UUID clientId;

    @Transient
    private UUID contactId;

    public void setPartenaireId(UUID partenaireId) {
        this.partenaireId = partenaireId;
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

    public Partenaire(PartenaireSearch document) {
        UUID uuid = UUID.fromString(document.getId());
        this.partenaireId = uuid;
        this.nom = document.getNom();
        this.prenom = document.getPrenom();
        this.passport = document.getPassport();
        this.permiDeConduire = document.getPermiDeConduire();
        this.cni = document.getCni();
        this.telephone = document.getTelephone();
        this.email = document.getEmail();
        this.clientId = document.getClientId();
        this.contactId = document.getContactId();
    }


    @Override
    public String toString() {
        return "PartenaireDocument{" +
                "partenaireId=" + partenaireId +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", dateNaiss=" + dateNaiss +
                ", lieuDeNaiss='" + lieuDeNaiss + '\'' +
                ", passport='" + passport + '\'' +
                ", permiDeConduire='" + permiDeConduire + '\'' +
                ", cni='" + cni + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", numeroContribuable='" + numeroContribuable + '\'' +
                ", clientId=" + clientId +
                ", contactId=" + contactId +
                '}';
    }

}
