package com.catis.objectTemporaire;

import com.catis.model.entity.CarteGrise;
import com.catis.model.entity.Produit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor @NoArgsConstructor
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
}
