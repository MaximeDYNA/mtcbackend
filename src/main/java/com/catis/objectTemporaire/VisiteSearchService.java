// package com.catis.objectTemporaire;


// import javax.persistence.Id;
// import org.springframework.data.elasticsearch.annotations.Document;
// import org.springframework.data.elasticsearch.annotations.Field;
// import org.springframework.data.elasticsearch.annotations.FieldType;

// import com.fasterxml.jackson.databind.annotation.JsonSerialize;

// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;

// import java.util.Date;
// import java.util.List;
// import java.util.UUID;



// @Data
// @AllArgsConstructor
// @NoArgsConstructor
// @Getter
// @Setter
// @Document(indexName = "visite_index")
// public class VisiteSearchService {

//     @Id
//     private UUID Id; 

//     @Field(type = FieldType.Integer)
//     private int contreVisite;

//     @Field(type = FieldType.Text)
//     private String document;

//     @Field(type = FieldType.Text)
//     private String suspension;

//     @Field(type = FieldType.Text)
//     private String createdDate;

//     @Field(type = FieldType.Text)
//     private String reglophare;

//     @Field(type = FieldType.Text)
//     private String ripage;

//     @Field(type = FieldType.Object, includeInParent = true)
//     private Inspection inspection;

//     @Field(type = FieldType.Text)
//     private String verbalProcessId;

//     @Field(type = FieldType.Nested, includeInParent = true)
//     private CarteGrise carteGrise;

//     @Field(type = FieldType.Integer)
//     private int statut;

//     @Field(type = FieldType.Text)
//     private String freinage;

//     @Field(type = FieldType.Keyword)
//     private String idVisite;

//     @Field(type = FieldType.Integer)
//     private int statutVisite;

//     @Field(type = FieldType.Object)
//     private Control control;

//     @Field(type = FieldType.Integer)
//     private int verbalStatus;

//     @Field(type = FieldType.Text)
//     private String visuel;

//     @Field(type = FieldType.Integer)
//     private int conformityTest;

//     @Field(type = FieldType.Text)
//     private String modifiedDate;

//     @Field(type = FieldType.Integer)
//     private int encours;

//     @Field(type = FieldType.Text)
//     private String pollution;

//     // Getters and Setters
//     @Data
//     @AllArgsConstructor
//     @NoArgsConstructor
//     @Getter
//     @Setter
//     public static class Inspection {
//         @Field(type = FieldType.Integer)
//         private int activeStatus;

//         @Field(type = FieldType.Text)
//         private String dateFin;

//         @Field(type = FieldType.Integer)
//         private int essieux;

//         @Field(type = FieldType.Keyword)
//         private String chassis;

//         @Field(type = FieldType.Text)
//         private String bestPlate;

//         @Field(type = FieldType.Keyword)
//         private String id;

//         @Field(type = FieldType.Text)
//         private String dateDebut;

//         @Field(type = FieldType.Integer)
//         private int kilometrage;

//         @Field(type = FieldType.Integer)
//         private int distancePercentage;

//         // Getters and Setters
//     }

//     @Data
//     @AllArgsConstructor
//     @NoArgsConstructor
//     @Getter
//     @Setter
//     public static class CarteGrise {
//         @Field(type = FieldType.Text)
//         private String lieuDedelivrance;

//         @Field(type = FieldType.Text)
//         private String dateDelivrance;

//         @Field(type = FieldType.Object,includeInParent = true)
//         private Produit produit;

//         @Field(type = FieldType.Text)
//         private String centreSsdt;

//         @Field(type = FieldType.Keyword)
//         private String  carteGriseId;

//         @Field(type = FieldType.Text)
//         private String dateDebutValid;

//         @Field(type = FieldType.Text)
//         private String preImmatriculation;

//         @Field(type = FieldType.Text)
//         private String enregistrement;

//         @Field(type = FieldType.Text)
//         private String ssdtId;

//         @Field(type = FieldType.Text)
//         private String commune;

//         @Field(type = FieldType.Nested, includeInParent = true)
//         private Vehicule vehicule;

//         @Field(type = FieldType.Integer)
//         private int montantPaye;

//         @Field(type = FieldType.Text)
//         private String dateFinValid;

//         @Field(type = FieldType.Text)
//         private String genreVehicule;

//         @Field(type = FieldType.Text)
//         private String type;

//         @Field(type = FieldType.Text)
//         @JsonSerialize(using = ActiveStatusSerializer.class)
//         private String vehiculeGage;

//         @Field(type = FieldType.Keyword)
//         private String numImmatriculation;

//         @Field(type = FieldType.Nested, includeInParent = true)
//         private ProprietaireVehicule proprietaireVehicule;

