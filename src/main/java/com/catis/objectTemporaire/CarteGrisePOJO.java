package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarteGrisePOJO {

    private UUID carteGriseId;
    private String numImmatriculation;
    private String preImmatriculation;
    private Date dateDebutValid;
    private Date dateFinValid;
    private String ssdt_id;
    private String commune;
    private double montantPaye;
    private boolean vehiculeGage;
    private String genreVehicule;
    private String enregistrement;
    private Date dateDelivrance;
    private String lieuDedelivrance;
    private String centre_ssdt;
    private ObjectForSelect proprietaire;
    private ObjectForSelect vehicule;
    private ObjectForSelect produit;
    private ObjectForSelect organisationId;

}
