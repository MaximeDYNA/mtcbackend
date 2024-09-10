package com.catis.repository.nativeQueries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.catis.controller.SseController;
import com.catis.model.control.Control;
import com.catis.model.control.GieglanFile;
import com.catis.model.entity.CarteGrise;
import com.catis.model.entity.CategorieProduit;
import com.catis.model.entity.CategorieVehicule;
import com.catis.model.entity.Energie;
import com.catis.model.entity.Formule;
import com.catis.model.entity.Inspection;
import com.catis.model.entity.MarqueVehicule;
import com.catis.model.entity.Mesure;
import com.catis.model.entity.Partenaire;
import com.catis.model.entity.Produit;
import com.catis.model.entity.ProprietaireVehicule;
import com.catis.model.entity.RapportDeVisite;
import com.catis.model.entity.Seuil;
import com.catis.model.entity.Vehicule;
import com.catis.model.entity.VerbalProcess;
import com.catis.model.entity.Visite;
import com.catis.model.entity.Visite.TestResult;
import com.catis.objectTemporaire.GieglanFileIcon;
import com.catis.objectTemporaire.NewListView;
import com.catis.objectTemporaire.ProduitCategorieTest;
import com.catis.objectTemporaire.Utils;
import com.catis.service.GieglanFileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

// flemming implimented 
public class VisiteNativeQueryRepositoryImpl implements VisiteNativeQueryRepository {

    @PersistenceContext
    private EntityManager entityManager;

     @Autowired
    private GieglanFileService gieglanFileService;

    @Autowired
    private ObjectMapper objectMapper;

      private static Logger log = LoggerFactory.getLogger(VisiteNativeQueryRepositoryImpl.class);


