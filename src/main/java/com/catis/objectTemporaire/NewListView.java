package com.catis.objectTemporaire;

import com.catis.model.entity.CarteGrise;
import com.catis.model.entity.Produit;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewListView implements Serializable {
    private Produit categorie;
    private UUID id;
    private String type;
    private String reference;
    private String chassis;
    private String client;
    private String createdAt;
    private LocalDateTime createdDate;
    private String statut;
    private int statutVisite;
    private UUID idVisite;
    private boolean contreVisite;
    private UUID inspection;
    private CarteGrise carteGrise;
    private boolean conformityTest;
    private int isConform;
    private String organisation;
    private String bestPlate;
    private double accurance;
    private String date;
    private boolean newCss=false;
    private String document;

    public NewListView(UUID id, Produit categorie, String type, String reference, String chassis, String client, String createdAt, LocalDateTime createdDate, String statut, int statutVisite, UUID idVisite, boolean contreVisite, UUID inspection, CarteGrise carteGrise, boolean conformityTest, int isConform, String organisation, String bestPlate, double accurance, String date) {
        this.id = id;
        this.categorie = categorie;
        this.type = type;
        this.reference = reference;
        this.chassis = chassis;
        this.client = client;
        this.createdAt = createdAt;
        this.createdDate = createdDate;
        this.statut = statut;
        this.statutVisite = statutVisite;
        this.idVisite = idVisite;
        this.contreVisite = contreVisite;
        this.inspection = inspection;
        this.carteGrise = carteGrise;
        this.conformityTest = conformityTest;
        this.isConform = isConform;
        this.organisation = organisation;
        this.bestPlate = bestPlate;
        this.accurance = accurance;
        this.date = date;
        System.out.println("Traitement de la visite N°"+this.id);
    }
    public NewListView(UUID id,Produit categorie, String type, String reference,
                       String chassis, String client, String createdAt, LocalDateTime createdDate,
                       String statut, int statutVisite, UUID idVisite, boolean contreVisite,
                       UUID inspection, CarteGrise carteGrise, boolean conformityTest, int isConform,
                       String organisation, String bestPlate, double accurance, String date, boolean newCss, String document) {
        this.id = id;
        this.categorie = categorie;
        this.type = type;
        this.reference = reference;
        this.chassis = chassis;
        this.client = client;
        this.createdAt = createdAt;
        this.createdDate = createdDate;
        this.statut = statut;
        this.statutVisite = statutVisite;
        this.idVisite = idVisite;
        this.contreVisite = contreVisite;
        this.inspection = inspection;
        this.carteGrise = carteGrise;
        this.conformityTest = conformityTest;
        this.isConform = isConform;
        this.organisation = organisation;
        this.bestPlate = bestPlate;
        this.accurance = accurance;
        this.date = date;
        this.newCss = newCss;
        this.document = document;
    }
}
