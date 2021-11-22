package com.catis.objectTemporaire;

import com.catis.model.entity.CarteGrise;
import com.catis.model.entity.Produit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
public class NewListView {
    private Long id;
    private Produit categorie;
    private String type;
    private String reference;
    private String chassis;
    private String client;
    private String createdAt;
    private LocalDateTime createdDate;
    private String statut;
    private int statutVisite;
    private Long idVisite;
    private boolean contreVisite;
    private Long inspection;
    private CarteGrise carteGrise;
    private boolean conformityTest;
    private int isConform;
    private String organisation;
    private String bestPlate;
    private double accurance;
    private String date;
    private String document;
    private boolean newCss=false;

    public NewListView(Long id, Produit categorie, String type, String reference, String chassis, String client, String createdAt, LocalDateTime createdDate, String statut, int statutVisite, Long idVisite, boolean contreVisite, Long inspection, CarteGrise carteGrise, boolean conformityTest, int isConform, String organisation, String bestPlate, double accurance, String date) {
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
    public NewListView(Long id, Produit categorie, String type, String reference,
                       String chassis, String client, String createdAt, LocalDateTime createdDate,
                       String statut, int statutVisite, Long idVisite, boolean contreVisite,
                       Long inspection, CarteGrise carteGrise, boolean conformityTest, int isConform,
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