//         // Getters and Setters
//         @Data
//         @AllArgsConstructor
//         @NoArgsConstructor
//         @Getter
//         @Setter
//         public static class Produit {
//             @Field(type = FieldType.Integer)
//             private int delaiValidite;

//             @Field(type = FieldType.Object, includeInParent = true)
//             private CategorieVehicule categorieVehicule;

//             @Field(type = FieldType.Text)
//             private String libelle;

//             @Field(type = FieldType.Text)
//             private String img;

//             @Field(type = FieldType.Text)
//             private String description;

//             @Field(type = FieldType.Text)
//             private String produitId;

//             @Field(type = FieldType.Integer)
//             private int prix;

//             @Field(type = FieldType.Object, includeInParent = true)
//             private CategorieProduit categorieProduit;

//             // Getters and Setters
//         }

//         @Data
//         @AllArgsConstructor
//         @NoArgsConstructor
//         @Getter
//         @Setter
//         public static class CategorieVehicule {
//             @Field(type = FieldType.Text)
//             private String  id;

//             @Field(type = FieldType.Text)
//             private String type;

//             // Getters and Setters
//         }

//         @Data
//         @AllArgsConstructor
//         @NoArgsConstructor
//         @Getter
//         @Setter
//         public static class CategorieProduit {
//             @Field(type = FieldType.Text)
//             private String categorieProduitId;

//             @Field(type = FieldType.Text)
//             private String libelle;

//             @Field(type = FieldType.Text)
//             private String description;

//             // Getters and Setters
//         }

        
//         @Data
//         @AllArgsConstructor
//         @NoArgsConstructor
//         @Getter
//         @Setter
//         public static class Vehicule {
//             @Field(type = FieldType.Integer)
//             private int puissAdmin;

//             @Field(type = FieldType.Text)
//             private String typeVehicule;

//             @Field(type = FieldType.Integer)
//             private int cylindre;

//             @Field(type = FieldType.Integer)
//             private int chargeUtile;

//             @Field(type = FieldType.Keyword)
//             private String vehiculeId;

//             @Field(type = FieldType.Integer)
//             private int placeAssise;

//             @Field(type = FieldType.Object, includeInParent = true)
//             private MarqueVehicule marqueVehicule;

//             @Field(type = FieldType.Text)
//             private String carrosserie;

//             @Field(type = FieldType.Text)
//             private String premiereMiseEnCirculation;

//             @Field(type = FieldType.Keyword)
//             private String chassis;

//             @Field(type = FieldType.Object, includeInParent = true)
//             private Energie energie;

//             @Field(type = FieldType.Integer)
//             private int poidsVide;

//             @Field(type = FieldType.Integer)
//             private int score;

//             @Field(type = FieldType.Integer)
//             private int poidsTotalCha;

//             // Getters and Setters
//         }

        
//         @Data
//         @AllArgsConstructor
//         @NoArgsConstructor
//         @Getter
//         @Setter
//         public static class MarqueVehicule {
//             @Field(type = FieldType.Text)
//             private String  marqueVehiculeId;

//             @Field(type = FieldType.Text)
//             private String libelle;

//             @Field(type = FieldType.Text)
//             private String description;

//             // Getters and Setters
//         }

        
//         @Data
//         @AllArgsConstructor
//         @NoArgsConstructor
//         @Getter
//         @Setter
//         public static class Energie {
//             @Field(type = FieldType.Text)
//             private String  energieId;

//             @Field(type = FieldType.Text)
//             private String libelle;

//             // Getters and Setters
//         }

        
//         @Data
//         @AllArgsConstructor
//         @NoArgsConstructor
//         @Getter
//         @Setter
//         public static class ProprietaireVehicule {
//             @Field(type = FieldType.Nested, includeInParent = true)
//             private Partenaire partenaire;

//             @Field(type = FieldType.Integer)
//             private int score;

//             @Field(type = FieldType.Keyword)
//             private String proprietaireVehiculeId;

//             @Field(type = FieldType.Text)
//             private String description;

//             // Getters and Setters

            
//         @Data
//         @AllArgsConstructor
//         @NoArgsConstructor
//         @Getter
//         @Setter
//             public static class Partenaire {
//                 @Field(type = FieldType.Keyword)
//                 private String nom;

//                 @Field(type = FieldType.Keyword)
//                 private String  partenaireId;

//                 @Field(type = FieldType.Keyword)
//                 private String prenom;

//                 // Getters and Setters
//             }
//         }
//     }

    
//     @Data
//     @AllArgsConstructor
//     @NoArgsConstructor
//     @Getter
//     @Setter
//     public static class Control {
//         @Field(type = FieldType.Integer)
//         private int activeStatus;

//         @Field(type = FieldType.Text)
//         private String id;

//         @Field(type = FieldType.Text)
//         private String status;

//         // Getters and Setters
//     }
    
//     // Getters and Setters for outer class fields
// }
