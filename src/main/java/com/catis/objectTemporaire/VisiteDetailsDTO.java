package com.catis.objectTemporaire;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;

// import com.catis.objectTemporaire.Serializers.CarteGriseDTO;

// import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


import com.fasterxml.jackson.annotation.JsonProperty;


@Data
@ToString
@NoArgsConstructor
@Getter
@Setter
public class VisiteDetailsDTO implements Serializable {
    private LocalDateTime createdDate;
    private boolean contreVisite;
    private boolean encours;
    private int statut;
    private UUID id;
    private String document;
    private int isConform;
    private String certidocsId;
    private CarteGriseDTO carteGrise;

    public VisiteDetailsDTO(
        LocalDateTime createdDate,
        boolean contreVisite,
        boolean encours,
        int statut,
        UUID id,
        String document,
        int isConform,
        String certidocsId,
        CarteGriseDTO carteGrise
    ) {
        this.createdDate = createdDate;
        this.contreVisite = contreVisite;
        this.encours = encours;
        this.statut = statut;
        this.id = id;
        this.document = document;
        this.isConform = isConform;
        this.certidocsId = certidocsId;
        this.carteGrise = carteGrise;
    }

    // Getters and setters
    @Data
    @ToString
    @NoArgsConstructor
    @Getter
    @Setter
    public static class CarteGriseDTO {
        private UUID carteGriseId;
        private String numImmatriculation;
        private String preImmatriculation;
        private LocalDate dateDebutValid;
        private LocalDate dateFinValid;
        private String ssdtId;
        private String commune;
        private double montantPaye;
        private boolean vehiculeGage;
        private String genreVehicule;
        private String enregistrement;
        private String type;
        private LocalDate dateDelivrance;
        private String lieuDedelivrance;
        private String centreSsdt;
        private VehiculeDTO vehicule;
        private ProduitDTO produit;
        private ProprietaireVehiculeDTO proprietaireVehicule;

        public CarteGriseDTO(
            UUID carteGriseId,
            String numImmatriculation,
            String preImmatriculation,
            LocalDate dateDebutValid,
            LocalDate dateFinValid,
            String ssdtId,
            String commune,
            double montantPaye,
            boolean vehiculeGage,
            String genreVehicule,
            String enregistrement,
            String type,
            LocalDate dateDelivrance,
            String lieuDedelivrance,
            String centreSsdt,
            VehiculeDTO vehicule,
            ProduitDTO produit,
            ProprietaireVehiculeDTO proprietaireVehicule
        ) {
            this.carteGriseId = carteGriseId;
            this.numImmatriculation = numImmatriculation;
            this.preImmatriculation = preImmatriculation;
            this.dateDebutValid = dateDebutValid;
            this.dateFinValid = dateFinValid;
            this.ssdtId = ssdtId;
            this.commune = commune;
            this.montantPaye = montantPaye;
            this.vehiculeGage = vehiculeGage;
            this.genreVehicule = genreVehicule;
            this.enregistrement = enregistrement;
            this.type = type;
            this.dateDelivrance = dateDelivrance;
            this.lieuDedelivrance = lieuDedelivrance;
            this.centreSsdt = centreSsdt;
            this.vehicule = vehicule;
            this.produit = produit;
            this.proprietaireVehicule = proprietaireVehicule;
        }

        // Getters and setters
    }

        @Data
        @ToString
        @NoArgsConstructor
        @Getter
        @Setter
        public static class PartenaireDTO{
                    private UUID  partenaireId;
                    private String nom;
                    private String prenom;
                    public PartenaireDTO(
                        UUID partenaireId,
                        String nom,
                        String prenom
                ) {
                    this.partenaireId = partenaireId;
                    this.nom = nom;
                    this.prenom = prenom;
                }
        }
        @Data
        @ToString
        @NoArgsConstructor
        @Getter
        @Setter
        public static class ProprietaireVehiculeDTO {
                private UUID  proprietaireVehiculeId;
                private PartenaireDTO partenaire;
                private double score;
                private String description;

                public ProprietaireVehiculeDTO(
                    UUID proprietaireVehiculeId,
                    PartenaireDTO partenaire,
                    double score,
                    String description
            ) {
                this.proprietaireVehiculeId = proprietaireVehiculeId;
                this.partenaire = partenaire;
                this.score = score;
                this.description = description;
            }
        }

    @Data
    @ToString
    @NoArgsConstructor
    @Getter
    @Setter
    public  static class VehiculeDTO {
            private UUID  vehiculeId;
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

            public VehiculeDTO(
            UUID vehiculeId,
            String typeVehicule,
            String carrosserie,
            int placeAssise,
            String chassis,
            LocalDateTime premiereMiseEnCirculation,
            int puissAdmin,
            int poidsTotalCha,
            int poidsVide,
            int chargeUtile,
            int cylindre,
            double score,
            MarqueVehiculeDTO marqueVehicule,
            EnergieDTO energie
            ){
                this.vehiculeId = vehiculeId;
                this.typeVehicule = typeVehicule;
                this.carrosserie = carrosserie;
                this.placeAssise = placeAssise;
                this.chassis = chassis;
                this.premiereMiseEnCirculation = premiereMiseEnCirculation;
                this.puissAdmin = puissAdmin;
                this.poidsTotalCha = poidsTotalCha;
                this.poidsVide = poidsVide;
                this.chargeUtile = chargeUtile;
                this.cylindre = cylindre;
                this.score = score;
                this.marqueVehicule = marqueVehicule;
                this.energie = energie;
            }

            }

