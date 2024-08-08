package com.catis.repository.nativeQueries;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.catis.model.entity.Energie;
import com.catis.model.entity.MarqueVehicule;
import com.catis.model.entity.Organisation;
import com.catis.model.entity.Vehicule;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class VehiculeCustomRepositoryImpl implements VehiculeCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Vehicule> findByChassisContaining(String chassis, Pageable pageable) {
        String sql = String.format("SELECT JSON_OBJECT(" +
                "'activeStatus', CASE v.active_status WHEN b'1' THEN 1 ELSE 0 END, " +
                "'createdBy', v.created_by, " +
                "'modifiedBy', v.modified_by, " +
                "'vehiculeId', v.id, " +
                "'carrosserie', v.carrosserie, " +
                "'placeAssise', v.place_assise, " +
                "'chassis', v.chassis, " +
                "'premiereMiseEnCirculation', v.premiere_mise_en_circulation, " +
                "'puissAdmin', v.puiss_admin, " +
                "'poidsTotalCha', v.poids_total_cha, " +
                "'poidsVide', v.poids_vide, " +
                "'chargeUtile', v.charge_utile, " +
                "'cylindre', v.cylindre, " +
                "'score', v.score, " +
                "'marqueVehicule', JSON_OBJECT('id', mv.id, 'libelle', mv.libelle), " +
                "'energie', JSON_OBJECT('id', e.id, 'libelle', e.libelle), " +
                "'organisation', JSON_OBJECT('id', o.id, 'name', o.name) " +
                ") AS vehiculeDetails " +
                "FROM t_vehicule v " +
                "LEFT JOIN t_marquevehicule mv ON v.marque_vehicule_id = mv.id " +
                "LEFT JOIN t_energie e ON v.energie_id = e.id " +
                "LEFT JOIN t_organisation o ON v.organisation_id = o.id " +
                "WHERE v.active_status = true " +
                "AND v.chassis LIKE '%%%s%%' " +
                "ORDER BY v.created_date DESC " +
                "LIMIT %d OFFSET %d", chassis, pageable.getPageSize(), pageable.getOffset());
        
                Query query = entityManager.createNativeQuery(sql);

                String countQueryStr = "select count(*) from t_vehicule v where v.active_status=true ";
                 Query countQuery = entityManager.createNativeQuery(countQueryStr);
                 long totalRows = ((Number) countQuery.getSingleResult()).longValue();

                 List<Object[]> results = query.getResultList();
                 List<Vehicule> vehicules = new ArrayList<>();
         
                 for (Object result : results) {
                     String json = (String) result;
         
                     // Perform entity mapping directly within the loop
                     Vehicule vehicule = mapJsonToVehicule(json);
                     vehicules.add(vehicule);
                 }

        return new PageImpl<>(vehicules, pageable, totalRows);
    }


    private Vehicule mapJsonToVehicule(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
    
            Vehicule vehicule = new Vehicule();
            vehicule.setActiveStatus(jsonNode.get("activeStatus").asBoolean());
            vehicule.setCreatedBy(jsonNode.get("createdBy").asText());
            vehicule.setModifiedBy(jsonNode.get("modifiedBy").asText());
    
            // Handling UUID fields
            vehicule.setVehiculeId(parseUUID(jsonNode, "vehiculeId"));
            vehicule.setMarqueVehicule(parseMarqueVehicule(jsonNode));
            vehicule.setEnergie(parseEnergie(jsonNode));
            vehicule.setOrganisation(parseOrganisation(jsonNode));
    
            vehicule.setCarrosserie(jsonNode.get("carrosserie").asText());
            vehicule.setPlaceAssise(jsonNode.get("placeAssise").asInt());
            vehicule.setChassis(jsonNode.get("chassis").asText());
            vehicule.setPremiereMiseEnCirculation(DateParser(jsonNode.get("premiereMiseEnCirculation").asText()));
            vehicule.setPuissAdmin(jsonNode.get("puissAdmin").asInt());
            vehicule.setPoidsTotalCha(jsonNode.get("poidsTotalCha").asInt());
            vehicule.setPoidsVide(jsonNode.get("poidsVide").asInt());
            vehicule.setChargeUtile(jsonNode.get("chargeUtile").asInt());
            vehicule.setCylindre(jsonNode.get("cylindre").asInt());
            vehicule.setScore(jsonNode.get("score").asDouble());
    
            return vehicule;
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace(); // Handle exception appropriately
            return null;
        }
    }

    // Helper method to map JSON string to Vehicule entity
// Helper method to parse UUID from JSON node
private UUID parseUUID(JsonNode jsonNode, String fieldName) {
    if (jsonNode.hasNonNull(fieldName)) {
        return UUID.fromString(jsonNode.get(fieldName).asText());
    }
    return null;
}

// Helper method to parse MarqueVehicule from JSON node
private MarqueVehicule parseMarqueVehicule(JsonNode jsonNode) {
    JsonNode marqueVehiculeNode = jsonNode.get("marqueVehicule");
    if (marqueVehiculeNode != null && !marqueVehiculeNode.isNull()) {
        MarqueVehicule marqueVehicule = new MarqueVehicule();
        marqueVehicule.setMarqueVehiculeId(parseUUID(marqueVehiculeNode, "id"));
        marqueVehicule.setLibelle(marqueVehiculeNode.get("libelle").asText());
        return marqueVehicule;
    }
    return null;
}

// Helper method to parse Energie from JSON node
private Energie parseEnergie(JsonNode jsonNode) {
    JsonNode energieNode = jsonNode.get("energie");
    if (energieNode != null && !energieNode.isNull()) {
        Energie energie = new Energie();
        energie.setEnergieId(parseUUID(energieNode, "id"));
        energie.setLibelle(energieNode.get("libelle").asText());
        return energie;
    }
    return null;
}

// Helper method to parse Organisation from JSON node
private Organisation parseOrganisation(JsonNode jsonNode) {
    JsonNode organisationNode = jsonNode.get("organisation");
    if (organisationNode != null && !organisationNode.isNull()) {
        Organisation organisation = new Organisation();
        organisation.setOrganisationId(parseUUID(organisationNode, "id"));
        organisation.setNom(organisationNode.get("name").asText());
        return organisation;
    }
    return null;
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
