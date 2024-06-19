package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
@Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewListViewDTO {
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String activeStatus;
    private String createdBy;
    private String modifiedBy;
    private UUID idVisite;
    private boolean contreVisite;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private int statut;
    private int isConform;
    private String certidocsId;
    private boolean encours;
    private String document;
    private String ripage;
    private String suspension;
    private String freinage;
    private String pollution;
    private String reglophare;
    private String visuel;
    private OrganisationDTO organisation;
    // private CarteGriseDTO carteGrise;
    // private ControlDTO control;
    private List<Object> links;
    
        // Constructor, getters, and setters
    
        // Nested OrganisationDTO class
        public static class OrganisationDTO {
            private UUID organisationId;
            private String name;
            private String nom;
            private String adress;
    
            // Constructor, getters, and setters
        }
    
        // Nested CarteGriseDTO class
        public static class CarteGriseDTO {
            private UUID carteGriseId;
            private String numImmatriculation;
            private String preImmatriculation;
            private LocalDateTime dateDebutValid;
            private LocalDateTime dateFinValid;
            private String ssdt_id;
            private String commune;
            private double montantPaye;
            private boolean vehiculeGage;
            private String genreVehicule;
            private String enregistrement;
            private String type;
            private LocalDateTime dateDelivrance;
            private String lieuDedelivrance;
            private String centre_ssdt;
            private ProprietaireVehiculeDTO proprietaireVehicule;
            private VehiculeDTO vehicule;
            private ProduitDTO produit;
    
            // Constructor, getters, and setters
        }
    
        // Nested ProprietaireVehiculeDTO class
        public static class ProprietaireVehiculeDTO {
            private UUID proprietaireVehiculeId;
            private PartenaireDTO partenaire;
            private double score;
            private String description;
    
            // Constructor, getters, and setters
        }
    
        // Nested PartenaireDTO class
        public static class PartenaireDTO {
            private UUID partenaireId;
            private String nom;
    
            // Constructor, getters, and setters
        }
    
        // Nested VehiculeDTO class
        public static class VehiculeDTO {
            private UUID vehiculeId;
            private String typeVehicule;
            private String carrosserie;
            private int placeAssise;
            private String chassis;
            private LocalDateTime premiereMiseEnCirculation;
            private int puissAdmin;
            private int poidsTotalCha;
            private int poidsVide;
            private int chargeUtile;
            private int cylindre;
            private double score;
            private MarqueVehiculeDTO marqueVehicule;
            private EnergieDTO energie;
    
            // Constructor, getters, and setters
        }
    
        // Nested MarqueVehiculeDTO class
        public static class MarqueVehiculeDTO {
            private UUID marqueVehiculeId;
            private String libelle;
            private String description;
    
            // Constructor, getters, and setters
        }
    
        // Nested EnergieDTO class
        public static class EnergieDTO {
            private UUID energieId;
            private String libelle;
    
            // Constructor, getters, and setters
        }
    
        // Nested ProduitDTO class
        public static class ProduitDTO {
            private UUID produitId;
            private String libelle;
            private String description;
            private double prix;
            private int delaiValidite;
            private String img;
            private CategorieProduitDTO categorieProduit;
            private CategorieVehiculeDTO categorieVehicule;
    
            // Constructor, getters, and setters
        }
    
        // Nested CategorieProduitDTO class
        public static class CategorieProduitDTO {
            private UUID categorieProduitId;
            private String libelle;
            private String description;
    
            // Constructor, getters, and setters
        }
    
        // Nested CategorieVehiculeDTO class
        public static class CategorieVehiculeDTO {
            private UUID id;
            private String type;
    
            // Constructor, getters, and setters
        }
    
        // Nested ControlDTO class
        public static class ControlDTO {
            private UUID id;
            private String status;
            private LocalDateTime contreVDelayAt;
            private LocalDateTime validityAt;
    
            // Constructor, getters, and setters
        }
    }
    
