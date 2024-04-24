package com.catis.objectTemporaire;

import java.util.UUID;

import javax.persistence.Id;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.catis.model.entity.Partenaire;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(indexName = "partenaire_index")
public class PartenaireSearch {
    @Id
    @Field(type = FieldType.Keyword, name = "id")
    private String id;

    @Field(type = FieldType.Text, name = "clientid")
    private UUID clientId;

    @Field(type = FieldType.Text, name = "contactid")
    private UUID contactId;
    
    @Field(type = FieldType.Text, name = "vendeurid")
    private UUID vendeurId;
    
    @Field(type = FieldType.Text, name = "variants")
    private String variants;
    
    @Field(type = FieldType.Keyword, name = "nom")
    private String nom;
    
    @Field(type = FieldType.Text, name = "email")
    private String email;
    
    @Field(type = FieldType.Keyword, name = "prenom")
    private String prenom;
    
    @Field(type = FieldType.Text, name = "date_naiss")
    private String dateNaiss; // date de naissance

    @Field(type = FieldType.Text, name = "lieu_de_naiss")
    private String lieuDeNaiss; // lieu de naissance
    
    @Field(type = FieldType.Keyword, name = "passport")
    private String passport;
    
    @Field(type = FieldType.Text, name = "permi_de_conduire")
    private String permiDeConduire;

    @Field(type = FieldType.Text, name = "cni")
    private String cni;

    @Field(type = FieldType.Keyword, name = "telephone")
    private String telephone;

    @Field(type = FieldType.Nested,name = "partenaire")
    private Partenaire partenaire;

    @Field(type = FieldType.Nested,name = "client")
    private Partenaire client;

    @Field(type = FieldType.Nested,name = "partenaire")
    private Partenaire contact;

    public PartenaireSearch(Partenaire partenaire) {
        this.id = partenaire.getPartenaireId().toString();
        this.nom = partenaire.getNom();
        this.prenom = partenaire.getPrenom();
        this.dateNaiss = partenaire.getDateNaiss().toString();
        this.lieuDeNaiss = partenaire.getLieuDeNaiss();
        this.passport = partenaire.getPassport();
        this.permiDeConduire = partenaire.getPermiDeConduire();
        this.cni = partenaire.getCni();
        this.telephone = partenaire.getTelephone();
        this.email = partenaire.getEmail();
        this.clientId = partenaire.getClientId();
        this.contactId = partenaire.getContactId();
    }

    @Override
    public String toString() {
        return "PartenaireSearch{" +
                "id='" + id + '\'' +
                ", clientId=" + clientId +
                ", contactId=" + contactId +
                ", vendeurId=" + vendeurId +
                ", variants='" + variants + '\'' +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", prenom='" + prenom + '\'' +
                ", dateNaiss='" + dateNaiss + '\'' +
                ", lieuDeNaiss='" + lieuDeNaiss + '\'' +
                ", passport='" + passport + '\'' +
                ", permiDeConduire='" + permiDeConduire + '\'' +
                ", cni='" + cni + '\'' +
                ", telephone='" + telephone + '\'' +
                ", partenaire=" + partenaire +
                ", client=" + client +
                ", contact=" + contact +
                '}';
    }

    
}