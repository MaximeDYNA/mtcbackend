// package com.catis.objectTemporaire;

// import javax.persistence.Id;

// import org.springframework.data.elasticsearch.annotations.Document;

// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// import org.springframework.data.elasticsearch.annotations.Field;
// import org.springframework.data.elasticsearch.annotations.FieldType;

// import com.fasterxml.jackson.databind.annotation.JsonSerialize;
// import java.util.UUID;

// @Document(indexName = "vehicule_index")
// @AllArgsConstructor
// @NoArgsConstructor
// @Data
// public class VehicleSearch {

//     @Id
//     @Field(type = FieldType.Text, name = "id")
//     private UUID vehiculeId;

//     @Field(type = FieldType.Nested, name = "marquevehicule")
//     private MarqueVehicule marqueVehicule;

//     @Field(type =FieldType.Nested, name = "energie")
//     private Energie energie;

//     @Field(type = FieldType.Keyword, name = "chassis")
//     private String chassis;

//     @Field(type = FieldType.Integer, name = "puiss_admin")
//     private Integer puissAdmin;

//     @Field(type = FieldType.Integer, name = "poids_total_cha")
//     private Integer poidsTotalCha;

//     @Field(type = FieldType.Integer, name = "poids_vide")
//     private Integer poidsVide;

//     @Field(type = FieldType.Integer, name = "charge_utile")
//     private Integer chargeUtile;

//     @Field(type = FieldType.Integer, name = "cylindre")
//     private Integer cylindre;

//     @Field(type = FieldType.Double, name = "score")
//     private Double score;

//     @Field(type = FieldType.Nested, name = "organisation")
//     private Organisation organisation;

//     @Field(type = FieldType.Text, name = "created_by")
//     private String createdBy;

//     @Field(type = FieldType.Text, name = "modified_by")
//     private String modifiedBy;

//     // @Field(type = FieldType.Date, name = "created_date", format = DateFormat.custom, pattern = "yyyy-MM-dd'T'HH:mm:ss")
//     @Field(type = FieldType.Text, name = "created_date")
//     private String createdDate;

//     @Field(type = FieldType.Text, name = "modified_date")
//     private String modifiedDate;

//     @Field(type = FieldType.Text, name = "type_vehicule")
//     private String typeVehicule;

//     // @Field(type = FieldType.Text, name = "energie_id")
//     // private UUID energieId;

//     // @Field(type = FieldType.Text, name = "organisation_id")
//     // private UUID organisation_Id;

//     // @Field(type = FieldType.Text, name = "marque_vehicule_id")
//     // private UUID marqueVehiculeId;

//     @Field(type = FieldType.Text, name = "carrosserie")
//     private String carrosserie;

//     @Field(type = FieldType.Integer, name = "place_assise")
//     private Integer placeAssise;

//     @Field(type = FieldType.Text, name = "premiere_mise_en_circulation")
//     private String premiereMiseEnCirculation;

//     @Field(type = FieldType.Boolean, name = "active_status")
//     @JsonSerialize(using = ActiveStatusSerializer.class)
//     private boolean activeStatus;

//     @Override
//     public String toString() {
//         return "Vehicle{" +
//                 "vehiculeId='" + vehiculeId + '\'' +
//                 ", marqueVehicule=" + marqueVehicule +
//                 ", energie=" + energie +
//                 ", chassis='" + chassis + '\'' +
//                 ", puissAdmin=" + puissAdmin +
//                 ", poidsTotalCha=" + poidsTotalCha +
//                 ", poidsVide=" + poidsVide +
//                 ", chargeUtile=" + chargeUtile +
//                 ", cylindre=" + cylindre +
//                 ", score=" + score +
//                 ", organisation=" + organisation +
//                 ", createdBy='" + createdBy + '\'' +
//                 ", modifiedBy='" + modifiedBy + '\'' +
//                 ", createdDate=" + createdDate +
//                 ", modifiedDate=" + modifiedDate +
//                 ", typeVehicule='" + typeVehicule + '\'' +
//                 ", carrosserie='" + carrosserie + '\'' +
//                 ", placeAssise=" + placeAssise +
//                 ", premiereMiseEnCirculation='" + premiereMiseEnCirculation + '\'' +
//                 '}';
//     }

