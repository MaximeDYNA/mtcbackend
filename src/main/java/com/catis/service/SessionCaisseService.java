package com.catis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.catis.model.entity.Caissier;
import com.catis.model.entity.Organisation;
import com.catis.model.entity.SessionCaisse;
import com.catis.model.entity.Utilisateur;
import com.catis.repository.SessionCaisseRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Optional;

@Service
public class SessionCaisseService {

    @Autowired
    private SessionCaisseRepository sessionCaisseRepository;

    @PersistenceContext
    private EntityManager entityManager;

     @Autowired
    private ObjectMapper objectMapper;



    public SessionCaisse enregistrerSessionCaisse(SessionCaisse sessionCaisse) {

            return sessionCaisseRepository.save(sessionCaisse);

    }

    public SessionCaisse findSessionCaisseById(UUID idSessionCaisse) {
        return sessionCaisseRepository.findBySessionCaisseId(idSessionCaisse);
    }
    // Flemming implimented 
    public Optional<SessionCaisse> MainfindSessionCaisseByKeycloakId(String keycloakId) {

        try {
        String queryStr = String.format(
                "SELECT JSON_OBJECT(" +
                        "'active', CASE s.active WHEN b'1' THEN 1 ELSE 0 END, " +
                        "'sessionCaisseId', s.id, " +
                        "'organisation', JSON_OBJECT( " +
                        "'organisationId', o.id, " +
                        "'name', o.name, " +
                        "'nom', o.nom, " +
                        "'adress', o.adress " +
                        "), " +
                        "'caissier', JSON_OBJECT( " +
                        "'caissierId', cs.id, " +
                        "'nom', cs.nom, " +
                        "'user', JSON_OBJECT( " +
                        "'utilisateurId', u.id, " +
                        "'keycloakId', u.keycloak_id, " +
                        "'login', u.login " +
                        ") " +
                        ") " +
                        ") as json " +
                        "FROM t_sessioncaisse s " +
                        "JOIN t_organisation o ON o.id = s.organisation_id " +
                        "JOIN t_caissier cs ON cs.id = s.caissier_id " +
                        "JOIN t_utilisateur u ON u.id = cs.user_id " +
                        "WHERE s.active = true AND u.keycloak_id = '%s'",
                keycloakId
        );

        Query query = entityManager.createNativeQuery(queryStr);

        List<Object> result = query.getResultList();

        if (result.isEmpty()) {
            return Optional.empty();
        }
        // Assuming there's only one result as the query should return a unique result
        return Optional.ofNullable(parseResultToSessionCaisse(result.get(0)));
        // Object result = query.getSingleResult();

      } catch (NoResultException e) {
            // This catch block is now redundant since getResultList() does not throw NoResultException
            return Optional.empty();
        }
        // return Optional.ofNullable(parseResultToSessionCaisse(result));
    }

     private SessionCaisse parseResultToSessionCaisse(Object result) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(result.toString());

            SessionCaisse sessionCaisse = new SessionCaisse();
            sessionCaisse.setSessionCaisseId(UUID.fromString(jsonNode.get("sessionCaisseId").asText()));
            sessionCaisse.setActive(jsonNode.get("active").asInt() == 1);

            Organisation organisation = new Organisation();
            JsonNode organisationNode = jsonNode.get("organisation");
            organisation.setOrganisationId(UUID.fromString(organisationNode.get("organisationId").asText()));
            organisation.setName(organisationNode.get("name").asText());
            organisation.setNom(organisationNode.get("nom").asText());
            organisation.setAdress(organisationNode.get("adress").asText());
            sessionCaisse.setOrganisation(organisation);

            Caissier caissier = new Caissier();
            JsonNode caissierNode = jsonNode.get("caissier");
            caissier.setCaissierId(UUID.fromString(caissierNode.get("caissierId").asText()));
            caissier.setNom(caissierNode.get("nom").asText());

            Utilisateur user = new Utilisateur();
            JsonNode userNode = caissierNode.get("user");
            user.setUtilisateurId(UUID.fromString(userNode.get("utilisateurId").asText()));
            user.setKeycloakId(userNode.get("keycloakId").asText());
            user.setLogin(userNode.get("login").asText());
            caissier.setUser(user);

            sessionCaisse.setCaissier(caissier);

            return sessionCaisse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public SessionCaisse findSessionCaisseByKeycloakId(String keycloakId) {
        SessionCaisse sessionCaisse = sessionCaisseRepository.findByActiveTrueAndCaissier_User_KeycloakId(keycloakId);

        return sessionCaisse;
    }

    public SessionCaisse findSessionCaisseByUserId(UUID userId) {
        return sessionCaisseRepository.findByActiveTrueAndCaissier_User_UtilisateurId(userId);
    }

    public SessionCaisse findActiveSessionCaissierById(UUID caissierId) {
        return sessionCaisseRepository.findByActiveTrueAndCaissierCaissierId(caissierId);
    }

    @Transactional
    public SessionCaisse fermerSessionCaisse(UUID sessionCaisseId, double montantFermeture) {
        SessionCaisse sessionCaisse = sessionCaisseRepository.findBySessionCaisseId(sessionCaisseId);

        sessionCaisse.setMontantfermeture(montantFermeture);
        sessionCaisse.setActive(false);
        sessionCaisse.setDateHeureFermeture(new Date());
        sessionCaisse = sessionCaisseRepository.save(sessionCaisse);


        return sessionCaisse;
    }

    public List<SessionCaisse> getAll(){
        List<SessionCaisse> sessionCaisses = new ArrayList<>();

        sessionCaisseRepository.findByActiveStatusTrue().forEach(sessionCaisses::add);

        return sessionCaisses;
    }

    public List<SessionCaisse> getAll(Pageable pageable){
        List<SessionCaisse> sessionCaisses = new ArrayList<>();

        sessionCaisseRepository.findByActiveStatusTrue(pageable).forEach(sessionCaisses::add);

        return sessionCaisses;
    }


}
