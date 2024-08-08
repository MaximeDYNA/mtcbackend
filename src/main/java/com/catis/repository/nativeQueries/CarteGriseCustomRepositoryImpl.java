package com.catis.repository.nativeQueries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.catis.model.entity.CarteGrise;
import com.catis.model.entity.Organisation;
import com.catis.model.entity.Partenaire;
import com.catis.model.entity.Produit;
import com.catis.model.entity.ProprietaireVehicule;
import com.catis.model.entity.Vehicule;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageImpl;

@Repository
public class CarteGriseCustomRepositoryImpl implements CarteGriseCustomRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<CarteGrise> findByActiveStatusTrueAndSearch(String search, Pageable pageable) {
        String sql = String.format("SELECT JSON_OBJECT(" +
                "'activeStatus', CASE cg.active_status WHEN b'1' THEN 1 ELSE 0 END, " +
                "'modifiedBy', cg.modified_by, " +
                "'carteGriseId', cg.id, " +
                "'numImmatriculation', cg.num_immatriculation, " +
                "'preImmatriculation', cg.pre_immatriculation, " +
                "'ssdt_id', cg.ssdt_id, " +
                "'commune', cg.commune, " +
                "'montantPaye', cg.montant_paye, " +
                "'vehiculeGage', CASE cg.vehicule_gage WHEN b'1' THEN 1 ELSE 0 END, " +
                "'genreVehicule', cg.genre_vehicule, " +
                "'enregistrement', cg.enregistrement, " +
                "'type', cg.type, " +
                "'lieuDedelivrance', cg.lieu_dedelivrance, " +
                "'centre_ssdt', cg.centre_ssdt, " +
                "'vehicule', JSON_OBJECT('id', v.id, 'chassis', v.chassis), " +
                "'proprietaireVehicule', JSON_OBJECT('id', pv.id, 'partenaire', JSON_OBJECT('id', p.id, 'nom', p.nom, 'prenom', p.prenom)), " +
                "'produit', JSON_OBJECT('id', pr.id, 'libelle', pr.libelle), " +
                "'org', JSON_OBJECT('id', o.id, 'name', o.name) " +
                ") AS carteGriseJson " +
                "FROM t_cartegrise cg " +
                "LEFT JOIN t_vehicule v ON cg.vehicule_id = v.id " +
                "LEFT JOIN t_proprietairevehicule pv ON cg.proprietaire_vehicule_id = pv.id " +
                "LEFT JOIN t_partenaire p ON pv.partenaire_id = p.id " +
                "LEFT JOIN t_produit pr ON cg.produit_id = pr.id " +
                "JOIN t_organisation o ON o.id = cg.organisation_id " +
                "WHERE cg.active_status = true " +
                "AND ( " +
                "    (COALESCE('%s', '') = '' OR LOWER(cg.num_immatriculation) LIKE LOWER(CONCAT('%%', '%s', '%%'))) " +
                "    OR (COALESCE('%s', '') = '' OR LOWER(p.nom) LIKE LOWER(CONCAT('%%', '%s', '%%'))) " +
                ") " +
                "ORDER BY cg.created_date DESC " +
                "LIMIT %d OFFSET %d",search, search, search, search, pageable.getPageSize(), pageable.getOffset());

        Query query = entityManager.createNativeQuery(sql);

        String countQueryStr = "select count(*) from t_cartegrise cg where cg.active_status=true ";
         Query countQuery = entityManager.createNativeQuery(countQueryStr);
         long totalRows = ((Number) countQuery.getSingleResult()).longValue();


        List<Object[]> results = query.getResultList();
        List<CarteGrise> carteGrises = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        
        for (Object result : results) {
            String json = (String) result;
            carteGrises.add(parseCarteGriseJson(json, objectMapper));
        }

        return new PageImpl<>(carteGrises, pageable, totalRows);
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
    private CarteGrise parseCarteGriseJson(String json, ObjectMapper objectMapper) {
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            CarteGrise carteGrise = new CarteGrise();
            carteGrise.setActiveStatus(jsonNode.get("activeStatus").asBoolean());
            carteGrise.setModifiedBy(jsonNode.get("modifiedBy").asText());
            carteGrise.setCarteGriseId(UUID.fromString(jsonNode.get("carteGriseId").asText()));
            carteGrise.setNumImmatriculation(jsonNode.get("numImmatriculation").asText());
            carteGrise.setPreImmatriculation(jsonNode.get("preImmatriculation").asText());
            carteGrise.setSsdt_id(jsonNode.get("ssdt_id").asText());
            carteGrise.setCommune(jsonNode.get("commune").asText());
            carteGrise.setMontantPaye(jsonNode.get("montantPaye").asDouble());
            carteGrise.setVehiculeGage(jsonNode.get("vehiculeGage").asBoolean());
            carteGrise.setGenreVehicule(jsonNode.get("genreVehicule").asText());
            carteGrise.setEnregistrement(jsonNode.get("enregistrement").asText());
            carteGrise.setType(jsonNode.get("type").asText());
            carteGrise.setLieuDedelivrance(jsonNode.get("lieuDedelivrance").asText());
            carteGrise.setCentre_ssdt(jsonNode.get("centre_ssdt").asText());

            JsonNode vehiculeNode = jsonNode.get("vehicule");
            if (vehiculeNode != null && !vehiculeNode.isNull()) {
                Vehicule vehicule = new Vehicule();
                vehicule.setVehiculeId(UUID.fromString(vehiculeNode.get("id").asText()));
                vehicule.setChassis(vehiculeNode.get("chassis").asText());
                carteGrise.setVehicule(vehicule);
            }

            JsonNode proprietaireVehiculeNode = jsonNode.get("proprietaireVehicule");
            if (proprietaireVehiculeNode != null && !proprietaireVehiculeNode.isNull()) {
                ProprietaireVehicule proprietaireVehicule = new ProprietaireVehicule();
                proprietaireVehicule.setProprietaireVehiculeId(UUID.fromString(proprietaireVehiculeNode.get("id").asText()));
                JsonNode partenaireNode = proprietaireVehiculeNode.get("partenaire");
                if (partenaireNode != null && !partenaireNode.isNull()) {
                    Partenaire partenaire = new Partenaire();
                    partenaire.setPartenaireId(UUID.fromString(partenaireNode.get("id").asText()));
                    partenaire.setNom(partenaireNode.get("nom").asText());
                    partenaire.setPrenom(partenaireNode.get("prenom").asText());
                    proprietaireVehicule.setPartenaire(partenaire);
                }
                carteGrise.setProprietaireVehicule(proprietaireVehicule);
            }

            JsonNode produitNode = jsonNode.get("produit");
            if (produitNode != null && !produitNode.isNull()) {
                Produit produit = new Produit();
                produit.setProduitId(UUID.fromString(produitNode.get("id").asText()));
                produit.setLibelle(produitNode.get("libelle").asText());
                carteGrise.setProduit(produit);
            }

            JsonNode organisationNode = jsonNode.get("org");
            if (organisationNode != null && !organisationNode.isNull()) {
                Organisation organisation = new Organisation();
                organisation.setOrganisationId(UUID.fromString(organisationNode.get("id").asText()));
                organisation.setNom(organisationNode.get("name").asText());
                carteGrise.setOrganisation(organisation);
            }

            return carteGrise;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }
}
