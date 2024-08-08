package com.catis.repository.nativeQueries;

import com.catis.model.control.Control;
import com.catis.model.control.Control.StatusType;
import com.catis.model.entity.CarteGrise;
import com.catis.model.entity.CategorieProduit;
import com.catis.model.entity.CategorieVehicule;
import com.catis.model.entity.Energie;
import com.catis.model.entity.Inspection;
import com.catis.model.entity.MarqueVehicule;
import com.catis.model.entity.Partenaire;
import com.catis.model.entity.Produit;
import com.catis.model.entity.ProprietaireVehicule;
import com.catis.model.entity.Vehicule;
import com.catis.model.entity.VerbalProcess;

import com.fasterxml.jackson.databind.JsonNode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;




public class MyJsonToEntityMapper {






    public CarteGrise mapToCarteGrise(JsonNode jsonNode) {
        CarteGrise carteGrise = new CarteGrise();

        carteGrise.setCarteGriseId(
            UUID.fromString(jsonNode.get("carteGrise").get("carteGriseId").asText())
        );

        carteGrise.setMontantPaye(
            Double.parseDouble(jsonNode.get("carteGrise").get("montantPaye").asText())
        );

        carteGrise.setVehiculeGage(
            parseBooleanValue(jsonNode.get("carteGrise").get("vehiculeGage").asText())
        );

        carteGrise.setCentre_ssdt(
            jsonNode.path("carteGrise").path("centre_ssdt").isNull() 
                ? null 
                : jsonNode.path("carteGrise").path("centre_ssdt").asText()
        );

        carteGrise.setNumImmatriculation(
            jsonNode.path("carteGrise").path("numImmatriculation").isNull() 
                ? null 
                : jsonNode.path("carteGrise").path("numImmatriculation").asText()
        );

        carteGrise.setPreImmatriculation(
            jsonNode.path("carteGrise").path("preImmatriculation").isNull() 
                ? null 
                : jsonNode.path("carteGrise").path("preImmatriculation").asText()
        );

        carteGrise.setDateDebutValid(
            jsonNode.path("carteGrise").path("dateDebutValid").isNull() 
                ? null 
                : DateParser(jsonNode.path("carteGrise").path("dateDebutValid").asText())
        );

        carteGrise.setDateFinValid(
            jsonNode.path("carteGrise").path("dateFinValid").isNull() 
                ? null 
                : DateParser(jsonNode.path("carteGrise").path("dateFinValid").asText())
        );

        carteGrise.setSsdt_id(
            jsonNode.path("carteGrise").path("ssdt_id").isNull() 
                ? null 
                : jsonNode.path("carteGrise").path("ssdt_id").asText()
        );

        carteGrise.setCommune(
            jsonNode.path("carteGrise").path("commune").isNull() 
                ? null 
                : jsonNode.path("carteGrise").path("commune").asText()
        );

        carteGrise.setGenreVehicule(
            jsonNode.path("carteGrise").path("genreVehicule").isNull() 
                ? null 
                : jsonNode.path("carteGrise").path("genreVehicule").asText()
        );

        carteGrise.setEnregistrement(
            jsonNode.path("carteGrise").path("enregistrement").isNull() 
                ? null 
                : jsonNode.path("carteGrise").path("enregistrement").asText()
        );

        carteGrise.setType(
            jsonNode.path("carteGrise").path("type").isNull() 
                ? null 
                : jsonNode.path("carteGrise").path("type").asText()
        );

        carteGrise.setDateDelivrance(
            jsonNode.path("carteGrise").path("dateDelivrance").isNull() 
                ? null 
                : DateParser(jsonNode.path("carteGrise").path("dateDelivrance").asText())
        );

        carteGrise.setLieuDedelivrance(
            jsonNode.path("carteGrise").path("lieuDedelivrance").isNull() 
                ? null 
                : jsonNode.path("carteGrise").path("lieuDedelivrance").asText()
        );

        return carteGrise;
    }

  

    public Inspection mapToInspection(JsonNode jsonNode) {
        Inspection inspection = new Inspection();

        inspection.setIdInspection(
            jsonNode.path("id").isNull() 
                ? null 
                : UUID.fromString(jsonNode.path("id").asText())
        );

        inspection.setChassis(
            jsonNode.path("chassis").isNull() 
                ? null 
                : jsonNode.path("chassis").asText()
        );

        inspection.setEssieux(
            jsonNode.path("essieux").asInt()
        );

        inspection.setDateFin(
            jsonNode.path("date_fin").isNull() 
                ? null 
                : DateParser(jsonNode.path("date_fin").asText())
        );

        inspection.setBestPlate(
            jsonNode.path("bestPlate").isNull() 
                ? null 
                : jsonNode.path("bestPlate").asText()
        );

        inspection.setKilometrage(
            jsonNode.path("kilometrage").asInt()
        );

        inspection.setActiveStatus(
            jsonNode.path("activeStatus").asBoolean()
        );

        inspection.setDistancePercentage(
            jsonNode.path("distance_percentage").asInt()
        );

        return inspection;
    }