            @Data
        @ToString
        @NoArgsConstructor
        @Getter
        @Setter
        public static class EnergieDTO {
                private UUID energieId;
                private String libelle;
                public EnergieDTO(
                        UUID energieId,
                        String libelle
                ) {
                    this.energieId = energieId;
                    this.libelle = libelle;
                }
        }

        @Data
        @ToString
        @NoArgsConstructor
        @Getter
        @Setter
        public  static class MarqueVehiculeDTO{
                    private UUID  marqueVehiculeId;
                    private String libelle;
                    private String description;

                    public MarqueVehiculeDTO(
                        UUID marqueVehiculeId,
                        String libelle,
                        String description
                ) {
                    this.marqueVehiculeId = marqueVehiculeId;
                    this.libelle = libelle;
                    this.description = description;
                }
        }


        @Data
        @ToString
        @NoArgsConstructor
        @Getter
        @Setter
        public static class ProduitDTO {
            private UUID produitId;
            private String libelle;
            private String description;
            private double prix;
            private int delaiValidite;
            private String img;
            private CategorieProduitDTO categorieProduit;
            private CategorieVehiculeDTO categorieVehicule;

        public ProduitDTO(
            UUID produitId,
            String libelle,
            String description,
            double prix,
            int delaiValidite,
            String img,
            CategorieProduitDTO categorieProduit,
            CategorieVehiculeDTO categorieVehicule
    ) {
        this.produitId = produitId;
        this.libelle = libelle;
        this.description = description;
        this.prix = prix;
        this.delaiValidite = delaiValidite;
        this.img = img;
        this.categorieProduit = categorieProduit;
        this.categorieVehicule = categorieVehicule;
    }
        }

        @Data
        @ToString
        @NoArgsConstructor
        @Getter
        @Setter
        public static class CategorieProduitDTO {
            private UUID categorieProduitId;
            private String libelle;
            private String description;

            public CategorieProduitDTO(
                UUID categorieProduitId,
                String libelle,
                String description
        ) {
            this.categorieProduitId = categorieProduitId;
            this.libelle = libelle;
            this.description = description;
        }
            
        }

        @Data
        @ToString
        @NoArgsConstructor
        @Getter
        @Setter
        public static class CategorieVehiculeDTO {
                    private UUID  id;
                    private String type;
                    // Constructor with @JsonProperty annotations
                    public CategorieVehiculeDTO(
                        UUID id,
                        String type
                ) {
                    this.id = id;
                    this.type = type;
                }
        }


}

// public class VisiteDetailsDTO{
//     private LocalDateTime createdDate;
//     private boolean contreVisite;
//     private boolean encours;
//     private int statut;
//     private UUID id;
//     private String document;
//     private int isConform;
//     private String certidocsId;
//     private CarteGriseDTO carteGrise;

//     public VisiteDetailsDTO(LocalDateTime createdDate, boolean contreVisite, boolean encours, int statut, UUID id, String document, int isConform,String certidocsId,UUID carteGriseId,
//     String numImmatriculation,String preImmatriculation,Date dateDebutValid,Date dateFinValid,String ssdt_id,String commune,double montantPaye,boolean vehiculeGage,String genreVehicule,String enregistrement,String type,Date dateDelivrance,
//     String lieuDedelivrance,String centre_ssdt
//     ) 
    
//     {
//         this.createdDate = createdDate;
//         this.contreVisite = contreVisite;
//         this.encours = encours;
//         this.statut = statut;
//         this.id = id;
//         this.document = document;
//         this.isConform = isConform;
//         this.certidocsId = certidocsId;
//         this.carteGrise = new CarteGriseDTO(carteGriseId, numImmatriculation,  preImmatriculation,dateDebutValid, dateFinValid, 
//         ssdt_id, commune, montantPaye, vehiculeGage, genreVehicule, 
//         enregistrement, type, dateDelivrance, lieuDedelivrance, centre_ssdt);
        
//     }
//     // // cartegriseproduct
//     // UUID produitId,
//     // String libelle,
//     // String description,
//     // double prix,
//     // int delaiValidite,
//     // String img,
//     // // catergory product
//     // UUID categorieProduitId,
//     // String catlibelle,
//     // String catdescription,
//     // // for product cat vehicule
//     // UUID  vid,
//     // String vtype
//     //  private ProprietaireVehiculeDTO proprietaireVehicule;
//     // private VehiculeDTO vehicule;
//     // private ProduitDTO produit;
//         // private UUID inspection;
//         // private String bestPlate;
//         // private double accurance;
//         // private boolean newCss;
//         // private CarteGriseDTO carteGrise;  
//     }
    