      public Page<NewListView> endedMainVisitList(UUID organisationId, Pageable pageable) {
        String queryStr = String.format("SELECT " +
                "JSON_OBJECT(" +
                "'createdDate', v.created_date, " +
                "'contreVisite', CASE v.contre_visite WHEN  b'1' THEN 1 ELSE 0 END, " +
                "'encours', CASE v.encours WHEN b'1' THEN 1 ELSE 0 END, " +
                "'statutVisite', v.statut, " +
                "'idVisite', v.id, " +
                "'conformityTest', v.is_conform, " +
                "'documents', v.document, " +
                "'carteGrise', JSON_OBJECT( " +
                "    'carteGriseId', cg.id, " +
                "    'numImmatriculation', cg.num_immatriculation, " +
                "    'preImmatriculation', cg.pre_immatriculation, " +
                "    'dateDebutValid', cg.date_debut_valid, " +
                "    'dateFinValid', cg.date_fin_valid, " +
                "    'ssdt_id', cg.ssdt_id, " +
                "    'commune', cg.commune, " +
                "    'montantPaye', cg.montant_paye, " +
                "    'vehiculeGage', CASE cg.vehicule_gage WHEN  b'1' THEN 1 ELSE 0 END, " +
                "    'genreVehicule', cg.genre_vehicule, " +
                "    'enregistrement', cg.enregistrement, " +
                "    'type', cg.type, " +
                "    'dateDelivrance', cg.date_delivrance, " +
                "    'lieuDedelivrance', cg.lieu_dedelivrance, " +
                "    'centre_ssdt', cg.centre_ssdt, " +
                "    'proprietaireVehicule', JSON_OBJECT( " +
                "        'proprietaireVehiculeId', pv.id, " +
                "        'partenaire', JSON_OBJECT( " +
                "            'partenaireId', p.id, " +
                "            'nom', p.nom, " +
                "            'prenom', p.prenom " +
                "        ), " +
                "        'score', pv.score, " +
                "        'description', pv.description " +
                "    ), " +
                "    'produit', JSON_OBJECT( " +
                "        'produitId', pr.id, " +
                "        'libelle', pr.libelle, " +
                "        'description', pr.description, " +
                "        'prix', pr.prix, " +
                "        'delaiValidite', pr.delai_validite, " +
                "        'img', pr.img, " +
                "        'categorieProduit', JSON_OBJECT( " +
                "            'categorieProduitId', cp.id, " +
                "            'libelle', cp.libelle, " +
                "            'description', cp.description " +
                "        ), " +
                "        'categorieVehicule', JSON_OBJECT( " +
                "            'id', cv.id, " +
                "            'type', cv.type " +
                "        ) " +
                "    ) " +
                ") " +
                ") AS json_result " +
                "FROM t_visite v " +
                "JOIN t_cartegrise cg ON v.carte_grise_id = cg.id " +
                "JOIN t_proprietairevehicule pv ON cg.proprietaire_vehicule_id = pv.id " +
                "JOIN t_partenaire p ON pv.partenaire_id = p.id " +
                "JOIN t_produit pr ON cg.produit_id = pr.id " +
                "JOIN t_categorieproduit cp ON pr.categorie_produit_id = cp.id " +
                "JOIN categorie_vehicule cv ON pr.categorie_vehicule_id = cv.id " +
                "WHERE v.organisation_id = '%s' " +
                "AND    v.encours=0 " +
                "AND    v.active_status=1 order by v.created_date desc " +
                "LIMIT %d OFFSET %d", organisationId, pageable.getPageSize(), pageable.getOffset());

        Query query = entityManager.createNativeQuery(queryStr);

         // Total count query
         String countQueryStr = String.format("SELECT COUNT(*) FROM t_visite v WHERE v.encours=0 and v.active_status=1 and  v.organisation_id = '%s'", organisationId);
         Query countQuery = entityManager.createNativeQuery(countQueryStr);
         long totalRows = ((Number) countQuery.getSingleResult()).longValue();

        List<Object> rawResults = query.getResultList();
        int numberOfResults = rawResults.size();
        log.info("query retured " + numberOfResults);

        List<NewListView> newListViews = new ArrayList<>();


        for (Object rawResult : rawResults) {
            try {
                String jsonString = (String) rawResult;
                JsonNode jsonNode = objectMapper.readTree(jsonString);
                MyJsonToEntityMapper mapper = new MyJsonToEntityMapper();

                JsonNode partenaireNode = jsonNode.path("carteGrise").path("proprietaireVehicule");

                log.info("creating partner started");
                Partenaire partenaire = mapper.mapToPartenaire(partenaireNode);


                log.info("creating partner finished, propertaire vehicle started");
                JsonNode proprietaireVehiculeNode = jsonNode.path("carteGrise").path("proprietaireVehicule");
                ProprietaireVehicule proprietaireVehicule = mapper.mapToProprietaireVehicule(proprietaireVehiculeNode);

                proprietaireVehicule.setPartenaire(partenaire);
                log.info("creating propertaire vehicule finished, cartegrise started");

               
                // Construct CarteGrise instance
                CarteGrise carteGrise = mapper.mapToCarteGrise(jsonNode);
                carteGrise.setProprietaireVehicule(proprietaireVehicule);

                log.info("created carteGrise successfully, product started");

                JsonNode produitNode = jsonNode .path("carteGrise").path("produit");
                Produit produit = mapper.mapToProduit(produitNode);
                

                log.info("created carteGrise product");
                JsonNode categoryproduitNode = jsonNode .path("carteGrise").path("produit").path("categorieProduit");
                CategorieProduit categoryproduit = mapper.mapToCategorieProduit(categoryproduitNode);
                produit.setCategorieProduit(categoryproduit);
                log.info("created categoryproduit");
                
                JsonNode categorieVehiculeNode = jsonNode.path("carteGrise").path("produit").path("categorieVehicule");
                CategorieVehicule categorieVehicule = mapper.mapToCategorieVehicule(categorieVehiculeNode);
                produit.setCategorieVehicule(categorieVehicule);
                // carteGrise.setProduit(produit);
                log.info("created categorie vehicle");
                

                log.info("creating visite");
                Visite visite = new Visite();
                visite.setIdVisite(UUID.fromString(jsonNode.get("idVisite").asText()));
                visite.setContreVisite(jsonNode.get("contreVisite").asBoolean());
                visite.setStatut(jsonNode.get("statutVisite").asInt());
                visite.setIsConform(jsonNode.get("conformityTest").asInt());
                visite.setEncours(jsonNode.get("encours").asBoolean());
                visite.setDocument(jsonNode.get("documents").asText());
                visite.setCreatedDate(LocalDateTime.parse(jsonNode.get("createdDate").asText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")));
                
                
                    visite.setCarteGrise(carteGrise);                

                log.info("visite created associating inspection");

                log.info("creating new list view");

                NewListView newListView = new NewListView();
                newListView.setId(visite.getIdVisite());
                newListView.setCategorie(produit);
                newListView.setType(visite.typeRender());
                newListView.setReference(visite.getCarteGrise().getNumImmatriculation());
                newListView.setClient((visite.getCarteGrise().getProprietaireVehicule()
                                .getPartenaire()
                                .getNom()
                                == null
                                ? null : visite.getCarteGrise().getProprietaireVehicule()
                                .getPartenaire()
                                .getNom()));

                newListView.setCreatedDate(visite.getCreatedDate());
                newListView.setStatut(getHTML(visite));
                newListView.setStatutVisite(visite.getStatut());
                newListView.setIdVisite(visite.getIdVisite());
                newListView.setContreVisite(visite.isContreVisite());
                newListView.setCarteGrise(visite.getCarteGrise());
                newListView.setCreatedAt(Utils.parseDate(visite.getCreatedDate()));
                newListView.setDate(visite.getCreatedDate().format(SseController.dateTimeFormatter));

                newListViews.add(newListView);
            } catch (JsonProcessingException e) {
                log.info("encountered exception");
                e.printStackTrace();
            }
        }

        return new PageImpl<>(newListViews, pageable, totalRows);
    }

   
 



      public Page<NewListView> searchedVisitMainListstatus(String search, UUID orgId, int status, Pageable pageable) {
        String queryStr = String.format("SELECT " +
                "JSON_OBJECT(" +
                "'createdDate', v.created_date, " +
                "'contreVisite', CASE v.contre_visite WHEN  b'1' THEN 1 ELSE 0 END, " +
                "'encours', CASE v.encours WHEN b'1' THEN 1 ELSE 0 END, " +
                "'statutVisite', v.statut, " +
                "'idVisite', v.id, " +
                "'conformityTest', v.is_conform, " +
                "'documents', v.document, " +
                "'freinage', v.freinage, " +
                "'pollution', v.pollution, "+
                "'reglophare', v.reglophare, " +
                "'ripage', v.ripage," +
                "'suspension', v.suspension, " +
                "'visuel', v.visuel, " +
                "'verbalStatus', IF(v.statut != 0 and v.statut !=1, CASE vb.status  WHEN b'1' THEN 1 ELSE 0 END, NULL), " +
                "'verbal_process_id', IF(v.statut != 0 and v.statut !=1, vb.id,NULL)," +
                "'Inspection', IF(v.statut != 0 and v.statut !=1, JSON_OBJECT( " +
				" 'id', i.id, " +
				" 'activeStatus', CASE  i.active_status WHEN b'1' THEN 1 ELSE 0 END, " +
				" 'bestPlate', i.best_plate, " +
				" 'chassis', i.chassis, " +
				" 'date_debut', i.date_debut, " +
				" 'date_fin', i.date_fin, " +
				" 'distance_percentage', i.distance_percentage, " +
				" 'essieux', i.essieux, " + 
				" 'kilometrage', i.kilometrage " +
            " ), NULL), " +
                "'carteGrise', JSON_OBJECT( " +
                "    'carteGriseId', cg.id, " +
                "    'numImmatriculation', cg.num_immatriculation, " +
                "    'preImmatriculation', cg.pre_immatriculation, " +
                "    'dateDebutValid', cg.date_debut_valid, " +
                "    'dateFinValid', cg.date_fin_valid, " +
                "    'ssdt_id', cg.ssdt_id, " +
                "    'commune', cg.commune, " +
                "    'montantPaye', cg.montant_paye, " +
                "    'vehiculeGage', CASE cg.vehicule_gage WHEN  b'1' THEN 1 ELSE 0 END, " +
                "    'genreVehicule', cg.genre_vehicule, " +
                "    'enregistrement', cg.enregistrement, " +
                "    'type', cg.type, " +
                "    'dateDelivrance', cg.date_delivrance, " +
                "    'lieuDedelivrance', cg.lieu_dedelivrance, " +
                "    'centre_ssdt', cg.centre_ssdt, " +
                "    'proprietaireVehicule', JSON_OBJECT( " +
                "        'proprietaireVehiculeId', pv.id, " +
                "        'partenaire', JSON_OBJECT( " +
                "            'partenaireId', p.id, " +
                "            'nom', p.nom, " +
                "            'prenom', p.prenom " +
                "        ), " +
                "        'score', pv.score, " +
                "        'description', pv.description " +
                "    ), " +
                "    'vehicule',IF(v.statut != 0, JSON_OBJECT( " +
                "        'vehiculeId', vh.id, " +
                "        'typeVehicule', vh.type_vehicule, " +
                "        'carrosserie', vh.carrosserie, " +
                "        'placeAssise', vh.place_assise, " +
                "        'chassis', vh.chassis, " +
                "        'premiereMiseEnCirculation', vh.premiere_mise_en_circulation, " +
                "        'puissAdmin', vh.puiss_admin, " +
                "        'poidsTotalCha', vh.poids_total_cha, " +
                "        'poidsVide', vh.poids_vide, " +
                "        'chargeUtile', vh.charge_utile, " +
                "        'cylindre', vh.cylindre, " +
                "        'score', vh.score, " +
                "        'marqueVehicule', JSON_OBJECT( " +
                "            'marqueVehiculeId', mv.id, " +
                "            'libelle', mv.libelle, " +
                "            'description', mv.description " +
                "        ), " +
                "        'energie', JSON_OBJECT( " +
                "            'energieId', e.id, " +
                "            'libelle', e.libelle " +
                "        ) " +
                " ), NULL), " +
                "    'produit', JSON_OBJECT( " +
                "        'produitId', pr.id, " +
                "        'libelle', pr.libelle, " +
                "        'description', pr.description, " +
                "        'prix', pr.prix, " +
                "        'delaiValidite', pr.delai_validite, " +
                "        'img', pr.img, " +
                "        'categorieProduit', JSON_OBJECT( " +
                "            'categorieProduitId', cp.id, " +
                "            'libelle', cp.libelle, " +
                "            'description', cp.description " +
                "        ), " +
                "        'categorieVehicule', JSON_OBJECT( " +
                "            'id', cv.id, " +
                "            'type', cv.type " +
                "        ) " +
                "    ) " +
                ") " +
                ") AS json_result " +
                "FROM t_visite v " +
                "LEFT JOIN t_inspection i on i.visite_id = v.id AND v.statut != 0 and v.statut !=1 " +
                "LEFT JOIN verbal_process vb on vb.visite_id = v.id AND v.statut != 0 and v.statut !=1 " +
                "JOIN t_cartegrise cg ON v.carte_grise_id = cg.id " +
                "JOIN t_proprietairevehicule pv ON cg.proprietaire_vehicule_id = pv.id " +
                "JOIN t_partenaire p ON pv.partenaire_id = p.id " +
                "LEFT JOIN t_vehicule vh ON cg.vehicule_id = vh.id AND v.statut != 0 " +
                "LEFT JOIN t_marquevehicule mv ON vh.marque_vehicule_id = mv.id AND v.statut != 0 " +
                "LEFT JOIN t_energie e ON vh.energie_id = e.id AND v.statut != 0 " +
                "JOIN t_produit pr ON cg.produit_id = pr.id " +
                "JOIN t_categorieproduit cp ON pr.categorie_produit_id = cp.id " +
                "JOIN categorie_vehicule cv ON pr.categorie_vehicule_id = cv.id " +
                "WHERE v.organisation_id = '%s' " +
                "AND    v.encours=1 " +
                "AND    v.statut=%d " +
                "AND (" +
                "    (COALESCE('%s', '') = '' OR LOWER(cg.num_immatriculation) LIKE LOWER(CONCAT('%%', '%s', '%%'))) " +
                "    OR (COALESCE('%s', '') = '' OR LOWER(p.nom) LIKE LOWER(CONCAT('%%', '%s', '%%'))) " +
                ") " +
                "AND    v.active_status=1 order by v.created_date desc " +
                "LIMIT %d OFFSET %d", orgId, status, search, search, search, search, pageable.getPageSize(), pageable.getOffset());
    

        Query query = entityManager.createNativeQuery(queryStr);

         // Total count query
         String countQueryStr = String.format("SELECT COUNT(*) FROM t_visite v WHERE v.encours=1 and v.active_status=1  and v.organisation_id = '%s' and v.statut=%d", orgId, status);
         Query countQuery = entityManager.createNativeQuery(countQueryStr);
         long totalRows = ((Number) countQuery.getSingleResult()).longValue();

        List<Object> rawResults = query.getResultList();
        List<NewListView> newListViews = new ArrayList<>();


        for (Object rawResult : rawResults) {
            try {
                String jsonString = (String) rawResult;
                JsonNode jsonNode = objectMapper.readTree(jsonString);
                MyJsonToEntityMapper mapper = new MyJsonToEntityMapper();

                JsonNode partenaireNode = jsonNode.path("carteGrise").path("proprietaireVehicule");

                log.info("creating partner started");
                Partenaire partenaire = mapper.mapToPartenaire(partenaireNode);


                log.info("creating partner finished, propertaire vehicle started");
                JsonNode proprietaireVehiculeNode = jsonNode.path("carteGrise").path("proprietaireVehicule");
                ProprietaireVehicule proprietaireVehicule = mapper.mapToProprietaireVehicule(proprietaireVehiculeNode);

                proprietaireVehicule.setPartenaire(partenaire);
                log.info("creating propertaire vehicule finished, cartegrise started");

               
                CarteGrise carteGrise = mapper.mapToCarteGrise(jsonNode);
                carteGrise.setProprietaireVehicule(proprietaireVehicule);

                log.info("created carteGrise successfully, product started");

                JsonNode produitNode = jsonNode .path("carteGrise").path("produit");
                Produit produit = mapper.mapToProduit(produitNode);
                

                log.info("created carteGrise product");
                JsonNode categoryproduitNode = jsonNode .path("carteGrise").path("produit").path("categorieProduit");
                CategorieProduit categoryproduit = mapper.mapToCategorieProduit(categoryproduitNode);
                produit.setCategorieProduit(categoryproduit);
                log.info("created categoryproduit");
                
                JsonNode categorieVehiculeNode = jsonNode.path("carteGrise").path("produit").path("categorieVehicule");
                CategorieVehicule categorieVehicule = mapper.mapToCategorieVehicule(categorieVehiculeNode);
                produit.setCategorieVehicule(categorieVehicule);
                carteGrise.setProduit(produit);
                log.info("created categorie vehicle");
                
                
                if(jsonNode.get("carteGrise").has("vehicule") && !jsonNode.get("carteGrise").get("vehicule").isNull()){

                JsonNode vehiculeNode = jsonNode.path("carteGrise").path("vehicule");
                Vehicule vehicule = mapper.mapToVehicule(vehiculeNode);
               
                JsonNode energieNode = jsonNode.path("carteGrise").path("vehicule").path("energie");
                Energie energie = mapper.mapToEnergie(energieNode);
                
                vehicule.setEnergie(energie);
                
                JsonNode marqueVehiculeNode = jsonNode.path("carteGrise").path("vehicule").path("marqueVehicule");
                MarqueVehicule marqueVehicule = mapper.mapToMarqueVehicule(marqueVehiculeNode);
                vehicule.setMarqueVehicule(marqueVehicule);

                carteGrise.setVehicule(vehicule);

                log.info("created  vehicle");
                

                }

               
                log.info("creating visite");
                Visite visite = new Visite();
                visite.setIdVisite(UUID.fromString(jsonNode.get("idVisite").asText()));
                visite.setContreVisite(jsonNode.get("contreVisite").asBoolean());
                visite.setStatut(jsonNode.get("statutVisite").asInt());
                visite.setIsConform(jsonNode.get("conformityTest").asInt());
                visite.setEncours(jsonNode.get("encours").asBoolean());
                visite.setDocument(jsonNode.get("documents").asText());
                visite.setCreatedDate(LocalDateTime.parse(jsonNode.get("createdDate").asText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")));
                
                String ripageString = jsonNode.get("ripage").asText();
                TestResult ripage = TestResult.valueOf(ripageString);
                visite.setRipage(ripage);

                
                String freinageString = jsonNode.get("freinage").asText();
                TestResult freinage = TestResult.valueOf(freinageString);
                visite.setFreinage(freinage);

                String pollutionString = jsonNode.get("pollution").asText();
                TestResult pollution = TestResult.valueOf(pollutionString);
                visite.setPollution(pollution);

                String reglophareString = jsonNode.get("reglophare").asText();
                TestResult reglophare = TestResult.valueOf(reglophareString);
                visite.setReglophare(reglophare);

                String suspensionString = jsonNode.get("suspension").asText();
                TestResult suspension = TestResult.valueOf(suspensionString);
                visite.setSuspension(suspension);

                String visuelString = jsonNode.get("visuel").asText();
                TestResult visuel = TestResult.valueOf(visuelString);
                visite.setVisuel(visuel);
                visite.setCarteGrise(carteGrise);

                if(jsonNode.has("verbal_process_id") && !jsonNode.get("verbal_process_id").isNull()){
                    String id = jsonNode.path("verbal_process_id").asText();
                    Boolean statut = jsonNode.path("verbalStatus").asBoolean();
                    VerbalProcess verbalProcess = mapper.mapToVerbalProcess(id, statut);
                    visite.setProcess(verbalProcess);
                }

                if(jsonNode.has("Inspection") && !jsonNode.get("Inspection").isNull()){
                    JsonNode inspectionNode = jsonNode.path("Inspection");
                    Inspection inspection = mapper.mapToInspection(inspectionNode);
                    visite.setInspection(inspection);
                    log.info("created data binder successfully");
                }
                

                log.info("visite created");


                log.info("creating new list view");

                NewListView newListView = new NewListView();
                newListView.setCategorie(visite.getCarteGrise().getProduit());
                newListView.setId(visite.getIdVisite());
                newListView.setType(visite.typeRender());
                newListView.setReference(visite.getCarteGrise().getNumImmatriculation());
                newListView.setChassis((visite.getCarteGrise().getVehicule()==null
                                ? "": (visite.getCarteGrise().getVehicule().getChassis()==null
                                ? "" : visite.getCarteGrise().getVehicule().getChassis())));

                newListView.setClient((visite.getCarteGrise().getProprietaireVehicule()
                                .getPartenaire()
                                .getNom()
                                == null
                                ? null : visite.getCarteGrise().getProprietaireVehicule()
                                .getPartenaire()
                                .getNom()));

                newListView.setCreatedDate(visite.getCreatedDate());
                newListView.setStatut(getHTML(visite));
                newListView.setStatutVisite(visite.getStatut());
                newListView.setIdVisite(visite.getIdVisite());
                newListView.setContreVisite(visite.isContreVisite());
                newListView.setInspection(visite.getInspection()==null? null : visite.getInspection().getIdInspection());
                newListView.setCarteGrise(visite.getCarteGrise());
                newListView.setCreatedAt(Utils.parseDate(visite.getCreatedDate()));
                newListView.setDate(visite.getCreatedDate().format(SseController.dateTimeFormatter));

                newListViews.add(newListView);
            } catch (JsonProcessingException e) {
                log.info("encountered exception");
                e.printStackTrace();
            }
        }

        return new PageImpl<>(newListViews, pageable, totalRows);
    }
    
    
    


        public Page<NewListView> searchedVisitMainList(String search, UUID orgId, Pageable pageable) {
        String queryStr = String.format("SELECT " +
                "JSON_OBJECT(" +
                "'createdDate', v.created_date, " +
                "'contreVisite', CASE v.contre_visite WHEN  b'1' THEN 1 ELSE 0 END, " +
                "'encours', CASE v.encours WHEN b'1' THEN 1 ELSE 0 END, " +
                "'statutVisite', v.statut, " +
                "'idVisite', v.id, " +
                "'conformityTest', v.is_conform, " +
                "'documents', v.document, " +
                "'freinage', v.freinage, " +
                "'pollution', v.pollution, "+
                "'reglophare', v.reglophare, " +
                "'ripage', v.ripage," +
                "'suspension', v.suspension, " +
                "'visuel', v.visuel, " +
                "'control', JSON_OBJECT( "+
                    "'id', c.id, " +
                    "'status', c.status, " +
                    "'active_status',CASE c.active_status WHEN b'1' THEN 1 ELSE 0 END " +
                    
                "), " + 
                "'verbalStatus', IF(v.statut != 0 and v.statut !=1, CASE vb.status  WHEN b'1' THEN 1 ELSE 0 END, NULL), " +
                "'verbal_process_id', IF(v.statut != 0 and v.statut !=1, vb.id,NULL)," +
                "'Inspection', IF(v.statut != 0 and v.statut !=1, JSON_OBJECT( " +
				" 'id', i.id, " +
				" 'activeStatus', CASE  i.active_status WHEN b'1' THEN 1 ELSE 0 END, " +
				" 'bestPlate', i.best_plate, " +
				" 'chassis', i.chassis, " +
				" 'date_debut', i.date_debut, " +
				" 'date_fin', i.date_fin, " +
				" 'distance_percentage', i.distance_percentage, " +
				" 'essieux', i.essieux, " + 
				" 'kilometrage', i.kilometrage " +
            " ), NULL), " +
                "'carteGrise', JSON_OBJECT( " +
                "    'carteGriseId', cg.id, " +
                "    'numImmatriculation', cg.num_immatriculation, " +
                "    'preImmatriculation', cg.pre_immatriculation, " +
                "    'dateDebutValid', cg.date_debut_valid, " +
                "    'dateFinValid', cg.date_fin_valid, " +
                "    'ssdt_id', cg.ssdt_id, " +
                "    'commune', cg.commune, " +
                "    'montantPaye', cg.montant_paye, " +
                "    'vehiculeGage', CASE cg.vehicule_gage WHEN  b'1' THEN 1 ELSE 0 END, " +
                "    'genreVehicule', cg.genre_vehicule, " +
                "    'enregistrement', cg.enregistrement, " +
                "    'type', cg.type, " +
                "    'dateDelivrance', cg.date_delivrance, " +
                "    'lieuDedelivrance', cg.lieu_dedelivrance, " +
                "    'centre_ssdt', cg.centre_ssdt, " +
                "    'proprietaireVehicule', JSON_OBJECT( " +
                "        'proprietaireVehiculeId', pv.id, " +
                "        'partenaire', JSON_OBJECT( " +
                "            'partenaireId', p.id, " +
                "            'nom', p.nom, " +
                "            'prenom', p.prenom " +
                "        ), " +
                "        'score', pv.score, " +
                "        'description', pv.description " +
                "    ), " +
                "    'vehicule',IF(v.statut != 0, JSON_OBJECT( " +
                "        'vehiculeId', vh.id, " +
                "        'typeVehicule', vh.type_vehicule, " +
                "        'carrosserie', vh.carrosserie, " +
                "        'placeAssise', vh.place_assise, " +
                "        'chassis', vh.chassis, " +
                "        'premiereMiseEnCirculation', vh.premiere_mise_en_circulation, " +
                "        'puissAdmin', vh.puiss_admin, " +
                "        'poidsTotalCha', vh.poids_total_cha, " +
                "        'poidsVide', vh.poids_vide, " +
                "        'chargeUtile', vh.charge_utile, " +
                "        'cylindre', vh.cylindre, " +
                "        'score', vh.score, " +
                "        'marqueVehicule', JSON_OBJECT( " +
                "            'marqueVehiculeId', mv.id, " +
                "            'libelle', mv.libelle, " +
                "            'description', mv.description " +
                "        ), " +
                "        'energie', JSON_OBJECT( " +
                "            'energieId', e.id, " +
                "            'libelle', e.libelle " +
                "        ) " +
                " ), NULL), " +
                "    'produit', JSON_OBJECT( " +
                "        'produitId', pr.id, " +
                "        'libelle', pr.libelle, " +
                "        'description', pr.description, " +
                "        'prix', pr.prix, " +
                "        'delaiValidite', pr.delai_validite, " +
                "        'img', pr.img, " +
                "        'categorieProduit', JSON_OBJECT( " +
                "            'categorieProduitId', cp.id, " +
                "            'libelle', cp.libelle, " +
                "            'description', cp.description " +
                "        ), " +
                "        'categorieVehicule', JSON_OBJECT( " +
                "            'id', cv.id, " +
                "            'type', cv.type " +
                "        ) " +
                "    ) " +
                ") " +
                ") AS json_result " +
                "FROM t_visite v " +
                "LEFT JOIN t_inspection i on i.visite_id = v.id AND v.statut != 0 and v.statut !=1 " +
                "LEFT JOIN verbal_process vb on vb.visite_id = v.id AND v.statut != 0 and v.statut !=1 " +
                "JOIN t_cartegrise cg ON v.carte_grise_id = cg.id " +
                "JOIN control c on  c.carte_grise_id=cg.id " +
                "JOIN t_proprietairevehicule pv ON cg.proprietaire_vehicule_id = pv.id " +
                "JOIN t_partenaire p ON pv.partenaire_id = p.id " +
                "JOIN t_produit pr ON cg.produit_id = pr.id " +
                "LEFT JOIN t_vehicule vh ON cg.vehicule_id = vh.id AND v.statut != 0 " +
                "LEFT JOIN t_marquevehicule mv ON vh.marque_vehicule_id = mv.id AND v.statut != 0 " +
                "LEFT JOIN t_energie e ON vh.energie_id = e.id AND v.statut != 0 " +
                "JOIN t_categorieproduit cp ON pr.categorie_produit_id = cp.id " +
                "JOIN categorie_vehicule cv ON pr.categorie_vehicule_id = cv.id " +
                "WHERE v.organisation_id = '%s' " +
                "AND    v.encours=true " +
                "AND (" +
                "    (COALESCE('%s', '') = '' OR LOWER(cg.num_immatriculation) LIKE LOWER(CONCAT('%%', '%s', '%%'))) " +
                "    OR (COALESCE('%s', '') = '' OR LOWER(p.nom) LIKE LOWER(CONCAT('%%', '%s', '%%'))) " +
                ") " +
                "AND  v.active_status=true  order by v.created_date desc " +
                "LIMIT %d OFFSET %d", orgId, search, search, search, search, pageable.getPageSize(), pageable.getOffset());
        Query query = entityManager.createNativeQuery(queryStr);

         // Total count query
         String countQueryStr = String.format("SELECT COUNT(*) FROM t_visite v WHERE v.encours=1 and v.active_status=1  and v.organisation_id = '%s'", orgId);
         Query countQuery = entityManager.createNativeQuery(countQueryStr);
         long totalRows = ((Number) countQuery.getSingleResult()).longValue();

        List<Object> rawResults = query.getResultList();
        int numberOfResults = rawResults.size();
        log.info("query retured " + numberOfResults);
        List<NewListView> newListViews = new ArrayList<>();


        for (Object rawResult : rawResults) {
            try {
                String jsonString = (String) rawResult;
                JsonNode jsonNode = objectMapper.readTree(jsonString);
                MyJsonToEntityMapper mapper = new MyJsonToEntityMapper();

                JsonNode partenaireNode = jsonNode.path("carteGrise").path("proprietaireVehicule");

                log.info("creating partner started");
                Partenaire partenaire = mapper.mapToPartenaire(partenaireNode);


                log.info("creating partner finished, propertaire vehicle started");
                JsonNode proprietaireVehiculeNode = jsonNode.path("carteGrise").path("proprietaireVehicule");
                ProprietaireVehicule proprietaireVehicule = mapper.mapToProprietaireVehicule(proprietaireVehiculeNode);

                proprietaireVehicule.setPartenaire(partenaire);

                log.info("creating propertaire vehicule finished, cartegrise started");
               
                CarteGrise carteGrise = mapper.mapToCarteGrise(jsonNode);
                carteGrise.setProprietaireVehicule(proprietaireVehicule);

                log.info("created carteGrise successfully, product started");

                JsonNode produitNode = jsonNode .path("carteGrise").path("produit");
                Produit produit = mapper.mapToProduit(produitNode);
                

                log.info("created carteGrise product");
                JsonNode categoryproduitNode = jsonNode .path("carteGrise").path("produit").path("categorieProduit");
                CategorieProduit categoryproduit = mapper.mapToCategorieProduit(categoryproduitNode);
                produit.setCategorieProduit(categoryproduit);
                log.info("created categoryproduit");
                
                JsonNode categorieVehiculeNode = jsonNode.path("carteGrise").path("produit").path("categorieVehicule");
                CategorieVehicule categorieVehicule = mapper.mapToCategorieVehicule(categorieVehiculeNode);
                produit.setCategorieVehicule(categorieVehicule);
                carteGrise.setProduit(produit);
                log.info("created categorie vehicle");


                
                
                
                if(jsonNode.get("carteGrise").has("vehicule") && !jsonNode.get("carteGrise").get("vehicule").isNull()){
                    
                     
                JsonNode vehiculeNode = jsonNode.path("carteGrise").path("vehicule");
                Vehicule vehicule = mapper.mapToVehicule(vehiculeNode);
               
                JsonNode energieNode = jsonNode.path("carteGrise").path("vehicule").path("energie");
                Energie energie = mapper.mapToEnergie(energieNode);
                
                vehicule.setEnergie(energie);
                
                JsonNode marqueVehiculeNode = jsonNode.path("carteGrise").path("vehicule").path("marqueVehicule");
                MarqueVehicule marqueVehicule = mapper.mapToMarqueVehicule(marqueVehiculeNode);
                vehicule.setMarqueVehicule(marqueVehicule);

                carteGrise.setVehicule(vehicule);

                log.info("created  vehicle");


                }
               
                
                log.info("creating visite");
                Visite visite = new Visite();
                visite.setIdVisite(UUID.fromString(jsonNode.get("idVisite").asText()));
                visite.setContreVisite(jsonNode.get("contreVisite").asBoolean());
                visite.setStatut(jsonNode.get("statutVisite").asInt());
                visite.setIsConform(jsonNode.get("conformityTest").asInt());
                visite.setEncours(jsonNode.get("encours").asBoolean());
                visite.setDocument(jsonNode.get("documents").asText());
                visite.setCreatedDate(LocalDateTime.parse(jsonNode.get("createdDate").asText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")));
                
                String ripageString = jsonNode.get("ripage").asText();
                TestResult ripage = TestResult.valueOf(ripageString);
                visite.setRipage(ripage);

                
                String freinageString = jsonNode.get("freinage").asText();
                TestResult freinage = TestResult.valueOf(freinageString);
                visite.setFreinage(freinage);

                String pollutionString = jsonNode.get("pollution").asText();
                TestResult pollution = TestResult.valueOf(pollutionString);
                visite.setPollution(pollution);

                String reglophareString = jsonNode.get("reglophare").asText();
                TestResult reglophare = TestResult.valueOf(reglophareString);
                visite.setReglophare(reglophare);

                String suspensionString = jsonNode.get("suspension").asText();
                TestResult suspension = TestResult.valueOf(suspensionString);
                visite.setSuspension(suspension);

                String visuelString = jsonNode.get("visuel").asText();
                TestResult visuel = TestResult.valueOf(visuelString);
                visite.setVisuel(visuel);
                
                visite.setCarteGrise(carteGrise);

                JsonNode controlNode = jsonNode.path("control");
                Control control = mapper.mapToControl(controlNode);
                visite.setControl(control);

                if(jsonNode.has("verbal_process_id") && !jsonNode.get("verbal_process_id").isNull()){
                    String id = jsonNode.path("verbal_process_id").asText();
                    Boolean status = jsonNode.path("verbalStatus").asBoolean();
                    VerbalProcess verbalProcess = mapper.mapToVerbalProcess(id, status);
                    visite.setProcess(verbalProcess);
                }

                if(jsonNode.has("Inspection") && !jsonNode.get("Inspection").isNull()){
                    JsonNode inspectionNode = jsonNode.path("Inspection");
                    Inspection inspection = mapper.mapToInspection(inspectionNode);
                    visite.setInspection(inspection);
                    log.info("created data binder successfully");
                }


                log.info("visite created");


                log.info("creating new list view");

                NewListView newListView = new NewListView();
                newListView.setId(visite.getIdVisite());
                newListView.setCategorie(visite.getCarteGrise().getProduit());
                newListView.setType(visite.typeRender());
                newListView.setReference(visite.getCarteGrise().getNumImmatriculation());
                newListView.setChassis((visite.getCarteGrise().getVehicule()==null
                                ? "": (visite.getCarteGrise().getVehicule().getChassis()==null
                                ? "" : visite.getCarteGrise().getVehicule().getChassis())));

                newListView.setClient((visite.getCarteGrise().getProprietaireVehicule()
                                .getPartenaire()
                                .getNom()
                                == null
                                ? null : visite.getCarteGrise().getProprietaireVehicule()
                                .getPartenaire()
                                .getNom()));

                newListView.setCreatedDate(visite.getCreatedDate());
                newListView.setStatut(getHTML(visite));
                newListView.setStatutVisite(visite.getStatut());
                newListView.setIdVisite(visite.getIdVisite());
                newListView.setContreVisite(visite.isContreVisite());
                newListView.setInspection(visite.getInspection()==null? null : visite.getInspection().getIdInspection());
                newListView.setCarteGrise(visite.getCarteGrise());
                newListView.setCreatedAt(Utils.parseDate(visite.getCreatedDate()));
                newListView.setDate(visite.getCreatedDate().format(SseController.dateTimeFormatter));
                newListView.setDocument(visite.getDocument());

                newListViews.add(newListView);
            } catch (JsonProcessingException e) {
                log.info("encountered exception");
                e.printStackTrace();
            }
        }

        return new PageImpl<>(newListViews, pageable, totalRows);
    }
   





    public Page<NewListView> MainlistParStatus(int status, UUID orgId, Pageable pageable) {
        String queryStr = String.format("SELECT " +
                "JSON_OBJECT(" +
                "'createdDate', v.created_date, " +
                "'contreVisite', CASE v.contre_visite WHEN  b'1' THEN 1 ELSE 0 END, " +
                "'encours', CASE v.encours WHEN b'1' THEN 1 ELSE 0 END, " +
                "'statutVisite', v.statut, " +
                "'idVisite', v.id, " +
                "'conformityTest', v.is_conform, " +
                "'documents', v.document, " +
                "'freinage', v.freinage, " +
                "'pollution', v.pollution, "+
                "'reglophare', v.reglophare, " +
                "'ripage', v.ripage," +
                "'suspension', v.suspension, " +
                "'visuel', v.visuel, " +
                "'carteGrise', JSON_OBJECT( " +
                "    'carteGriseId', cg.id, " +
                "    'numImmatriculation', cg.num_immatriculation, " +
                "    'preImmatriculation', cg.pre_immatriculation, " +
                "    'dateDebutValid', cg.date_debut_valid, " +
                "    'dateFinValid', cg.date_fin_valid, " +
                "    'ssdt_id', cg.ssdt_id, " +
                "    'commune', cg.commune, " +
                "    'montantPaye', cg.montant_paye, " +
                "    'vehiculeGage', CASE cg.vehicule_gage WHEN  b'1' THEN 1 ELSE 0 END, " +
                "    'genreVehicule', cg.genre_vehicule, " +
                "    'enregistrement', cg.enregistrement, " +
                "    'type', cg.type, " +
                "    'dateDelivrance', cg.date_delivrance, " +
                "    'lieuDedelivrance', cg.lieu_dedelivrance, " +
                "    'centre_ssdt', cg.centre_ssdt, " +
                "    'proprietaireVehicule', JSON_OBJECT( " +
                "        'proprietaireVehiculeId', pv.id, " +
                "        'partenaire', JSON_OBJECT( " +
                "            'partenaireId', p.id, " +
                "            'nom', p.nom, " +
                "            'prenom', p.prenom " +
                "        ), " +
                "        'score', pv.score, " +
                "        'description', pv.description " +
                "    ), " +
                "    'vehicule', JSON_OBJECT( " +
                "        'vehiculeId', vh.id, " +
                "        'typeVehicule', vh.type_vehicule, " +
                "        'carrosserie', vh.carrosserie, " +
                "        'placeAssise', vh.place_assise, " +
                "        'chassis', vh.chassis, " +
                "        'premiereMiseEnCirculation', vh.premiere_mise_en_circulation, " +
                "        'puissAdmin', vh.puiss_admin, " +
                "        'poidsTotalCha', vh.poids_total_cha, " +
                "        'poidsVide', vh.poids_vide, " +
                "        'chargeUtile', vh.charge_utile, " +
                "        'cylindre', vh.cylindre, " +
                "        'score', vh.score, " +
                "        'marqueVehicule', JSON_OBJECT( " +
                "            'marqueVehiculeId', mv.id, " +
                "            'libelle', mv.libelle, " +
                "            'description', mv.description " +
                "        ), " +
                "        'energie', JSON_OBJECT( " +
                "            'energieId', e.id, " +
                "            'libelle', e.libelle " +
                "        ) " +
                "    ), " +
                "    'produit', JSON_OBJECT( " +
                "        'produitId', pr.id, " +
                "        'libelle', pr.libelle, " +
                "        'description', pr.description, " +
                "        'prix', pr.prix, " +
                "        'delaiValidite', pr.delai_validite, " +
                "        'img', pr.img, " +
                "        'categorieProduit', JSON_OBJECT( " +
                "            'categorieProduitId', cp.id, " +
                "            'libelle', cp.libelle, " +
                "            'description', cp.description " +
                "        ), " +
                "        'categorieVehicule', JSON_OBJECT( " +
                "            'id', cv.id, " +
                "            'type', cv.type " +
                "        ) " +
                "    ) " +
                ") " +
                ") AS json_result " +
                "FROM t_visite v " +
                "JOIN t_cartegrise cg ON v.carte_grise_id = cg.id " +
                "JOIN t_proprietairevehicule pv ON cg.proprietaire_vehicule_id = pv.id " +
                "JOIN t_partenaire p ON pv.partenaire_id = p.id " +
                "JOIN t_vehicule vh ON cg.vehicule_id = vh.id " +
                "JOIN t_marquevehicule mv ON vh.marque_vehicule_id = mv.id " +
                "JOIN t_energie e ON vh.energie_id = e.id " +
                "JOIN t_produit pr ON cg.produit_id = pr.id " +
                "JOIN t_categorieproduit cp ON pr.categorie_produit_id = cp.id " +
                "JOIN categorie_vehicule cv ON pr.categorie_vehicule_id = cv.id " +
                "WHERE v.organisation_id = '%s' " +
                "AND    v.encours=1 " +
                "AND    v.statut=%d " +
                "AND    v.active_status=1 order by v.created_date desc " +
                "LIMIT %d OFFSET %d", orgId, status, pageable.getPageSize(), pageable.getOffset());

        Query query = entityManager.createNativeQuery(queryStr);

         // Total count query
         String countQueryStr = String.format("SELECT COUNT(*) FROM t_visite v WHERE v.encours=1 and v.active_status=1  and v.organisation_id = '%s' and v.statut=%d", orgId, status);
         Query countQuery = entityManager.createNativeQuery(countQueryStr);
         long totalRows = ((Number) countQuery.getSingleResult()).longValue();

        List<Object> rawResults = query.getResultList();
        List<NewListView> newListViews = new ArrayList<>();


        for (Object rawResult : rawResults) {
            try {
                String jsonString = (String) rawResult;
                JsonNode jsonNode = objectMapper.readTree(jsonString);
                MyJsonToEntityMapper mapper = new MyJsonToEntityMapper();

                JsonNode partenaireNode = jsonNode.path("carteGrise").path("proprietaireVehicule");

                log.info("creating partner started");
                Partenaire partenaire = mapper.mapToPartenaire(partenaireNode);


                log.info("creating partner finished, propertaire vehicle started");
                JsonNode proprietaireVehiculeNode = jsonNode.path("carteGrise").path("proprietaireVehicule");
                ProprietaireVehicule proprietaireVehicule = mapper.mapToProprietaireVehicule(proprietaireVehiculeNode);

                proprietaireVehicule.setPartenaire(partenaire);

                log.info("creating propertaire vehicule finished, cartegrise started");

                CarteGrise carteGrise = mapper.mapToCarteGrise(jsonNode);
                carteGrise.setProprietaireVehicule(proprietaireVehicule);

                log.info("created carteGrise successfully, product started");

                JsonNode produitNode = jsonNode .path("carteGrise").path("produit");
                Produit produit = mapper.mapToProduit(produitNode);
                

                log.info("created carteGrise product");
                JsonNode categoryproduitNode = jsonNode .path("carteGrise").path("produit").path("categorieProduit");
                CategorieProduit categoryproduit = mapper.mapToCategorieProduit(categoryproduitNode);
                produit.setCategorieProduit(categoryproduit);
                log.info("created categoryproduit");
                
                JsonNode categorieVehiculeNode = jsonNode.path("carteGrise").path("produit").path("categorieVehicule");
                CategorieVehicule categorieVehicule = mapper.mapToCategorieVehicule(categorieVehiculeNode);
                produit.setCategorieVehicule(categorieVehicule);
                carteGrise.setProduit(produit);
                log.info("created categorie vehicle");
                
                
                JsonNode vehiculeNode = jsonNode.path("carteGrise").path("vehicule");
                Vehicule vehicule = mapper.mapToVehicule(vehiculeNode);
               
                JsonNode energieNode = jsonNode.path("carteGrise").path("vehicule").path("energie");
                Energie energie = mapper.mapToEnergie(energieNode);
                
                vehicule.setEnergie(energie);
                
                JsonNode marqueVehiculeNode = jsonNode.path("carteGrise").path("vehicule").path("marqueVehicule");
                MarqueVehicule marqueVehicule = mapper.mapToMarqueVehicule(marqueVehiculeNode);
                vehicule.setMarqueVehicule(marqueVehicule);

                carteGrise.setVehicule(vehicule);

                log.info("created  vehicle");


                log.info("creating visite");
                Visite visite = new Visite();
                visite.setIdVisite(UUID.fromString(jsonNode.get("idVisite").asText()));
                visite.setContreVisite(jsonNode.get("contreVisite").asBoolean());
                visite.setStatut(jsonNode.get("statutVisite").asInt());
                visite.setIsConform(jsonNode.get("conformityTest").asInt());
                visite.setEncours(jsonNode.get("encours").asBoolean());
                visite.setDocument(jsonNode.get("documents").asText());
                visite.setCreatedDate(LocalDateTime.parse(jsonNode.get("createdDate").asText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")));

                String ripageString = jsonNode.get("ripage").asText();
                TestResult ripage = TestResult.valueOf(ripageString);
                visite.setRipage(ripage);

                
                String freinageString = jsonNode.get("freinage").asText();
                TestResult freinage = TestResult.valueOf(freinageString);
                visite.setFreinage(freinage);

                String pollutionString = jsonNode.get("pollution").asText();
                TestResult pollution = TestResult.valueOf(pollutionString);
                visite.setPollution(pollution);

                String reglophareString = jsonNode.get("reglophare").asText();
                TestResult reglophare = TestResult.valueOf(reglophareString);
                visite.setReglophare(reglophare);

                String suspensionString = jsonNode.get("suspension").asText();
                TestResult suspension = TestResult.valueOf(suspensionString);
                visite.setSuspension(suspension);

                String visuelString = jsonNode.get("visuel").asText();
                TestResult visuel = TestResult.valueOf(visuelString);
                visite.setVisuel(visuel);
                
                
                visite.setCarteGrise(carteGrise);
                

                log.info("visite created");


                log.info("creating new list view");

                NewListView newListView = new NewListView();
                newListView.setId(visite.getIdVisite());
                newListView.setType(visite.typeRender());
                newListView.setReference(visite.getCarteGrise().getNumImmatriculation());
                newListView.setChassis((visite.getCarteGrise().getVehicule()==null
                                ? "": (visite.getCarteGrise().getVehicule().getChassis()==null
                                ? "" : visite.getCarteGrise().getVehicule().getChassis())));

                newListView.setClient((visite.getCarteGrise().getProprietaireVehicule()
                                .getPartenaire()
                                .getNom()
                                == null
                                ? null : visite.getCarteGrise().getProprietaireVehicule()
                                .getPartenaire()
                                .getNom()));

                newListView.setCreatedDate(visite.getCreatedDate());
                newListView.setStatut(getHTML(visite));
                newListView.setStatutVisite(visite.getStatut());
                newListView.setIdVisite(visite.getIdVisite());
                newListView.setContreVisite(visite.isContreVisite());
                newListView.setInspection(visite.getInspection()==null? null : visite.getInspection().getIdInspection());
                newListView.setCarteGrise(visite.getCarteGrise());
                newListView.setCreatedAt(Utils.parseDate(visite.getCreatedDate()));
                newListView.setDate(visite.getCreatedDate().format(SseController.dateTimeFormatter));
                newListViews.add(newListView);
            } catch (JsonProcessingException e) {
                log.info("encountered exception");
                e.printStackTrace();
            }
        }

        return new PageImpl<>(newListViews, pageable, totalRows);
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




    public List<Visite> LastVisiteWithTestIsOkDirectQuery(UUID controlId, UUID visiteId){
        String sql = String.format(
            "SELECT JSON_OBJECT(" +
            "'createdDate', v.created_date, " +
            "'statut', v.statut, " +
            "'contreVisite', CASE v.contre_visite WHEN b'1' THEN 1 ELSE 0 END, " +
            "'encours', CASE v.encours WHEN b'1' THEN 1 ELSE 0 END, " +
            "'statutVisite', v.statut, " +
            "'idVisite', v.id, " +
            "'conformityTest', v.is_conform, " +
            "'document', v.document, " +
            "'freinage', v.freinage, " +
            "'pollution', v.pollution, " +
            "'reglophare', v.reglophare, " +
            "'ripage', v.ripage, " +
            "'suspension', v.suspension, " +
            "'visuel', v.visuel, " +
            "'rapport_de_visite', JSON_OBJECT(" +
            "'id', r.id, " +
            "'code_message', r.code_message, " +
            "'decision', CASE r.decision WHEN b'1' THEN 1 ELSE 0 END, " +
            "'result', r.result " +
            "), " +
            "'seuil', JSON_OBJECT(" +
            "'id', s.id, " +
            "'code_message', s.code_message, " +
            "'decision', CASE s.decision WHEN b'1' THEN 1 ELSE 0 END, " +
            "'value', s.value, " +
            "'operande', s.operande " +
            "), " +
            "'formule', JSON_OBJECT(" +
            "'id', f.id, " +
            "'description', f.description " +
            "), " +
            "'mesure', JSON_OBJECT(" +
            "'id', m.id, " +
            "'description', m.description, " +
            "'code', m.code " +
            "), " +
            "'gieglan_files', JSON_OBJECT(" +
            "'id', g.id, " +
            "'name', g.name, " +
            "'status', g.status, " +
            "'type', g.type " +
            ") " +
            ") as json " +
            "FROM t_visite v " +
            "JOIN rapport_de_visite r ON v.id = r.visite_id " +
            "JOIN seuil s ON r.seuil_id = s.id " +
            "JOIN formule f ON s.formule_id = f.id " +
            "JOIN t_mesure m ON f.id = m.formule_id " +
            "JOIN gieglan_file g ON r.gieglan_file_id = g.id " +
            "WHERE v.control_id = '%s' " +
            "  AND v.id <> '%s' " +
            "  AND g.is_accept = 1 " +
            "  AND g.status = 'VALIDATED' " +
            "ORDER BY v.created_date DESC",
            controlId.toString(), visiteId.toString()
        );
    
        Query query = entityManager.createNativeQuery(sql);
        List<String> jsonResults = query.getResultList();
    
        List<Visite> visites = jsonResults.stream()
                                          .map(this::convertJsonToVisite)
                                          .collect(Collectors.toList());
    
        log.info("results size: {}", visites.size());
        return visites;
    }

    
    private Visite convertJsonToVisite(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(json);
            
            log.info("creating visite");
            Visite visite = new Visite();
            visite.setIdVisite(UUID.fromString(jsonNode.get("idVisite").asText()));
            visite.setContreVisite(jsonNode.get("contreVisite").asBoolean());
            visite.setStatut(jsonNode.get("statutVisite").asInt());
            visite.setIsConform(jsonNode.get("conformityTest").asInt());
            visite.setEncours(jsonNode.get("encours").asBoolean());
            visite.setDocument(jsonNode.get("document").asText());
            visite.setCreatedDate(LocalDateTime.parse(jsonNode.get("createdDate").asText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")));
            
            visite.setRipage(TestResult.valueOf(jsonNode.get("ripage").asText()));
            visite.setFreinage(TestResult.valueOf(jsonNode.get("freinage").asText()));
            visite.setPollution(TestResult.valueOf(jsonNode.get("pollution").asText()));
            visite.setReglophare(TestResult.valueOf(jsonNode.get("reglophare").asText()));
            visite.setSuspension(TestResult.valueOf(jsonNode.get("suspension").asText()));
            visite.setVisuel(TestResult.valueOf(jsonNode.get("visuel").asText()));
    
            log.info("creating rapport de visite");
            RapportDeVisite rapportDeVisite = new RapportDeVisite();
            JsonNode rapportNode = jsonNode.get("rapport_de_visite");
            rapportDeVisite.setId(UUID.fromString(rapportNode.get("id").asText()));
            rapportDeVisite.setCodeMessage(rapportNode.get("code_message").asText());
            rapportDeVisite.setDecision(rapportNode.get("decision").asBoolean());
            rapportDeVisite.setResult(rapportNode.get("result").asText());
            rapportDeVisite.setVisite(visite);
    
            log.info("creating seuil");
            Seuil seuil = new Seuil();
            JsonNode seuilNode = jsonNode.get("seuil");
            seuil.setId(seuilNode.get("id").asText());
            seuil.setCodeMessage(seuilNode.get("code_message").asText());
            seuil.setDecision(seuilNode.get("decision").asBoolean());
            seuil.setValue(seuilNode.get("value").asDouble());
            seuil.setOperande(seuilNode.get("operande").asText());
            rapportDeVisite.setSeuil(seuil);
    
            log.info("creating formule");
            Formule formule = new Formule();
            JsonNode formuleNode = jsonNode.get("formule");
            formule.setId(formuleNode.get("id").asText());
            formule.setDescription(formuleNode.get("description").asText());
            seuil.setFormule(formule);
    
            log.info("creating mesure");
            Mesure mesure = new Mesure();
            JsonNode mesureNode = jsonNode.get("mesure");
            mesure.setIdMesure(UUID.fromString(mesureNode.get("id").asText()));
            mesure.setDescription(mesureNode.get("description").asText());
            mesure.setCode(mesureNode.get("code").asText());
            formule.setMesures(Collections.singleton(mesure));
    
            log.info("creating gieglan file");
            GieglanFile gieglanFile = new GieglanFile();
            JsonNode gieglanFileNode = jsonNode.get("gieglan_files");
            gieglanFile.setId(UUID.fromString(gieglanFileNode.get("id").asText()));
            gieglanFile.setName(gieglanFileNode.get("name").asText());
            gieglanFile.setStatus(com.catis.model.control.GieglanFile.StatusType.valueOf(gieglanFileNode.get("status").asText()));
            gieglanFile.setType(com.catis.model.control.GieglanFile.FileType.valueOf(gieglanFileNode.get("type").asText()));
            rapportDeVisite.setGieglanFile(gieglanFile);
    
            visite.setRapportDeVisites(Collections.singletonList(rapportDeVisite));
            
            return visite;
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON to Visite: ", e);
            return null;
        }
    }
    
    


        public String getHTML(Visite visite) {
        String icons = "";
        switch (visite.statutRender()) {
            case "maj":
                return "<span class=\"badge badge-primary\">" + visite.statutRender() + "</span>";

            case "A inspecter":
                return  "<span class=\"badge badge-warning\">" + visite.statutRender() + "</span>";

            case "En cours test":
                for (GieglanFileIcon cat : replaceIconIfNecessary(visite)) {
                    icons += cat.getIcon();
                }
                return icons;


            case "A signer":
                if(visite.getProcess().isStatus())
                    return  "<span class=\"badge badge-info\"> ACCEPTE " + visite.statutRender() + "</span>";
                else
                    return  "<span class=\"badge badge-danger\"> REFUSE " + visite.statutRender() + "</span>";

            case "A imprimer":
                return "<span class=\"badge badge-success\">" + visite.statutRender() + "</span>";

            case "A certifier":
                return "<span class=\"badge badge-primary\">" + visite.statutRender() + "</span>";
            case "Accepté":
                return "<span class=\"badge badge-success\">" + visite.statutRender() + "</span>";
            case "Refusé":
                return "<span class=\"badge badge-dark\">" + visite.statutRender() + "</span>";
            default:
                return "<span class=\"badge badge-warning\">" + visite.statutRender() + "</span>";
        }
    }

    @Transactional
    public List<GieglanFileIcon> replaceIconIfNecessary(Visite visite){
        System.out.println("build visite +++++++++++++++"+ visite.getIdVisite());
        ProduitCategorieTest p = Utils.tests.stream()
                .filter(produitCategorieTest -> produitCategorieTest.getProduitId().equals(visite.getCarteGrise().getProduit().getProduitId()))
                .findFirst()
                .get();
        Set<GieglanFileIcon> icons = new HashSet<>();
        List<GieglanFileIcon> gieglanFileIcons  =p.getTest().stream().map(
                testNew -> new GieglanFileIcon(testNew.getExtension(), testNew.getIcon())
        ).collect(Collectors.toList());
        if(visite.isContreVisite()){
            gieglanFileService.getGieglanFileFailed(visite).forEach(g -> {
                switch (g.getCategorieTest().getLibelle()){
                    case "F" :
                        if(visite.getFreinage().equals(Visite.TestResult.PENDING))
                            icons.add(new GieglanFileIcon("F", "<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp"));
                        else if(visite.getFreinage().equals(Visite.TestResult.SUCCESS))
                            icons.add(new GieglanFileIcon("F", "<span class=\"badge badge-success\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp"));
                        else
                            icons.add(new GieglanFileIcon("F", "<span class=\"badge badge-danger\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp"));
                        break;
                    case "R" :
                        if(visite.getRipage().equals(Visite.TestResult.PENDING))
                            icons.add(new GieglanFileIcon("R", "<span class=\"badge badge-light\"><i class=\"i-Car-2\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Ripage\"></i></span>&nbsp"));
                        else if(visite.getRipage().equals(Visite.TestResult.SUCCESS))
                            icons.add(new GieglanFileIcon("R", "<span class=\"badge badge-success\"><i class=\"i-Car-2\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Ripage\"></i></span>&nbsp"));
                        else
                            icons.add(new GieglanFileIcon("R", "<span class=\"badge badge-danger\"><i class=\"i-Car-2\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Ripage\"></i></span>&nbsp"));
                        break;
                    case "S" :
                        if(visite.getSuspension().equals(Visite.TestResult.PENDING))
                            icons.add(new GieglanFileIcon("S", "<span class=\"badge badge-light\"><i class=\"i-Jeep-2\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"Suspension\"></i></span>&nbsp"));
                        else if(visite.getSuspension().equals(Visite.TestResult.SUCCESS))
                            icons.add(new GieglanFileIcon("S", "<span class=\"badge badge-success\"><i class=\"i-Jeep-2\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"Suspension\"></i></span>&nbsp"));
                        else
                            icons.add(new GieglanFileIcon("S", "<span class=\"badge badge-danger\"><i class=\"i-Jeep-2\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"Suspension\"></i></span>&nbsp"));
                        break;
                    case "P" :
                        if(visite.getReglophare().equals(Visite.TestResult.PENDING))
                            icons.add(new GieglanFileIcon("P", "<span class=\"badge badge-light\"><i class=\"i-Flash\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Réglophare\"></i></span>&nbsp"));
                        else if(visite.getReglophare().equals(Visite.TestResult.SUCCESS))
                            icons.add(new GieglanFileIcon("P", "<span class=\"badge badge-success\"><i class=\"i-Flash\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Réglophare\"></i></span>&nbsp"));
                        else
                            icons.add(new GieglanFileIcon("P", "<span class=\"badge badge-danger\"><i class=\"i-Flash\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Réglophare\"></i></span>&nbsp"));
                        break;

                    case "JSON":

                        if(visite.getVisuel().equals(Visite.TestResult.PENDING))
                            icons.add(new GieglanFileIcon("JSON", "<span class=\"badge badge-light\"><i class=\"i-Eye\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures visuelles\"></i></span>&nbsp"));
                        else if(visite.getVisuel().equals(Visite.TestResult.SUCCESS))
                            icons.add(new GieglanFileIcon("JSON", "<span class=\"badge badge-success\"><i class=\"i-Eye\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures visuelles\"></i></span>&nbsp"));
                        else
                            icons.add(new GieglanFileIcon("JSON", "<span class=\"badge badge-danger\"><i class=\"i-Eye\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures visuelles\"></i></span>&nbsp"));
                        break;
                    case "G":
                        if(visite.getPollution().equals(Visite.TestResult.PENDING))
                            icons.add(new GieglanFileIcon("G", "<span class=\"badge badge-light\"><i class=\"i-Cloud1\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Analyseur de gaz\"></i></span>&nbsp"));
                        else if(visite.getPollution().equals(Visite.TestResult.SUCCESS))
                            icons.add(new GieglanFileIcon("G", "<span class=\"badge badge-success\"><i class=\"i-Cloud1\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Analyseur de gaz\"></i></span>&nbsp"));
                        else
                            icons.add(new GieglanFileIcon("G", "<span class=\"badge badge-danger\"><i class=\"i-Cloud1\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Analyseur de gaz\"></i></span>&nbsp"));

                }
            });

        }

        else {
            gieglanFileIcons.forEach(gieglanFileIcon -> {
                switch (gieglanFileIcon.getExtension()){
                    case "F" :
                        if(visite.getFreinage().equals(Visite.TestResult.PENDING))
                            icons.add(new GieglanFileIcon("F", "<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp"));
                        else if(visite.getFreinage().equals(Visite.TestResult.SUCCESS))
                            icons.add(new GieglanFileIcon("F", "<span class=\"badge badge-success\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp"));
                        else
                            icons.add(new GieglanFileIcon("F", "<span class=\"badge badge-danger\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp"));
                        break;
                    case "R" :
                        if(visite.getRipage().equals(Visite.TestResult.PENDING))
                            icons.add(new GieglanFileIcon("R", "<span class=\"badge badge-light\"><i class=\"i-Car-2\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Ripage\"></i></span>&nbsp"));
                        else if(visite.getRipage().equals(Visite.TestResult.SUCCESS))
                            icons.add(new GieglanFileIcon("R", "<span class=\"badge badge-success\"><i class=\"i-Car-2\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Ripage\"></i></span>&nbsp"));
                        else
                            icons.add(new GieglanFileIcon("R", "<span class=\"badge badge-danger\"><i class=\"i-Car-2\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Ripage\"></i></span>&nbsp"));
                        break;
                    case "S" :
                        if(visite.getSuspension().equals(Visite.TestResult.PENDING))
                            icons.add(new GieglanFileIcon("S", "<span class=\"badge badge-light\"><i class=\"i-Jeep-2\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"Suspension\"></i></span>&nbsp"));
                        else if(visite.getSuspension().equals(Visite.TestResult.SUCCESS))
                            icons.add(new GieglanFileIcon("S", "<span class=\"badge badge-success\"><i class=\"i-Jeep-2\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"Suspension\"></i></span>&nbsp"));
                        else
                            icons.add(new GieglanFileIcon("S", "<span class=\"badge badge-danger\"><i class=\"i-Jeep-2\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"Suspension\"></i></span>&nbsp"));
                        break;
                    case "P" :
                        if(visite.getReglophare().equals(Visite.TestResult.PENDING))
                            icons.add(new GieglanFileIcon("P", "<span class=\"badge badge-light\"><i class=\"i-Flash\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Réglophare\"></i></span>&nbsp"));
                        else if(visite.getReglophare().equals(Visite.TestResult.SUCCESS))
                            icons.add(new GieglanFileIcon("P", "<span class=\"badge badge-success\"><i class=\"i-Flash\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Réglophare\"></i></span>&nbsp"));
                        else
                            icons.add(new GieglanFileIcon("P", "<span class=\"badge badge-danger\"><i class=\"i-Flash\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Réglophare\"></i></span>&nbsp"));
                        break;

                    case "JSON":

                        if(visite.getVisuel().equals(Visite.TestResult.PENDING))
                            icons.add(new GieglanFileIcon("JSON", "<span class=\"badge badge-light\"><i class=\"i-Eye\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures visuelles\"></i></span>&nbsp"));
                        else if(visite.getVisuel().equals(Visite.TestResult.SUCCESS))
                            icons.add(new GieglanFileIcon("JSON", "<span class=\"badge badge-success\"><i class=\"i-Eye\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures visuelles\"></i></span>&nbsp"));
                        else
                            icons.add(new GieglanFileIcon("JSON", "<span class=\"badge badge-danger\"><i class=\"i-Eye\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures visuelles\"></i></span>&nbsp"));
                        break;
                    case "G":
                        if(visite.getPollution().equals(Visite.TestResult.PENDING))
                            icons.add(new GieglanFileIcon("G", "<span class=\"badge badge-light\"><i class=\"i-Cloud1\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Analyseur de gaz\"></i></span>&nbsp"));
                        else if(visite.getPollution().equals(Visite.TestResult.SUCCESS))
                            icons.add(new GieglanFileIcon("G", "<span class=\"badge badge-success\"><i class=\"i-Cloud1\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Analyseur de gaz\"></i></span>&nbsp"));
                        else
                            icons.add(new GieglanFileIcon("G", "<span class=\"badge badge-danger\"><i class=\"i-Cloud1\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Analyseur de gaz\"></i></span>&nbsp"));
                        break;
                }
            });


        }
        return new ArrayList<>(icons);
    }

}
