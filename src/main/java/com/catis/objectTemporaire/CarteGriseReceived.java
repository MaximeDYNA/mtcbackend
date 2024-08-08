package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarteGriseReceived {
    private UUID carteGriseId;
    private UUID visiteId;
    private String numImmatriculation;
    private String preImmatriculation;// immatriculation précédente
    private Date dateDebutValid; //debut de validité
    private Date dateFinValid;// fin de validité
    private String ssdt_id;
    private String commune;
    private double montantPaye;
    private boolean vehiculeGage; // véhicule gagé
    private String genreVehicule;
    private UUID marqueVehiculeId;
    private UUID produitId;
    private UUID proprietaireId;
    private UUID vehiculeId;
    private String typeVehicule;
    private String carrosserie;
    private String enregistrement;
    private String chassis;
    private Date premiereMiseEnCirculation;
    private UUID energieId;
    private int cylindre; //cm3
    private int puissAdmin; // Puissance Administrative
    private int poidsTotalCha; // poids total en charge
    private int poidsVide;
    private int chargeUtile; // charge utile
    private Date dateDelivrance;
    private String lieuDedelivrance;// lieu de délivrance
    private String centre_ssdt;
    private int places;
    private String type;

    public CarteGriseReceived() {
        super();
        // TODO Auto-generated constructor stub
    }

    public CarteGriseReceived(UUID carteGriseId, String numImmatriculation, String preImmatriculation,
                              Date dateDebutValid, Date dateFinValid, String ssdt_id, String commune, double montantPaye,
                              boolean vehiculeGage, String genreVehicule, UUID marqueVehiculeId, String typeVehicule, String carrosserie,
                              String enregistrement, String chassis, Date premiereMiseEnCirculation, UUID energieId, int cylindre,
                              int puissAdmin, int poidsTotalCha, int poidsVide, int chargeUtile, Date dateDelivrance,
                              String lieuDedelivrance, String centre_ssdt) {
        super();
        this.carteGriseId = carteGriseId;
        this.numImmatriculation = numImmatriculation;
        this.preImmatriculation = preImmatriculation;
        this.dateDebutValid = dateDebutValid;
        this.dateFinValid = dateFinValid;
        this.ssdt_id = ssdt_id;
        this.commune = commune;
        this.montantPaye = montantPaye;
        this.vehiculeGage = vehiculeGage;
        this.genreVehicule = genreVehicule;
        this.marqueVehiculeId = marqueVehiculeId;
        this.typeVehicule = typeVehicule;
        this.carrosserie = carrosserie;
        this.enregistrement = enregistrement;
        this.chassis = chassis;
        this.premiereMiseEnCirculation = premiereMiseEnCirculation;
        this.energieId = energieId;
        this.cylindre = cylindre;
        this.puissAdmin = puissAdmin;
        this.poidsTotalCha = poidsTotalCha;
        this.poidsVide = poidsVide;
        this.chargeUtile = chargeUtile;
        this.dateDelivrance = dateDelivrance;
        this.lieuDedelivrance = lieuDedelivrance;
        this.centre_ssdt = centre_ssdt;
    }

}