    public VerbalProcess mapToVerbalProcess(String id, Boolean status) {
        VerbalProcess verbalProcess = new VerbalProcess();

        verbalProcess.setId(
           UUID.fromString(id)
        );

        verbalProcess.setStatus(
            status
        );

        return verbalProcess;
    }

    public Control mapToControl(JsonNode controlNode) {
        Control control = new Control();

        control.setId(UUID.fromString(controlNode.path("id").asText()));
        control.setActiveStatus(controlNode.path("active_status").asBoolean());
        control.setStatus(StatusType.valueOf(controlNode.path("status").asText()));

        return control;
    }

    public MarqueVehicule mapToMarqueVehicule(JsonNode marqueVehiculeNode) {
        MarqueVehicule marqueVehicule = new MarqueVehicule();

        marqueVehicule.setMarqueVehiculeId(
            marqueVehiculeNode.path("marqueVehiculeId").isNull() 
                ? null 
                : UUID.fromString(marqueVehiculeNode.path("marqueVehiculeId").asText())
        );

        marqueVehicule.setLibelle(
            marqueVehiculeNode.path("libelle").isNull() 
                ? null 
                : marqueVehiculeNode.path("libelle").asText()
        );

        marqueVehicule.setDescription(
            marqueVehiculeNode.path("description").isNull() 
                ? null 
                : marqueVehiculeNode.path("description").asText()
        );

        return marqueVehicule;
    }
    public Energie mapToEnergie(JsonNode energieNode) {
        Energie energie = new Energie();

        energie.setEnergieId(
            energieNode.path("energieId").isNull() 
                ? null 
                : UUID.fromString(energieNode.path("energieId").asText())
        );

        energie.setLibelle(
            energieNode.path("libelle").isNull() 
                ? null 
                : energieNode.path("libelle").asText()
        );

        return energie;
    }

    public Produit mapToProduit(JsonNode jsonNode) {
        Produit produit = new Produit();

        produit.setProduitId(
            jsonNode.path("produitId").isNull() 
                ? null 
                : UUID.fromString(jsonNode.path("produitId").asText())
        );

        produit.setLibelle(
            jsonNode.path("libelle").isNull() 
                ? null 
                : jsonNode.path("libelle").asText()
        );

        produit.setImg(
            jsonNode.path("img").isNull() 
                ? null 
                : jsonNode.path("img").asText()
        );

        produit.setDescription(
            jsonNode.path("description").isNull() 
                ? null 
                : jsonNode.path("description").asText()
        );

        produit.setPrix(
            jsonNode.path("prix").isNull() 
                ? 0 
                : jsonNode.path("prix").asInt()
        );

        produit.setDelaiValidite(
            jsonNode.path("delaiValidite").isNull() 
                ? 0 
                : jsonNode.path("delaiValidite").asInt()
        );
        return produit;
    }

    
    public Vehicule mapToVehicule(JsonNode vehiculeNode) {
        Vehicule vehicule = new Vehicule();

        vehicule.setVehiculeId(
            vehiculeNode.path("vehiculeId").isNull() 
                ? null 
                : UUID.fromString(vehiculeNode.path("vehiculeId").asText())
        );

        vehicule.setScore(
            vehiculeNode.path("score").isNull() 
                ? 0 
                : vehiculeNode.path("score").asInt()
        );

        vehicule.setChassis(
            vehiculeNode.path("chassis").isNull() 
                ? null 
                : vehiculeNode.path("chassis").asText()
        );

        vehicule.setCylindre(
            vehiculeNode.path("cylindre").isNull() 
                ? 0 
                : vehiculeNode.path("cylindre").asInt()
        );

        vehicule.setPoidsVide(
            vehiculeNode.path("poidsVide").isNull() 
                ? 0 
                : vehiculeNode.path("poidsVide").asInt()
        );

        vehicule.setPuissAdmin(
            vehiculeNode.path("puissAdmin").isNull() 
                ? 0 
                : vehiculeNode.path("puissAdmin").asInt()
        );

        vehicule.setCarrosserie(
            vehiculeNode.path("carrosserie").isNull() 
                ? null 
                : vehiculeNode.path("carrosserie").asText()
        );

        vehicule.setChargeUtile(
            vehiculeNode.path("chargeUtile").isNull() 
                ? 0 
                : vehiculeNode.path("chargeUtile").asInt()
        );

        vehicule.setPlaceAssise(
            vehiculeNode.path("placeAssise").isNull() 
                ? 0 
                : vehiculeNode.path("placeAssise").asInt()
        );

        vehicule.setTypeVehicule(
            vehiculeNode.path("typeVehicule").isNull() 
                ? null 
                : vehiculeNode.path("typeVehicule").asText()
        );

        vehicule.setPoidsTotalCha(
            vehiculeNode.path("poidsTotalCha").isNull() 
                ? 0 
                : vehiculeNode.path("poidsTotalCha").asInt()
        );

        vehicule.setPremiereMiseEnCirculation(
            vehiculeNode.path("premiereMiseEnCirculation").isNull() 
                ? null 
                : DateParser(vehiculeNode.path("premiereMiseEnCirculation").asText())
        );

        return vehicule;
    }