//     @Data
//     public static class MarqueVehicule {

//         @Field(type = FieldType.Keyword,name = "id")
//         private UUID marqueVehiculeId;

//         @Field(type = FieldType.Keyword,name = "libelle")
//         private String libelle;

//         @Field(type = FieldType.Text,name = "description")
//         private String description;

//         @Field(type = FieldType.Text,name = "created_date")
//         private String createdDate;

//         @Field(type = FieldType.Text, name = "modified_date")
//         private String modifiedDate;

//         @Field(type = FieldType.Boolean, name = "active_status")
//         @JsonSerialize(using = ActiveStatusSerializer.class)
//         private Boolean activeStatus;

//         @Field(type = FieldType.Text,name = "created_by")
//         private String createdBy;

//         @Field(type = FieldType.Text,name = "modified_by")
//         private String modifiedBy;

//         @Field(type = FieldType.Text,name = "organisation_id")
//         private String organisation;

//         @Override
//         public String toString() {
//             return "MarqueVehicule{" +
//                     "libelle='" + libelle + '\'' +
//                     ", description='" + description + '\'' +
//                     ", createdDate=" + createdDate +
//                     ", modifiedDate=" + modifiedDate +
//                     ", activeStatus='" + activeStatus + '\'' +
//                     ", createdBy='" + createdBy + '\'' +
//                     ", modifiedBy='" + modifiedBy + '\'' +
//                     '}';
//         }

//     }

//     @Data
//     public static class Energie {

//         @Field(type = FieldType.Keyword,name = "id")
//         private UUID energieId;

//         @Field(type = FieldType.Keyword,name = "libelle")
//         private String libelle;

//         @Field(type = FieldType.Text,name = "organisation_id")
//         private String organisation;

//         @Field(type = FieldType.Text,name = "created_date")
//         private String createdDate;

//         @Field(type = FieldType.Text, name = "modified_date")
//         private String modifiedDate;

//         @Field(type = FieldType.Boolean,name = "active_status")
//         @JsonSerialize(using = ActiveStatusSerializer.class)
//         private Boolean activeStatus;

//         @Field(type = FieldType.Text,name = "created_by")
//         private String createdBy;

//         @Field(type = FieldType.Text,name = "modified_by")
//         private String modifiedBy;

//     }

//     @Data
//     public static class Organisation {

//         @Field(type = FieldType.Keyword, name = "id")
//         private UUID organisationId;

//         @Field(type = FieldType.Keyword, name = "parent_organisation_id")
//         private UUID parentOrganisationId;

//         @Field(type = FieldType.Keyword, name = "organisation_id")
//         private String orgId;

//         @Field(type = FieldType.Keyword, name = "name")
//         private String name;

//         @Field(type = FieldType.Keyword, name = "nom")
//         private String nom;

//         @Field(type = FieldType.Text,name = "adress")
//         private String adress;

//         @Field(type = FieldType.Text,name = "tel1")
//         private String tel1;

//         @Field(type = FieldType.Text,name = "tel2")
//         private String tel2;

//         @Field(type = FieldType.Boolean, name = "parent")
//         private Boolean parent;

//         @Field(type = FieldType.Boolean, name = "conformity")
//         private Boolean conformity;

//         @Field(type = FieldType.Double, name = "score")
//         private Double score;

//         @Field(type = FieldType.Text, name = "created_date")
//         private String createdDate;

//         @Field(type = FieldType.Text, name = "modified_date")
//         private String modifiedDate;

//         @Field(type = FieldType.Boolean, name = "active_status")
//         private Boolean activeStatus;

//         @Field(type = FieldType.Text,name = "created_by")
//         private String createdBy;

//         @Field(type = FieldType.Text, name = "modified_by")
//         private String modifiedBy;

//         @Field(type = FieldType.Text, name="patente")
//         private String patente;

//         @Field(type = FieldType.Text,name = "statut_jurique")
//         private String statutJurique;

//         @Field(type = FieldType.Text, name = "numero_de_contribuable")
//         private String numeroDeContribuable;

//         @Field(type = FieldType.Text,name = "lang")
//         private String lang;

//         @Field(type = FieldType.Text, name = "region")
//         private String region;

//         @Field(type = FieldType.Text, name = "devise")
//         private String devise;

//     }

// }