    public CategorieProduit mapToCategorieProduit(JsonNode jsonNode) {
        CategorieProduit categorieProduit = new CategorieProduit();

        categorieProduit.setCategorieProduitId(
            jsonNode.path("categorieProduitId").isNull() 
                ? null 
                : UUID.fromString(jsonNode.path("categorieProduitId").asText())
        );

        categorieProduit.setLibelle(
            jsonNode.path("libelle").isNull() 
                ? null 
                : jsonNode.path("libelle").asText()
        );

        categorieProduit.setDescription(
            jsonNode.path("description").isNull() 
                ? null 
                : jsonNode.path("description").asText()
        );

        return categorieProduit;
    }

   public CategorieVehicule mapToCategorieVehicule(JsonNode categorieVehiculeNode) {
        CategorieVehicule categorieVehicule = new CategorieVehicule();

        categorieVehicule.setId(
            categorieVehiculeNode.path("id").isNull() 
                ? null 
                : UUID.fromString(categorieVehiculeNode.path("id").asText())
        );

        categorieVehicule.setType(
            categorieVehiculeNode.path("type").isNull() 
                ? null 
                : categorieVehiculeNode.path("type").asText()
        );

        return categorieVehicule;
    }


    public Partenaire mapToPartenaire(JsonNode partenaireNode) {
        Partenaire partenaire = new Partenaire();

        partenaire.setPartenaireId(
            partenaireNode.path("partenaire").path("partenaireId").isNull() 
                ? null 
                : UUID.fromString(partenaireNode.path("partenaire").path("partenaireId").asText())
        );

        partenaire.setNom(
            partenaireNode.path("partenaire").path("nom").isNull() 
                ? null 
                : partenaireNode.path("partenaire").path("nom").asText()
        );

        partenaire.setPrenom(
            partenaireNode.path("partenaire").path("prenom").isNull() 
                ? null 
                : partenaireNode.path("partenaire").path("prenom").asText()
        );

        return partenaire;
    }

    public ProprietaireVehicule mapToProprietaireVehicule(JsonNode proprietaireVehiculeNode) {
        ProprietaireVehicule proprietaireVehicule = new ProprietaireVehicule();

        proprietaireVehicule.setProprietaireVehiculeId(
            proprietaireVehiculeNode.path("proprietaireVehiculeId").isNull() 
                ? null 
                : UUID.fromString(proprietaireVehiculeNode.path("proprietaireVehiculeId").asText())
        );

        proprietaireVehicule.setDescription(
            proprietaireVehiculeNode.path("description").isNull() 
                ? null 
                : proprietaireVehiculeNode.path("description").asText()
        );

        proprietaireVehicule.setScore(
            proprietaireVehiculeNode.path("score").isNull() 
                ? 0 
                : proprietaireVehiculeNode.path("score").asInt()
        );

        return proprietaireVehicule;
    }


    private boolean parseBooleanValue(String booleanString) {
        return "1".equals(booleanString);
    }


    public static Date DateParser(String dateString) {
        if (dateString == null) {
            return null;
        }
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date outputDate = convertDate(dateString, inputFormat, outputFormat);
                    System.out.println("Converted Date: " + outputDate);
                    return outputDate;
                } catch (ParseException e) {
                    return null;
                    // e.printStackTrace();
                }
    }


    public static Date convertDate(String dateString, SimpleDateFormat inputFormat, SimpleDateFormat outputFormat) throws ParseException {
        Date date = inputFormat.parse(dateString);
        String formattedDate = outputFormat.format(date);
        return outputFormat.parse(formattedDate);
    }

}


