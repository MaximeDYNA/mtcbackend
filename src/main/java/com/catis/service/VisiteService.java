package com.catis.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.catis.controller.SseController;
import com.catis.model.control.Control;
import com.catis.model.entity.*;
import com.catis.model.entity.Visite.TestResult;
import com.catis.objectTemporaire.*;

import com.catis.repository.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.catis.controller.exception.VisiteEnCoursException;
import com.catis.model.control.Control.StatusType;
import com.catis.model.control.GieglanFile;
import com.catis.repository.VisiteRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import static com.catis.controller.SseController.emitters;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Service
@CacheConfig(cacheNames={"VisiteCache"})
public class VisiteService {


    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private VisiteRepository visiteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrganisationService os;
    @Autowired
    private CategorieTestVehiculeService cat;
    @Autowired
    private GieglanFileService gieglanFileService;
    @Autowired
    private NotificationService notificationService;



    private static Logger log = LoggerFactory.getLogger(VisiteService.class);
    
    // flemming implimented
    public List<Visite> getLastVisiteWithTestIsOkDirectQuery(UUID controlId, UUID visiteId) {
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
    
    

    public List<Visite> findActiveVI(){
        return visiteRepository.findByActiveStatusTrueAndContreVisiteFalse();
    }
    public List<Visite> findActiveCV(){
        return visiteRepository.findByActiveStatusTrueAndContreVisiteTrue();
    }
    public List<Visite> findActiveVisites(){
        return visiteRepository.findByActiveStatusTrue();
    }
    public List<Visite> findbyProduit(Produit produit){
        return visiteRepository.findByActiveStatusTrueAndCarteGriseProduit(produit);
    }
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Visite visiteWithLastMissedTests(Visite visite){
        List <Visite> v = visiteRepository
            .getBeforeLastVisite(visite.getControl(), visite, PageRequest.of(0,1));
        if(!v.isEmpty()) {
            return v.get(0);
        }
        return null;
    }


    @Transactional
    @CacheEvict(allEntries = true)
    public Visite add(Visite visite) {
        return visiteRepository.save(visite);
    }

    public List<Visite> findAll() {
        List<Visite> visites = new ArrayList<Visite>();
        visiteRepository.findAll().forEach(visites::add);
        return visites;
    }

    public List<Visite> findByReference(String ref, UUID organisationId) {
        return visiteRepository.findByCarteGriseNumImmatriculationIgnoreCaseOrCarteGrise_Vehicule_ChassisIgnoreCaseAndOrganisation_OrganisationId(ref, ref, organisationId);
    }
       public Visite findById(UUID i) {
        return visiteRepository.findById(i).get();
    }
    @CacheEvict(allEntries = true)
    public Visite enregistrer(Visite i) {
        return visiteRepository.save(i);
    }
    public boolean viensPourContreVisite(String imCha) {
        try {
            return !visiteRepository.findByContreVisiteFalseAndCarteGriseNumImmatriculationIgnoreCaseOrCarteGrise_Vehicule_ChassisIgnoreCase(imCha, imCha)
                    .stream()
                    .filter(visites -> Duration.between(visites.getDateFin(), LocalDateTime.now()).toDays() <= 15)
                    .collect(Collectors.toList()).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    @CacheEvict(allEntries = true)
    @Transactional
    public Visite ajouterVisite(CarteGrise cg, double montantTotal, double montantEncaisse, UUID organisationId, Caissier caissier, String document, String certidocsId) throws VisiteEnCoursException {
        Visite visite = new Visite();

        Organisation organisation = os.findByOrganisationId(organisationId);
        if (montantEncaisse < montantTotal) {
            visite.setStatut(9);
        } else
            visite.setStatut(0);

        if (isVisiteInitial(cg.getNumImmatriculation(), organisationId)) {
            visite.setContreVisite(false);
            visite.setEncours(true);
            visite.setCarteGrise(cg);
            visite.setDateDebut(LocalDateTime.now());
            Control control = new Control();
            List<Visite> visites = new ArrayList<>();
            visites.add(visite);
            control.setCarteGrise(cg);
            control.setStatus(StatusType.INITIALIZED);
            control.setVisites(visites);
            control.setOrganisation(organisation);
            visite.setControl(control);
            log.info("processed visite initial");
            
        } else {
            log.info("processing contre visite");
            visite.setContreVisite(true);
            visite.setStatut(1);
            List<Visite> vi =visiteRepository.getBeforeLastVisiteWithHisControl(cg.getNumImmatriculation(), PageRequest.of(0,1));
            if(!vi.isEmpty()) {
                visite.setControl(vi.get(0).getControl());
            }

            visite.setEncours(true);
            visite.setCarteGrise(cg);
            visite.setDateDebut(LocalDateTime.now());
        }

        if (cg.getProduit().getLibelle()== "dec") {
            return visite;
        }
        visite.setCaissier(caissier);
        visite.setOrganisation(organisation);
        visite.setReglophare(Visite.TestResult.PENDING);
        visite.setRipage(Visite.TestResult.PENDING);
        visite.setFreinage(Visite.TestResult.PENDING);
        visite.setSuspension(Visite.TestResult.PENDING);
        visite.setPollution(Visite.TestResult.PENDING);
        visite.setVisuel(Visite.TestResult.PENDING);
        visite.setDocument(document);
        visite.setCertidocsId(certidocsId);
        log.info("SAVING VISITE");
        visite = visiteRepository.save(visite);
        final Visite v = visite;
        dispatchNewVisit(visite);
        organisation.getUtilisateurs().forEach(utilisateur -> {
            notificationService.dipatchVisiteToMember(utilisateur.getKeycloakId(), v, false);
        });
        log.info("RETURNING VISITE");
        return visite;
    }
    
    
    
    @CacheEvict(allEntries = true)
    @Transactional
    public Visite modifierVisite(Visite visite) throws IOException {
        Visite v = visiteRepository.save(visite);
        v.getOrganisation().getUtilisateurs().forEach(utilisateur -> {
            notificationService.dipatchVisiteToMember(utilisateur.getKeycloakId(), v, true);
        });
        return v;
    }

 
    public boolean visiteEncours(String imCha, UUID organisationId) {
        return !visiteRepository.findByActiveStatusTrueAndCarteGriseNumImmatriculationIgnoreCaseOrCarteGrise_Vehicule_ChassisIgnoreCaseAndOrganisation_OrganisationId(imCha, imCha, organisationId)
                .stream().filter(visites -> visites.getDateFin() == null).collect(Collectors.toList())
                .isEmpty();
    }
    public List<Visite> enCoursVisitList(UUID orgId) {
        List<Visite> visiteEnCours = visiteRepository.findByOrganisation_OrganisationIdAndEncoursTrueAndActiveStatusTrueOrderByCreatedDateDesc(orgId);

        return visiteEnCours;
    }
    public List<Visite> getOrganisationVisiteWithTest(UUID orgId, Pageable pageable) {
        List<Visite> visiteEnCours = visiteRepository.getOrganisationVisiteWithTest(orgId, pageable);

        return visiteEnCours;
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public List<Visite> enCoursVisitList(UUID orgId, Pageable pageable) {
        List<Visite> visiteEnCours = visiteRepository.findByOrganisation_OrganisationIdAndEncoursTrueAndActiveStatusTrue(orgId, pageable);

        return visiteEnCours;
    }

    @Transactional
    @Cacheable
    public Page<NewListView> NewsearchedVisitList(String search, UUID orgId, Pageable pageable) {
        if (search == null || search.isEmpty()) {
            search = null;
        }

        List<Visite> resultPage = visiteRepository.findByRef(search, orgId, pageable);
        List<NewListView> newListViews = resultPage.stream().map(visite ->
                new NewListView(
                        visite.getIdVisite(),
                        visite.getCarteGrise().getProduit(),
                        visite.typeRender(),
                        visite.getCarteGrise().getNumImmatriculation(),
                        (visite.getCarteGrise().getVehicule() == null ? "" :
                                (visite.getCarteGrise().getVehicule().getChassis() == null ? "" :
                                        visite.getCarteGrise().getVehicule().getChassis())),
                        (visite.getCarteGrise().getProprietaireVehicule().getPartenaire().getNom() == null ? null :
                                visite.getCarteGrise().getProprietaireVehicule().getPartenaire().getNom()),
                        Utils.parseDate(visite.getCreatedDate()),
                        visite.getCreatedDate(),
                        getHTML(visite),
                        visite.getStatut(),
                        visite.getIdVisite(),
                        visite.isContreVisite(),
                        (visite.getInspection() == null ? null : visite.getInspection().getIdInspection()),
                        visite.getCarteGrise(),
                        visite.getOrganisation().isConformity(),
                        visite.getIsConform(),
                        visite.getOrganisation().getNom(),
                        (visite.getInspection() == null ? "" : visite.getInspection().getBestPlate()),
                        (visite.getInspection() == null ? 0 : visite.getInspection().getDistancePercentage()),
                        visite.getCreatedDate().format(SseController.dateTimeFormatter),
                        false,
                        visite.getDocument()
                )
        ).collect(Collectors.toList());

        return new PageImpl<>(newListViews, pageable, 300);
    }

    // old method
    @Cacheable
    public Page<Visite> endedVisitList(UUID orgId, Pageable pageable){
        Page<Visite> visiteEnCours = visiteRepository.findByOrganisation_OrganisationIdAndEncoursFalseAndActiveStatusTrueOrderByCreatedDateDesc(orgId, pageable);
        return visiteEnCours;
    }
    // i implimneted
    @Transactional
    @Cacheable
    public Page<NewListView> NewendedVisitList(UUID orgId, Pageable pageable) {
        Page<Visite> resultPage = visiteRepository.findByOrganisation_OrganisationIdAndEncoursFalseAndActiveStatusTrueOrderByCreatedDateDesc(orgId, pageable);

        List<NewListView> newListViews = resultPage.stream().map(visite -> 
            new NewListView(
                visite.getIdVisite(),
                visite.getCarteGrise().getProduit(),
                visite.typeRender(),
                visite.getCarteGrise().getNumImmatriculation(),
                (visite.getCarteGrise().getVehicule() == null ? "" : 
                 (visite.getCarteGrise().getVehicule().getChassis() == null ? "" : 
                  visite.getCarteGrise().getVehicule().getChassis())),
                (visite.getCarteGrise().getProprietaireVehicule().getPartenaire().getNom() == null ? null : 
                 visite.getCarteGrise().getProprietaireVehicule().getPartenaire().getNom()),
                Utils.parseDate(visite.getCreatedDate()),
                visite.getCreatedDate(),
                getHTML(visite),
                visite.getStatut(),
                visite.getIdVisite(),
                visite.isContreVisite(),
                (visite.getInspection() == null ? null : visite.getInspection().getIdInspection()),
                visite.getCarteGrise(),
                visite.getOrganisation().isConformity(),
                visite.getIsConform(),
                visite.getOrganisation().getNom(),
                (visite.getInspection() == null ? null : visite.getInspection().getBestPlate()),
                (visite.getInspection() == null ? 0 : visite.getInspection().getDistancePercentage()),
                visite.getCreatedDate().format(SseController.dateTimeFormatter),
                false,
                visite.getDocument()
            )
        ).collect(Collectors.toList());

        return new PageImpl<>(newListViews, pageable, resultPage.getTotalElements());
    }


    // flemming implimented

    // @Cacheable
    public Page<NewListView> MainlistParStatusForGraphView(int status, UUID orgId) {
        
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
                "AND    v.active_status=1 order by v.created_date desc ", orgId, status);

        Query query = entityManager.createNativeQuery(queryStr);

        List<Object> rawResults = query.getResultList();
        
        List<NewListView> newListViews = new ArrayList<>();

        for (Object rawResult : rawResults) {
            try {
                String jsonString = (String) rawResult;
                JsonNode jsonNode = objectMapper.readTree(jsonString);

                log.info("creating partner started");
                Partenaire partenaire = new Partenaire();
                partenaire.setPartenaireId(UUID.fromString(jsonNode.get("carteGrise").get("proprietaireVehicule").get("partenaire").get("partenaireId").asText()));
                partenaire.setNom(jsonNode.get("carteGrise").get("proprietaireVehicule").get("partenaire").get("nom").asText());
                partenaire.setPrenom(jsonNode.get("carteGrise").get("proprietaireVehicule").get("partenaire").get("prenom").asText());
                
                log.info("creating partner finished, propertaire vehicle started");
                ProprietaireVehicule proprietaireVehicule = new ProprietaireVehicule();
                proprietaireVehicule.setProprietaireVehiculeId(UUID.fromString(jsonNode.get("carteGrise").get("proprietaireVehicule").get("proprietaireVehiculeId").asText()));
                proprietaireVehicule.setDescription(jsonNode.get("carteGrise").get("proprietaireVehicule").get("description").asText());
                proprietaireVehicule.setScore(jsonNode.get("carteGrise").get("proprietaireVehicule").get("score").asInt());
                proprietaireVehicule.setPartenaire(partenaire);

                log.info("creating propertaire vehicule finished, cartegrise started");

                // Construct CarteGrise instance
                CarteGrise carteGrise = new CarteGrise();
                carteGrise.setCarteGriseId(UUID.fromString(jsonNode.get("carteGrise").get("carteGriseId").asText()));
                carteGrise.setNumImmatriculation(jsonNode.get("carteGrise").get("numImmatriculation").asText());
                carteGrise.setPreImmatriculation(jsonNode.get("carteGrise").get("preImmatriculation").asText());
                carteGrise.setDateDebutValid(DateParser(jsonNode.get("carteGrise").get("dateDebutValid").asText()));
                carteGrise.setDateFinValid(DateParser(jsonNode.get("carteGrise").get("dateFinValid").asText()));
                
                carteGrise.setSsdt_id(jsonNode.get("carteGrise").get("ssdt_id").asText());
                carteGrise.setCommune(jsonNode.get("carteGrise").get("commune").asText());
                carteGrise.setMontantPaye(Double.parseDouble(jsonNode.get("carteGrise").get("montantPaye").asText()));
                carteGrise.setVehiculeGage(parseBooleanValue(jsonNode.get("carteGrise").get("vehiculeGage").asText()));
                carteGrise.setGenreVehicule(jsonNode.get("carteGrise").get("genreVehicule").asText());
                carteGrise.setEnregistrement(jsonNode.get("carteGrise").get("enregistrement").asText());
                carteGrise.setType(jsonNode.get("carteGrise").get("type").asText());
                carteGrise.setDateDelivrance(DateParser(jsonNode.get("carteGrise").get("dateDelivrance").asText()));
                carteGrise.setLieuDedelivrance(jsonNode.get("carteGrise").get("lieuDedelivrance").asText());
                carteGrise.setCentre_ssdt(jsonNode.get("carteGrise").get("centre_ssdt").asText());
                carteGrise.setType(jsonNode.get("carteGrise").get("type").asText());
                carteGrise.setProprietaireVehicule(proprietaireVehicule);

                log.info("created carteGrise successfully, product started");

                Produit produit = new Produit();
                produit.setProduitId(UUID.fromString(jsonNode.get("carteGrise").get("produit").get("produitId").asText()));
                produit.setLibelle(jsonNode.get("carteGrise").get("produit").get("libelle").asText());
                produit.setImg(jsonNode.get("carteGrise").get("produit").get("img").asText());
                produit.setDescription(jsonNode.get("carteGrise").get("produit").get("description").asText());
                produit.setPrix(jsonNode.get("carteGrise").get("produit").get("prix").asInt());
                produit.setDelaiValidite(jsonNode.get("carteGrise").get("produit").get("delaiValidite").asInt());
                
                log.info("created carteGrise product");
                CategorieProduit categoryproduit = new CategorieProduit();
                categoryproduit.setCategorieProduitId(UUID.fromString(jsonNode.get("carteGrise").get("produit").get("categorieProduit").get("categorieProduitId").asText()));
                categoryproduit.setLibelle(jsonNode.get("carteGrise").get("produit").get("categorieProduit").get("libelle").asText());
                categoryproduit.setDescription(jsonNode.get("carteGrise").get("produit").get("categorieProduit").get("description").asText());
                
                produit.setCategorieProduit(categoryproduit);
                log.info("created categoryproduit");
                
                CategorieVehicule  categorieVehicule = new CategorieVehicule();
                categorieVehicule.setId(UUID.fromString(jsonNode.get("carteGrise").get("produit").get("categorieVehicule").get("id").asText()));
                categorieVehicule.setType(jsonNode.get("carteGrise").get("produit").get("categorieVehicule").get("type").asText());
                
                produit.setCategorieVehicule(categorieVehicule);
                carteGrise.setProduit(produit);
                log.info("created categorie vehicle");
                
                
                
                Vehicule vehicule = new Vehicule();
                vehicule.setVehiculeId(UUID.fromString(jsonNode.get("carteGrise").get("vehicule").get("vehiculeId").asText()));
                vehicule.setScore(jsonNode.get("carteGrise").get("vehicule").get("score").asInt());
                vehicule.setChassis(jsonNode.get("carteGrise").get("vehicule").get("chassis").asText());
                vehicule.setCylindre(jsonNode.get("carteGrise").get("vehicule").get("cylindre").asInt());
                vehicule.setPoidsVide(jsonNode.get("carteGrise").get("vehicule").get("poidsVide").asInt());
                vehicule.setPuissAdmin(jsonNode.get("carteGrise").get("vehicule").get("puissAdmin").asInt());
                vehicule.setCarrosserie(jsonNode.get("carteGrise").get("vehicule").get("carrosserie").asText());
                vehicule.setChargeUtile(jsonNode.get("carteGrise").get("vehicule").get("chargeUtile").asInt());
                vehicule.setPlaceAssise(jsonNode.get("carteGrise").get("vehicule").get("placeAssise").asInt());
                vehicule.setTypeVehicule(jsonNode.get("carteGrise").get("vehicule").get("typeVehicule").asText());
                vehicule.setPoidsTotalCha(jsonNode.get("carteGrise").get("vehicule").get("poidsTotalCha").asInt());
                vehicule.setPremiereMiseEnCirculation(DateParser(jsonNode.get("carteGrise").get("vehicule").get("premiereMiseEnCirculation").asText()));
            
               
                Energie energie = new Energie();
                energie.setEnergieId(UUID.fromString(jsonNode.get("carteGrise").get("vehicule").get("energie").get("energieId").asText()));
                energie.setLibelle(jsonNode.get("carteGrise").get("vehicule").get("energie").get("libelle").asText());
                
                vehicule.setEnergie(energie);
                
                MarqueVehicule marqueVehicule = new MarqueVehicule(); 
                marqueVehicule.setMarqueVehiculeId(UUID.fromString(jsonNode.get("carteGrise").get("vehicule").get("marqueVehicule").get("marqueVehiculeId").asText()));
                marqueVehicule.setLibelle(jsonNode.get("carteGrise").get("vehicule").get("marqueVehicule").get("libelle").asText());
                marqueVehicule.setDescription(jsonNode.get("carteGrise").get("vehicule").get("marqueVehicule").get("description").asText());

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
                
                
                    visite.setCarteGrise(carteGrise);
                

                log.info("visite created");


                log.info("creating new list view");

                NewListView newListView = new NewListView();
                // newListView.setCategorie(visite.getCarteGrise().getProduit());
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

        return new PageImpl<>(newListViews);
    }
   
    @Cacheable
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

                log.info("creating partner started");
                Partenaire partenaire = new Partenaire();
                partenaire.setPartenaireId(UUID.fromString(jsonNode.get("carteGrise").get("proprietaireVehicule").get("partenaire").get("partenaireId").asText()));
                partenaire.setNom(jsonNode.get("carteGrise").get("proprietaireVehicule").get("partenaire").get("nom").asText());
                partenaire.setPrenom(jsonNode.get("carteGrise").get("proprietaireVehicule").get("partenaire").get("prenom").asText());
                
                log.info("creating partner finished, propertaire vehicle started");
                ProprietaireVehicule proprietaireVehicule = new ProprietaireVehicule();
                proprietaireVehicule.setProprietaireVehiculeId(UUID.fromString(jsonNode.get("carteGrise").get("proprietaireVehicule").get("proprietaireVehiculeId").asText()));
                proprietaireVehicule.setDescription(jsonNode.get("carteGrise").get("proprietaireVehicule").get("description").asText());
                proprietaireVehicule.setScore(jsonNode.get("carteGrise").get("proprietaireVehicule").get("score").asInt());
                proprietaireVehicule.setPartenaire(partenaire);

                log.info("creating propertaire vehicule finished, cartegrise started");

                // Construct CarteGrise instance
                CarteGrise carteGrise = new CarteGrise();
                carteGrise.setCarteGriseId(UUID.fromString(jsonNode.get("carteGrise").get("carteGriseId").asText()));
                carteGrise.setNumImmatriculation(jsonNode.get("carteGrise").get("numImmatriculation").asText());
                carteGrise.setPreImmatriculation(jsonNode.get("carteGrise").get("preImmatriculation").asText());
                carteGrise.setDateDebutValid(DateParser(jsonNode.get("carteGrise").get("dateDebutValid").asText()));
                carteGrise.setDateFinValid(DateParser(jsonNode.get("carteGrise").get("dateFinValid").asText()));
                
                carteGrise.setSsdt_id(jsonNode.get("carteGrise").get("ssdt_id").asText());
                carteGrise.setCommune(jsonNode.get("carteGrise").get("commune").asText());
                carteGrise.setMontantPaye(Double.parseDouble(jsonNode.get("carteGrise").get("montantPaye").asText()));
                carteGrise.setVehiculeGage(parseBooleanValue(jsonNode.get("carteGrise").get("vehiculeGage").asText()));
                carteGrise.setGenreVehicule(jsonNode.get("carteGrise").get("genreVehicule").asText());
                carteGrise.setEnregistrement(jsonNode.get("carteGrise").get("enregistrement").asText());
                carteGrise.setType(jsonNode.get("carteGrise").get("type").asText());
                carteGrise.setDateDelivrance(DateParser(jsonNode.get("carteGrise").get("dateDelivrance").asText()));
                carteGrise.setLieuDedelivrance(jsonNode.get("carteGrise").get("lieuDedelivrance").asText());
                carteGrise.setCentre_ssdt(jsonNode.get("carteGrise").get("centre_ssdt").asText());
                carteGrise.setType(jsonNode.get("carteGrise").get("type").asText());
                carteGrise.setProprietaireVehicule(proprietaireVehicule);

                log.info("created carteGrise successfully, product started");

                Produit produit = new Produit();
                produit.setProduitId(UUID.fromString(jsonNode.get("carteGrise").get("produit").get("produitId").asText()));
                produit.setLibelle(jsonNode.get("carteGrise").get("produit").get("libelle").asText());
                produit.setImg(jsonNode.get("carteGrise").get("produit").get("img").asText());
                produit.setDescription(jsonNode.get("carteGrise").get("produit").get("description").asText());
                produit.setPrix(jsonNode.get("carteGrise").get("produit").get("prix").asInt());
                produit.setDelaiValidite(jsonNode.get("carteGrise").get("produit").get("delaiValidite").asInt());
                
                log.info("created carteGrise product");
                CategorieProduit categoryproduit = new CategorieProduit();
                categoryproduit.setCategorieProduitId(UUID.fromString(jsonNode.get("carteGrise").get("produit").get("categorieProduit").get("categorieProduitId").asText()));
                categoryproduit.setLibelle(jsonNode.get("carteGrise").get("produit").get("categorieProduit").get("libelle").asText());
                categoryproduit.setDescription(jsonNode.get("carteGrise").get("produit").get("categorieProduit").get("description").asText());
                
                produit.setCategorieProduit(categoryproduit);
                log.info("created categoryproduit");
                
                CategorieVehicule  categorieVehicule = new CategorieVehicule();
                categorieVehicule.setId(UUID.fromString(jsonNode.get("carteGrise").get("produit").get("categorieVehicule").get("id").asText()));
                categorieVehicule.setType(jsonNode.get("carteGrise").get("produit").get("categorieVehicule").get("type").asText());
                
                produit.setCategorieVehicule(categorieVehicule);
                carteGrise.setProduit(produit);
                log.info("created categorie vehicle");
                
                
                
                Vehicule vehicule = new Vehicule();
                vehicule.setVehiculeId(UUID.fromString(jsonNode.get("carteGrise").get("vehicule").get("vehiculeId").asText()));
                vehicule.setScore(jsonNode.get("carteGrise").get("vehicule").get("score").asInt());
                vehicule.setChassis(jsonNode.get("carteGrise").get("vehicule").get("chassis").asText());
                vehicule.setCylindre(jsonNode.get("carteGrise").get("vehicule").get("cylindre").asInt());
                vehicule.setPoidsVide(jsonNode.get("carteGrise").get("vehicule").get("poidsVide").asInt());
                vehicule.setPuissAdmin(jsonNode.get("carteGrise").get("vehicule").get("puissAdmin").asInt());
                vehicule.setCarrosserie(jsonNode.get("carteGrise").get("vehicule").get("carrosserie").asText());
                vehicule.setChargeUtile(jsonNode.get("carteGrise").get("vehicule").get("chargeUtile").asInt());
                vehicule.setPlaceAssise(jsonNode.get("carteGrise").get("vehicule").get("placeAssise").asInt());
                vehicule.setTypeVehicule(jsonNode.get("carteGrise").get("vehicule").get("typeVehicule").asText());
                vehicule.setPoidsTotalCha(jsonNode.get("carteGrise").get("vehicule").get("poidsTotalCha").asInt());
                vehicule.setPremiereMiseEnCirculation(DateParser(jsonNode.get("carteGrise").get("vehicule").get("premiereMiseEnCirculation").asText()));
            
               
                Energie energie = new Energie();
                energie.setEnergieId(
                    jsonNode.path("carteGrise").path("vehicule").path("energie").path("energieId").isNull() ? null : UUID.fromString(jsonNode.path("carteGrise").path("vehicule").path("energie").path("energieId").asText())
                );
                
                // Set libelle for energie
                energie.setLibelle(
                    jsonNode.path("carteGrise").path("vehicule").path("energie").path("libelle").isNull() ? null : jsonNode.path("carteGrise").path("vehicule").path("energie").path("libelle").asText()
                );
                vehicule.setEnergie(energie);
                
                MarqueVehicule marqueVehicule = new MarqueVehicule(); 
               // Set marqueVehiculeId for marqueVehicule
                    marqueVehicule.setMarqueVehiculeId(
                        jsonNode.path("carteGrise").path("vehicule").path("marqueVehicule").path("marqueVehiculeId").isNull() ? null : UUID.fromString(jsonNode.path("carteGrise").path("vehicule").path("marqueVehicule").path("marqueVehiculeId").asText())
                    );

                    // Set libelle for marqueVehicule
                    marqueVehicule.setLibelle(
                        jsonNode.path("carteGrise").path("vehicule").path("marqueVehicule").path("libelle").isNull() ? null : jsonNode.path("carteGrise").path("vehicule").path("marqueVehicule").path("libelle").asText()
                    );

                    // Set description for marqueVehicule
                    marqueVehicule.setDescription(
                        jsonNode.path("carteGrise").path("vehicule").path("marqueVehicule").path("description").isNull() ? null : jsonNode.path("carteGrise").path("vehicule").path("marqueVehicule").path("description").asText()
                    );
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
   

    @Cacheable
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

                log.info("creating partner started");
                Partenaire partenaire = new Partenaire();
                partenaire.setPartenaireId(UUID.fromString(jsonNode.get("carteGrise").get("proprietaireVehicule").get("partenaire").get("partenaireId").asText()));
                partenaire.setNom(jsonNode.get("carteGrise").get("proprietaireVehicule").get("partenaire").get("nom").asText());
                partenaire.setPrenom(jsonNode.get("carteGrise").get("proprietaireVehicule").get("partenaire").get("prenom").asText());
                
                log.info("creating partner finished, propertaire vehicle started");
                ProprietaireVehicule proprietaireVehicule = new ProprietaireVehicule();
                proprietaireVehicule.setProprietaireVehiculeId(UUID.fromString(jsonNode.get("carteGrise").get("proprietaireVehicule").get("proprietaireVehiculeId").asText()));
                proprietaireVehicule.setDescription(jsonNode.get("carteGrise").get("proprietaireVehicule").get("description").asText());
                proprietaireVehicule.setScore(jsonNode.get("carteGrise").get("proprietaireVehicule").get("score").asInt());
                proprietaireVehicule.setPartenaire(partenaire);

                log.info("creating propertaire vehicule finished, cartegrise started");

                // Construct CarteGrise instance
                CarteGrise carteGrise = new CarteGrise();
                carteGrise.setCarteGriseId(UUID.fromString(jsonNode.get("carteGrise").get("carteGriseId").asText()));
                carteGrise.setVehiculeGage(parseBooleanValue(jsonNode.get("carteGrise").get("vehiculeGage").asText()));
                carteGrise.setMontantPaye(Double.parseDouble(jsonNode.get("carteGrise").get("montantPaye").asText()));                
              
                carteGrise.setCentre_ssdt(
                    jsonNode.path("carteGrise").path("centre_ssdt").isNull() ? null : jsonNode.path("carteGrise").path("centre_ssdt").asText()
                );
                carteGrise.setNumImmatriculation(
                    jsonNode.path("carteGrise").path("numImmatriculation").isNull() ? null : jsonNode.path("carteGrise").path("numImmatriculation").asText()
                );
                carteGrise.setPreImmatriculation(
                    jsonNode.path("carteGrise").path("preImmatriculation").isNull() ? null : jsonNode.path("carteGrise").path("preImmatriculation").asText()
                );
                carteGrise.setDateDebutValid(
                    jsonNode.path("carteGrise").path("dateDebutValid").isNull() ? null : DateParser(jsonNode.path("carteGrise").path("dateDebutValid").asText())
                );
                carteGrise.setDateFinValid(
                    jsonNode.path("carteGrise").path("dateFinValid").isNull() ? null : DateParser(jsonNode.path("carteGrise").path("dateFinValid").asText())
                );
                carteGrise.setSsdt_id(
                    jsonNode.path("carteGrise").path("ssdt_id").isNull() ? null : jsonNode.path("carteGrise").path("ssdt_id").asText()
                );
                carteGrise.setCommune(
                    jsonNode.path("carteGrise").path("commune").isNull() ? null : jsonNode.path("carteGrise").path("commune").asText()
                );
                carteGrise.setGenreVehicule(
                    jsonNode.path("carteGrise").path("genreVehicule").isNull() ? null : jsonNode.path("carteGrise").path("genreVehicule").asText()
                );
                carteGrise.setEnregistrement(
                    jsonNode.path("carteGrise").path("enregistrement").isNull() ? null : jsonNode.path("carteGrise").path("enregistrement").asText()
                );
                carteGrise.setType(
                    jsonNode.path("carteGrise").path("type").isNull() ? null : jsonNode.path("carteGrise").path("type").asText()
                );
                carteGrise.setDateDelivrance(
                    jsonNode.path("carteGrise").path("dateDelivrance").isNull() ? null : DateParser(jsonNode.path("carteGrise").path("dateDelivrance").asText())
                );
                carteGrise.setLieuDedelivrance(
                    jsonNode.path("carteGrise").path("lieuDedelivrance").isNull() ? null : jsonNode.path("carteGrise").path("lieuDedelivrance").asText()
                );
                carteGrise.setProprietaireVehicule(proprietaireVehicule);

                log.info("created carteGrise successfully, product started");

                Produit produit = new Produit();
                produit.setProduitId(UUID.fromString(jsonNode.get("carteGrise").get("produit").get("produitId").asText()));
                produit.setLibelle(jsonNode.get("carteGrise").get("produit").get("libelle").asText());
                produit.setImg(jsonNode.get("carteGrise").get("produit").get("img").asText());
                produit.setDescription(jsonNode.get("carteGrise").get("produit").get("description").asText());
                produit.setPrix(jsonNode.get("carteGrise").get("produit").get("prix").asInt());
                produit.setDelaiValidite(jsonNode.get("carteGrise").get("produit").get("delaiValidite").asInt());
                
                log.info("created carteGrise product");
                CategorieProduit categoryproduit = new CategorieProduit();
                categoryproduit.setCategorieProduitId(UUID.fromString(jsonNode.get("carteGrise").get("produit").get("categorieProduit").get("categorieProduitId").asText()));
                categoryproduit.setLibelle(jsonNode.get("carteGrise").get("produit").get("categorieProduit").get("libelle").asText());
                categoryproduit.setDescription(jsonNode.get("carteGrise").get("produit").get("categorieProduit").get("description").asText());
                
                produit.setCategorieProduit(categoryproduit);
                log.info("created categoryproduit");
                
                CategorieVehicule  categorieVehicule = new CategorieVehicule();
                categorieVehicule.setId(UUID.fromString(jsonNode.get("carteGrise").get("produit").get("categorieVehicule").get("id").asText()));
                categorieVehicule.setType(jsonNode.get("carteGrise").get("produit").get("categorieVehicule").get("type").asText());
                
                produit.setCategorieVehicule(categorieVehicule);
                carteGrise.setProduit(produit);
                log.info("created categorie vehicle");
                
                
                if(jsonNode.get("carteGrise").has("vehicule") && !jsonNode.get("carteGrise").get("vehicule").isNull()){

                    Vehicule vehicule = new Vehicule();
                    vehicule.setVehiculeId(UUID.fromString(jsonNode.get("carteGrise").get("vehicule").get("vehiculeId").asText()));
                    vehicule.setScore(jsonNode.get("carteGrise").get("vehicule").get("score").asInt());
                    vehicule.setChassis(jsonNode.get("carteGrise").get("vehicule").get("chassis").asText());
                    vehicule.setCylindre(jsonNode.get("carteGrise").get("vehicule").get("cylindre").asInt());
                    vehicule.setPoidsVide(jsonNode.get("carteGrise").get("vehicule").get("poidsVide").asInt());
                    vehicule.setPuissAdmin(jsonNode.get("carteGrise").get("vehicule").get("puissAdmin").asInt());
                    vehicule.setCarrosserie(jsonNode.get("carteGrise").get("vehicule").get("carrosserie").asText());
                    vehicule.setChargeUtile(jsonNode.get("carteGrise").get("vehicule").get("chargeUtile").asInt());
                    vehicule.setPlaceAssise(jsonNode.get("carteGrise").get("vehicule").get("placeAssise").asInt());
                    vehicule.setTypeVehicule(jsonNode.get("carteGrise").get("vehicule").get("typeVehicule").asText());
                    vehicule.setPoidsTotalCha(jsonNode.get("carteGrise").get("vehicule").get("poidsTotalCha").asInt());
                    vehicule.setPremiereMiseEnCirculation(DateParser(jsonNode.get("carteGrise").get("vehicule").get("premiereMiseEnCirculation").asText()));
                    
                    Energie energie = new Energie();
                    energie.setEnergieId(
                        jsonNode.path("carteGrise").path("vehicule").path("energie").path("energieId").isNull() ? null : UUID.fromString(jsonNode.path("carteGrise").path("vehicule").path("energie").path("energieId").asText())
                    );
                    
                    // Set libelle for energie
                    energie.setLibelle(
                        jsonNode.path("carteGrise").path("vehicule").path("energie").path("libelle").isNull() ? null : jsonNode.path("carteGrise").path("vehicule").path("energie").path("libelle").asText()
                    );

                    vehicule.setEnergie(energie);
                    MarqueVehicule marqueVehicule = new MarqueVehicule(); 
                    marqueVehicule.setMarqueVehiculeId(UUID.fromString(jsonNode.get("carteGrise").get("vehicule").get("marqueVehicule").get("marqueVehiculeId").asText()));
                    marqueVehicule.setLibelle(jsonNode.get("carteGrise").get("vehicule").get("marqueVehicule").get("libelle").asText());
                    marqueVehicule.setDescription(jsonNode.get("carteGrise").get("vehicule").get("marqueVehicule").get("description").asText());
    
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
                    VerbalProcess verbalProcess = new VerbalProcess();
                    verbalProcess.setId(UUID.fromString(jsonNode.get("verbal_process_id").asText()));
                    verbalProcess.setStatus(jsonNode.get("verbalStatus").asBoolean());
    
                    visite.setProcess(verbalProcess);
                }

                if(jsonNode.has("Inspection") && !jsonNode.get("Inspection").isNull()){
                    Inspection inspection = new Inspection();
                    inspection.setIdInspection(UUID.fromString(jsonNode.get("Inspection").get("id").asText()));
                    inspection.setChassis(jsonNode.get("Inspection").get("chassis").asText());
                    inspection.setEssieux(jsonNode.get("Inspection").get("essieux").asInt());
                    inspection.setDateFin(DateParser(jsonNode.get("Inspection").get("date_fin").asText()));
    
    
                    inspection.setBestPlate(jsonNode.get("Inspection").get("bestPlate").asText());
    
    
                    inspection.setKilometrage(jsonNode.get("Inspection").get("kilometrage").asInt());
                    inspection.setActiveStatus(jsonNode.get("Inspection").get("activeStatus").asBoolean());
                    inspection.setDistancePercentage(jsonNode.get("Inspection").get("distance_percentage").asInt());
                    
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
    // flemming implimented
    
    @Cacheable
    @Transactional
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

                log.info("creating partner started");
                Partenaire partenaire = new Partenaire();
                partenaire.setPartenaireId(UUID.fromString(jsonNode.get("carteGrise").get("proprietaireVehicule").get("partenaire").get("partenaireId").asText()));
                partenaire.setNom(jsonNode.get("carteGrise").get("proprietaireVehicule").get("partenaire").get("nom").asText());
                partenaire.setPrenom(jsonNode.get("carteGrise").get("proprietaireVehicule").get("partenaire").get("prenom").asText());
                
                log.info("creating partner finished, propertaire vehicle started");
                ProprietaireVehicule proprietaireVehicule = new ProprietaireVehicule();
                proprietaireVehicule.setProprietaireVehiculeId(UUID.fromString(jsonNode.get("carteGrise").get("proprietaireVehicule").get("proprietaireVehiculeId").asText()));
                proprietaireVehicule.setDescription(jsonNode.get("carteGrise").get("proprietaireVehicule").get("description").asText());
                proprietaireVehicule.setScore(jsonNode.get("carteGrise").get("proprietaireVehicule").get("score").asInt());
                proprietaireVehicule.setPartenaire(partenaire);

                log.info("creating propertaire vehicule finished, cartegrise started");

                // Construct CarteGrise instance
                CarteGrise carteGrise = new CarteGrise();
                carteGrise.setCarteGriseId(UUID.fromString(jsonNode.get("carteGrise").get("carteGriseId").asText()));
                carteGrise.setMontantPaye(Double.parseDouble(jsonNode.get("carteGrise").get("montantPaye").asText()));
                carteGrise.setVehiculeGage(parseBooleanValue(jsonNode.get("carteGrise").get("vehiculeGage").asText()));
              
                carteGrise.setCentre_ssdt(
                    jsonNode.path("carteGrise").path("centre_ssdt").isNull() ? null : jsonNode.path("carteGrise").path("centre_ssdt").asText()
                );
                carteGrise.setNumImmatriculation(
                    jsonNode.path("carteGrise").path("numImmatriculation").isNull() ? null : jsonNode.path("carteGrise").path("numImmatriculation").asText()
                );
                carteGrise.setPreImmatriculation(
                    jsonNode.path("carteGrise").path("preImmatriculation").isNull() ? null : jsonNode.path("carteGrise").path("preImmatriculation").asText()
                );
                carteGrise.setDateDebutValid(
                    jsonNode.path("carteGrise").path("dateDebutValid").isNull() ? null : DateParser(jsonNode.path("carteGrise").path("dateDebutValid").asText())
                );
                carteGrise.setDateFinValid(
                    jsonNode.path("carteGrise").path("dateFinValid").isNull() ? null : DateParser(jsonNode.path("carteGrise").path("dateFinValid").asText())
                );
                carteGrise.setSsdt_id(
                    jsonNode.path("carteGrise").path("ssdt_id").isNull() ? null : jsonNode.path("carteGrise").path("ssdt_id").asText()
                );
                carteGrise.setCommune(
                    jsonNode.path("carteGrise").path("commune").isNull() ? null : jsonNode.path("carteGrise").path("commune").asText()
                );
                carteGrise.setGenreVehicule(
                    jsonNode.path("carteGrise").path("genreVehicule").isNull() ? null : jsonNode.path("carteGrise").path("genreVehicule").asText()
                );
                carteGrise.setEnregistrement(
                    jsonNode.path("carteGrise").path("enregistrement").isNull() ? null : jsonNode.path("carteGrise").path("enregistrement").asText()
                );
                carteGrise.setType(
                    jsonNode.path("carteGrise").path("type").isNull() ? null : jsonNode.path("carteGrise").path("type").asText()
                );
                carteGrise.setDateDelivrance(
                    jsonNode.path("carteGrise").path("dateDelivrance").isNull() ? null : DateParser(jsonNode.path("carteGrise").path("dateDelivrance").asText())
                );
                carteGrise.setLieuDedelivrance(
                    jsonNode.path("carteGrise").path("lieuDedelivrance").isNull() ? null : jsonNode.path("carteGrise").path("lieuDedelivrance").asText()
                );
                carteGrise.setProprietaireVehicule(proprietaireVehicule);

                log.info("created carteGrise successfully, product started");

                Produit produit = new Produit();
                produit.setProduitId(UUID.fromString(jsonNode.get("carteGrise").get("produit").get("produitId").asText()));
                produit.setLibelle(jsonNode.get("carteGrise").get("produit").get("libelle").asText());
                produit.setImg(jsonNode.get("carteGrise").get("produit").get("img").asText());
                produit.setDescription(jsonNode.get("carteGrise").get("produit").get("description").asText());
                produit.setPrix(jsonNode.get("carteGrise").get("produit").get("prix").asInt());
                produit.setDelaiValidite(jsonNode.get("carteGrise").get("produit").get("delaiValidite").asInt());
                
                log.info("created carteGrise product");
                CategorieProduit categoryproduit = new CategorieProduit();
                categoryproduit.setCategorieProduitId(UUID.fromString(jsonNode.get("carteGrise").get("produit").get("categorieProduit").get("categorieProduitId").asText()));
                categoryproduit.setLibelle(jsonNode.get("carteGrise").get("produit").get("categorieProduit").get("libelle").asText());
                categoryproduit.setDescription(jsonNode.get("carteGrise").get("produit").get("categorieProduit").get("description").asText());
                
                produit.setCategorieProduit(categoryproduit);
                log.info("created categoryproduit");
                
                CategorieVehicule  categorieVehicule = new CategorieVehicule();
                categorieVehicule.setId(UUID.fromString(jsonNode.get("carteGrise").get("produit").get("categorieVehicule").get("id").asText()));
                categorieVehicule.setType(jsonNode.get("carteGrise").get("produit").get("categorieVehicule").get("type").asText());
                
                produit.setCategorieVehicule(categorieVehicule);
                carteGrise.setProduit(produit);
                log.info("created categorie vehicle");
                
                
                if(jsonNode.get("carteGrise").has("Inspection") && !jsonNode.get("carteGrise").get("vehicule").isNull()){
                    Vehicule vehicule = new Vehicule();
                    vehicule.setVehiculeId(UUID.fromString(jsonNode.get("carteGrise").get("vehicule").get("vehiculeId").asText()));
                    vehicule.setScore(jsonNode.get("carteGrise").get("vehicule").get("score").asInt());
                    vehicule.setChassis(jsonNode.get("carteGrise").get("vehicule").get("chassis").asText());
                    vehicule.setCylindre(jsonNode.get("carteGrise").get("vehicule").get("cylindre").asInt());
                    vehicule.setPoidsVide(jsonNode.get("carteGrise").get("vehicule").get("poidsVide").asInt());
                    vehicule.setPuissAdmin(jsonNode.get("carteGrise").get("vehicule").get("puissAdmin").asInt());
                    vehicule.setCarrosserie(jsonNode.get("carteGrise").get("vehicule").get("carrosserie").asText());
                    vehicule.setChargeUtile(jsonNode.get("carteGrise").get("vehicule").get("chargeUtile").asInt());
                    vehicule.setPlaceAssise(jsonNode.get("carteGrise").get("vehicule").get("placeAssise").asInt());
                    vehicule.setTypeVehicule(jsonNode.get("carteGrise").get("vehicule").get("typeVehicule").asText());
                    vehicule.setPoidsTotalCha(jsonNode.get("carteGrise").get("vehicule").get("poidsTotalCha").asInt());
                    vehicule.setPremiereMiseEnCirculation(DateParser(jsonNode.get("carteGrise").get("vehicule").get("premiereMiseEnCirculation").asText()));

                    Energie energie = new Energie();
                    energie.setEnergieId(UUID.fromString(jsonNode.get("carteGrise").get("vehicule").get("energie").get("energieId").asText()));
                    energie.setLibelle(jsonNode.get("carteGrise").get("vehicule").get("energie").get("libelle").asText());
                    
                    vehicule.setEnergie(energie);
                    MarqueVehicule marqueVehicule = new MarqueVehicule(); 
                    marqueVehicule.setMarqueVehiculeId(UUID.fromString(jsonNode.get("carteGrise").get("vehicule").get("marqueVehicule").get("marqueVehiculeId").asText()));
                    marqueVehicule.setLibelle(jsonNode.get("carteGrise").get("vehicule").get("marqueVehicule").get("libelle").asText());
                    marqueVehicule.setDescription(jsonNode.get("carteGrise").get("vehicule").get("marqueVehicule").get("description").asText());
    
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

                Control control = new Control();
                control.setId(UUID.fromString(jsonNode.get("control").get("id").asText()));
                control.setActiveStatus(jsonNode.get("control").get("active_status").asBoolean());
                control.setStatus(StatusType.valueOf(jsonNode.get("control").get("status").asText()));
                visite.setControl(control);

                if(jsonNode.has("verbal_process_id") && !jsonNode.get("verbal_process_id").isNull()){
                    VerbalProcess verbalProcess = new VerbalProcess();
                    verbalProcess.setId(
                        jsonNode.path("verbal_process_id").isNull() ? null : UUID.fromString(jsonNode.path("verbal_process_id").asText())
                    );
                    // Set status for verbalProcess
                    verbalProcess.setStatus(
                       jsonNode.path("verbalStatus").asBoolean()
                    );
                    visite.setProcess(verbalProcess);
                }

                if(jsonNode.has("Inspection") && !jsonNode.get("Inspection").isNull()){
                    Inspection inspection = new Inspection();
                                        
                        // Set idInspection
                        inspection.setIdInspection(
                            jsonNode.path("Inspection").path("id").isNull() ? null : UUID.fromString(jsonNode.path("Inspection").path("id").asText())
                        );

                        // Set chassis
                        inspection.setChassis(
                            jsonNode.path("Inspection").path("chassis").isNull() ? null : jsonNode.path("Inspection").path("chassis").asText()
                        );

                        // Set essieux
                        inspection.setEssieux(
                            jsonNode.path("Inspection").path("essieux").asInt()
                        );

                        // Set dateFin
                        inspection.setDateFin(
                            jsonNode.path("Inspection").path("date_fin").isNull() ? null : DateParser(jsonNode.path("Inspection").path("date_fin").asText())
                        );

                        // Set bestPlate
                        inspection.setBestPlate(
                            jsonNode.path("Inspection").path("bestPlate").isNull() ? null : jsonNode.path("Inspection").path("bestPlate").asText()
                        );

                        // Set kilometrage
                        inspection.setKilometrage(
                            jsonNode.path("Inspection").path("kilometrage").asInt()
                        );

                        // Set activeStatus
                        inspection.setActiveStatus(
                            jsonNode.path("Inspection").path("activeStatus").asBoolean()
                        );

                    // Set distancePercentage
                    inspection.setDistancePercentage(
                        jsonNode.path("Inspection").path("distance_percentage").asInt()
                    );

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
   



    @Cacheable
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
                "'verbalStatus', CASE vb.status  WHEN b'1' THEN 1 ELSE 0 END, " +
                "'verbal_process_id', vb.id, " +
                "'Inspection', JSON_OBJECT( " +
				" 'id', i.id, " +
				" 'activeStatus', CASE  i.active_status WHEN b'1' THEN 1 ELSE 0 END, " +
				" 'bestPlate', i.best_plate, " +
				" 'chassis', i.chassis, " +
				" 'date_debut', i.date_debut, " +
				" 'date_fin', i.date_fin, " +
				" 'distance_percentage', i.distance_percentage, " +
				" 'essieux', i.essieux, " + 
				" 'kilometrage', i.kilometrage " +
            " ), " +
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
                "JOIN  t_inspection i on i.visite_id = v.id " +
                "JOIN  verbal_process vb on vb.visite_id = v.id " +
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

                log.info("creating partner started");
                Partenaire partenaire = new Partenaire();
                partenaire.setPartenaireId(UUID.fromString(jsonNode.get("carteGrise").get("proprietaireVehicule").get("partenaire").get("partenaireId").asText()));
                partenaire.setNom(jsonNode.get("carteGrise").get("proprietaireVehicule").get("partenaire").get("nom").asText());
                partenaire.setPrenom(jsonNode.get("carteGrise").get("proprietaireVehicule").get("partenaire").get("prenom").asText());
                
                log.info("creating partner finished, propertaire vehicle started");
                ProprietaireVehicule proprietaireVehicule = new ProprietaireVehicule();
                proprietaireVehicule.setProprietaireVehiculeId(UUID.fromString(jsonNode.get("carteGrise").get("proprietaireVehicule").get("proprietaireVehiculeId").asText()));
                proprietaireVehicule.setDescription(jsonNode.get("carteGrise").get("proprietaireVehicule").get("description").asText());
                proprietaireVehicule.setScore(jsonNode.get("carteGrise").get("proprietaireVehicule").get("score").asInt());
                proprietaireVehicule.setPartenaire(partenaire);

                log.info("creating propertaire vehicule finished, cartegrise started");

                // Construct CarteGrise instance
                CarteGrise carteGrise = new CarteGrise();
                carteGrise.setCarteGriseId(UUID.fromString(jsonNode.get("carteGrise").get("carteGriseId").asText()));
                carteGrise.setNumImmatriculation(jsonNode.get("carteGrise").get("numImmatriculation").asText());
                carteGrise.setPreImmatriculation(jsonNode.get("carteGrise").get("preImmatriculation").asText());
                carteGrise.setDateDebutValid(DateParser(jsonNode.get("carteGrise").get("dateDebutValid").asText()));
                carteGrise.setDateFinValid(DateParser(jsonNode.get("carteGrise").get("dateFinValid").asText()));
                
                carteGrise.setSsdt_id(jsonNode.get("carteGrise").get("ssdt_id").asText());
                carteGrise.setCommune(jsonNode.get("carteGrise").get("commune").asText());
                carteGrise.setMontantPaye(Double.parseDouble(jsonNode.get("carteGrise").get("montantPaye").asText()));
                carteGrise.setVehiculeGage(parseBooleanValue(jsonNode.get("carteGrise").get("vehiculeGage").asText()));
                carteGrise.setGenreVehicule(jsonNode.get("carteGrise").get("genreVehicule").asText());
                carteGrise.setEnregistrement(jsonNode.get("carteGrise").get("enregistrement").asText());
                carteGrise.setType(jsonNode.get("carteGrise").get("type").asText());
                carteGrise.setDateDelivrance(DateParser(jsonNode.get("carteGrise").get("dateDelivrance").asText()));
                carteGrise.setLieuDedelivrance(jsonNode.get("carteGrise").get("lieuDedelivrance").asText());
                carteGrise.setCentre_ssdt(jsonNode.get("carteGrise").get("centre_ssdt").asText());
                carteGrise.setType(jsonNode.get("carteGrise").get("type").asText());
                carteGrise.setProprietaireVehicule(proprietaireVehicule);

                log.info("created carteGrise successfully, product started");

                Produit produit = new Produit();
                produit.setProduitId(UUID.fromString(jsonNode.get("carteGrise").get("produit").get("produitId").asText()));
                produit.setLibelle(jsonNode.get("carteGrise").get("produit").get("libelle").asText());
                produit.setImg(jsonNode.get("carteGrise").get("produit").get("img").asText());
                produit.setDescription(jsonNode.get("carteGrise").get("produit").get("description").asText());
                produit.setPrix(jsonNode.get("carteGrise").get("produit").get("prix").asInt());
                produit.setDelaiValidite(jsonNode.get("carteGrise").get("produit").get("delaiValidite").asInt());
                
                log.info("created carteGrise product");
                CategorieProduit categoryproduit = new CategorieProduit();
                categoryproduit.setCategorieProduitId(UUID.fromString(jsonNode.get("carteGrise").get("produit").get("categorieProduit").get("categorieProduitId").asText()));
                categoryproduit.setLibelle(jsonNode.get("carteGrise").get("produit").get("categorieProduit").get("libelle").asText());
                categoryproduit.setDescription(jsonNode.get("carteGrise").get("produit").get("categorieProduit").get("description").asText());
                
                produit.setCategorieProduit(categoryproduit);
                log.info("created categoryproduit");
                
                CategorieVehicule  categorieVehicule = new CategorieVehicule();
                categorieVehicule.setId(UUID.fromString(jsonNode.get("carteGrise").get("produit").get("categorieVehicule").get("id").asText()));
                categorieVehicule.setType(jsonNode.get("carteGrise").get("produit").get("categorieVehicule").get("type").asText());
                
                produit.setCategorieVehicule(categorieVehicule);
                carteGrise.setProduit(produit);
                log.info("created categorie vehicle");
                
                
                
                Vehicule vehicule = new Vehicule();
                vehicule.setVehiculeId(UUID.fromString(jsonNode.get("carteGrise").get("vehicule").get("vehiculeId").asText()));
                vehicule.setScore(jsonNode.get("carteGrise").get("vehicule").get("score").asInt());
                vehicule.setChassis(jsonNode.get("carteGrise").get("vehicule").get("chassis").asText());
                vehicule.setCylindre(jsonNode.get("carteGrise").get("vehicule").get("cylindre").asInt());
                vehicule.setPoidsVide(jsonNode.get("carteGrise").get("vehicule").get("poidsVide").asInt());
                vehicule.setPuissAdmin(jsonNode.get("carteGrise").get("vehicule").get("puissAdmin").asInt());
                vehicule.setCarrosserie(jsonNode.get("carteGrise").get("vehicule").get("carrosserie").asText());
                vehicule.setChargeUtile(jsonNode.get("carteGrise").get("vehicule").get("chargeUtile").asInt());
                vehicule.setPlaceAssise(jsonNode.get("carteGrise").get("vehicule").get("placeAssise").asInt());
                vehicule.setTypeVehicule(jsonNode.get("carteGrise").get("vehicule").get("typeVehicule").asText());
                vehicule.setPoidsTotalCha(jsonNode.get("carteGrise").get("vehicule").get("poidsTotalCha").asInt());
                vehicule.setPremiereMiseEnCirculation(DateParser(jsonNode.get("carteGrise").get("vehicule").get("premiereMiseEnCirculation").asText()));
            
               
                Energie energie = new Energie();
                energie.setEnergieId(UUID.fromString(jsonNode.get("carteGrise").get("vehicule").get("energie").get("energieId").asText()));
                energie.setLibelle(jsonNode.get("carteGrise").get("vehicule").get("energie").get("libelle").asText());
                
                vehicule.setEnergie(energie);
                
                MarqueVehicule marqueVehicule = new MarqueVehicule(); 
                marqueVehicule.setMarqueVehiculeId(UUID.fromString(jsonNode.get("carteGrise").get("vehicule").get("marqueVehicule").get("marqueVehiculeId").asText()));
                marqueVehicule.setLibelle(jsonNode.get("carteGrise").get("vehicule").get("marqueVehicule").get("libelle").asText());
                marqueVehicule.setDescription(jsonNode.get("carteGrise").get("vehicule").get("marqueVehicule").get("description").asText());

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
                
                
                    visite.setCarteGrise(carteGrise);

                    VerbalProcess verbalProcess = new VerbalProcess();
                    verbalProcess.setId(UUID.fromString(jsonNode.get("verbal_process_id").asText()));
                    verbalProcess.setStatus(jsonNode.get("verbalStatus").asBoolean());

                    visite.setProcess(verbalProcess);
                

                log.info("visite created associating inspection");


                Inspection inspection = new Inspection();
                inspection.setIdInspection(UUID.fromString(jsonNode.get("Inspection").get("id").asText()));
                inspection.setChassis(jsonNode.get("Inspection").get("chassis").asText());
                inspection.setEssieux(jsonNode.get("Inspection").get("essieux").asInt());
                inspection.setDateFin(DateParser(jsonNode.get("Inspection").get("date_fin").asText()));


                inspection.setBestPlate(jsonNode.get("Inspection").get("bestPlate").asText());
                // inspection.setDateDebut(DateParser(jsonNode.get("inspection").get("date_debut").asText()));


                inspection.setKilometrage(jsonNode.get("Inspection").get("kilometrage").asInt());
                inspection.setActiveStatus(jsonNode.get("Inspection").get("activeStatus").asBoolean());
                inspection.setDistancePercentage(jsonNode.get("Inspection").get("distance_percentage").asInt());
                
                visite.setInspection(inspection);

                log.info("created data binder successfully");

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

    

    public List<Visite> getsearchedVisitList(String search, UUID orgId, Pageable pageable){
        List<Visite> visiteEnCours = visiteRepository.findByRef(search, orgId, pageable);
        return visiteEnCours;
    }

    // end flemming i implimneted
    
    public List<Visite> searchedVisitList(String search, UUID orgId, Pageable pageable){
        List<Visite> visiteEnCours = visiteRepository.findByRef(search, orgId, pageable);
        return visiteEnCours;
    }
    
    @Cacheable
    public List<Visite> searchedVisitListstatus(String search, UUID orgId, int status, Pageable pageable){
        List<Visite> visiteEnCours = visiteRepository.findByRefAndStatus(search, orgId, status, pageable);
        return visiteEnCours;
    }
    @Cacheable(key = "'listforadmin_' + '5555'")
    public List<Visite> visitListForAdmin(String search, Pageable pageable){
        List<Visite> visiteEnCours = visiteRepository.findByActiveStatusTrueAndCarteGrise_NumImmatriculationContainingIgnoreCaseOrCarteGrise_Vehicule_ChassisContainingIgnoreCaseOrCaissier_Partenaire_NomContainingIgnoreCaseOrCarteGrise_ProprietaireVehicule_Partenaire_NomContainingIgnoreCaseAndOrganisation_NomContainingIgnoreCaseOrderByCreatedDateDesc(search,search,search,search,search,pageable);
        return visiteEnCours;
    }
    @Cacheable(key = "'endedvisiteview_' + #orgId")
    public List<Visite> endedVisitList(UUID orgId){
        List<Visite> visiteEnCours = visiteRepository.findByOrganisation_OrganisationIdAndEncoursFalseAndActiveStatusTrueOrderByCreatedDateDesc(orgId);
        return visiteEnCours;
    }
    @Cacheable(key = "'encourlistforcontext_' + #orgId")
    public List<Visite> enCoursVisitListForContext(UUID orgId) {
        List<Visite> visiteEnCours = visiteRepository.findByEncoursTrueAndOrganisation_OrganisationIdAndActiveStatusTrueOrderByCreatedDateDesc(orgId);
        return visiteEnCours;
    }
    @Cacheable(key = "'allvisitelistview_' + #orgId")
    public List<Visite> AllVisitList(UUID orgId) {
        List<Visite> visiteEnCours = new ArrayList<>();
        visiteRepository.findByOrganisation_OrganisationIdAndActiveStatusTrueOrderByCreatedDateDesc(orgId).forEach(visiteEnCours::add);
        return visiteEnCours;
    }
   
    public void terminerInspection(UUID visiteId) throws IOException {
        Visite visite = new Visite();
        visite = visiteRepository.findById(visiteId).get();
        visite.setEncours(false);
        visite.setDateFin(LocalDateTime.now());
        visite.setStatut(4);
        Visite visite2 = visiteRepository.save(visite);
        visite.getOrganisation().getUtilisateurs().forEach(utilisateur -> {
            notificationService.dipatchVisiteToMember(utilisateur.getKeycloakId(), visite2, true);
        });

    }

    // new 
    @Cacheable
    public Page<Visite> listParStatus(int status, UUID orgId, Pageable pageable) {
        return visiteRepository.findByActiveStatusTrueAndEncoursTrueAndStatutAndOrganisation_OrganisationId(status, orgId, pageable);
    }
    // flemming implimented
    @Cacheable(key = "'graphview_' + #orgId")
    public List<GraphView> listParStatusForGraphViews(UUID orgId) {
        String[] statusNames = {"maj", "A inspecter", "En cours test", "A signer", "A imprimer", "A enregister", "A certifier", "Accepté", "Refusé"};
        List<GraphView> graphViews = new ArrayList<>();
        int[] datas = new int[9];

        for (int i = 0; i < statusNames.length; i++) {
            String sql = String.format("SELECT COUNT(*) FROM t_visite v WHERE v.encours=1 and v.active_status=1 and v.statut = %d AND v.organisation_id = '%s'", i, orgId.toString());
            Query query = entityManager.createNativeQuery(sql);
            int count = ((Number) query.getSingleResult()).intValue();
            datas[i] = count;
            graphViews.add(new GraphView(statusNames[i], count));
        }

        return graphViews;
    }

    @Cacheable
    public List<Visite> listParStatus(int status, UUID orgId) {
        return visiteRepository.findByActiveStatusTrueAndEncoursTrueAndStatutAndOrganisation_OrganisationId(status, orgId, Sort.by(Sort.Direction.DESC, "createdDate"));
    }


    @Cacheable(key = "'kanbanview_' + #organisationId")
    public Map<String, Object> MainlistParStatusForkanban(UUID organisationId) {
        List<Object[]> visiteStatusCount = visiteRepository.getVisiteStatusCountByOrganisation(organisationId);
        log.info("Retrieved visiteStatusCount: {}", visiteStatusCount);

        Map<String, Object> statusCountMap = initializeStatusCountMap();
        List<String> enCoursTestNumImmatriculation = visiteRepository.getEnCoursTestNumImmatriculation(organisationId, PageRequest.of(0, 15, Sort.Direction.DESC, "createdDate"));
    
        for (Object[] row : visiteStatusCount) {
            int status = (int) row[0];
            long count = (long) row[1];
            if (status == 2) {
                Map<String, Object> enCoursTestData = new HashMap<>();
                enCoursTestData.put("count", count);
                enCoursTestData.put("visites", enCoursTestNumImmatriculation);
                statusCountMap.put(getStatusLabel(status), enCoursTestData);
                log.info("Updated En cours test data: {}", enCoursTestData);
            } else {
                statusCountMap.put(getStatusLabel(status), count);
            }
        }
        log.info("Final statusCountMap: {}", statusCountMap);
        return statusCountMap;
    }

    private Map<String, Object> initializeStatusCountMap() {
        Map<String, Object> statusCountMap = new HashMap<>();
        statusCountMap.put("maj", 0L);
        statusCountMap.put("A inspecter", 0L);
        Map<String, Object> enCoursTestMap = new HashMap<>();
        enCoursTestMap.put("count", 0L);
        enCoursTestMap.put("visite", new ArrayList<String>());
        statusCountMap.put("En cours test", enCoursTestMap);
        statusCountMap.put("A signer", 0L);
        statusCountMap.put("En Attente conformité", 0L);
        statusCountMap.put("Non conforme", 0L);
        statusCountMap.put("A imprimer", 0L);
        statusCountMap.put("Refusé", 0L);
        statusCountMap.put("A certifier", 0L);
        statusCountMap.put("Accepté", 0L);
        statusCountMap.put("A approuver", 0L);
        statusCountMap.put("erreur", 0L);
        return statusCountMap;
    }


    
    
    private String getStatusLabel(int code) {
        switch (code) {
            case 0:
                return "maj";
            case 1:
                return "A inspecter";
            case 2:
                return "En cours test";
            case 3:
                return "A signer";
            case 4:
                return "En Attente conformité";
            case 5:
                return "Non conforme";
            case 6:
                return "A imprimer";
            case 7:
                return "Refusé";
            case 8:
                return "A certifier";
            case 9:
                return "Accepté";
            case 10:
                return "A approuver";
            default:
                return "erreur";
        }
    }
    
    
    @Cacheable
    public List<KanBanSimpleData> listParStatusForkanban(int status, UUID orgId) {
        List<Visite> visites = visiteRepository.findByActiveStatusTrueAndEncoursTrueAndStatutAndOrganisation_OrganisationId(status, orgId, Sort.by(Sort.Direction.DESC, "createdDate"));
        List<KanBanSimpleData> kanBanSimpleDatas = new ArrayList<>();

        KanBanSimpleData kanBanSimpleData;
        for (Visite visite : visites){
            kanBanSimpleData = new KanBanSimpleData();
            kanBanSimpleData.setContreVisite(visite.isContreVisite());
            kanBanSimpleData.setRef(visite.getCarteGrise().getNumImmatriculation());
            kanBanSimpleDatas.add(kanBanSimpleData);
        }
        return kanBanSimpleDatas;
    }
    @CacheEvict(allEntries = true)
    public Visite commencerInspection(Visite visite) throws IOException {

        visite.setDateFin(LocalDateTime.now());
        visite.setStatut(2);
        Visite visite2 = visiteRepository.save(visite);
        visite.getOrganisation().getUtilisateurs().forEach(utilisateur -> {
            notificationService.dipatchVisiteToMember(utilisateur.getKeycloakId(), visite2, true);
        });
        return visite2;

    }

    public boolean isVisiteInitial(String ref, UUID organisationId) throws VisiteEnCoursException {
        List<Visite> visites = findByReference(ref, organisationId);

        Visite visite = visites.stream().filter(visite1 -> visite1.isActiveStatus()).max(Comparator.comparing(Visite::getCreatedDate))
                .orElse(null);

        if (visite != null) {

            if (visite.getControl().getStatus().equals(StatusType.INITIALIZED)) {

                throw new VisiteEnCoursException();
            }
            if (visite.getControl().getStatus().equals(StatusType.VALIDATED)) {
                return true;
            }
            if (visite.getControl().getStatus().equals(StatusType.REJECTED)) {
                LocalDateTime now = LocalDateTime.now();

                if (visite.getControl().getContreVDelayAt().isAfter(now)) {
                    return false;
                }
                if (visite.getControl().getContreVDelayAt().isBefore(now) || visite.getControl().getContreVDelayAt().equals(now)) {
                    return true;
                }
            }
        }
        return true;
    }

    public boolean isVehiculeExist(String ref, UUID organisationId) {
        if (findByReference(ref, organisationId).isEmpty())
            return false;
        return true;
    }
    public int getVisitsOfTheDay(){
        return visiteRepository.visitsOfTheDay().size();
    }

    public List<Visite> getVisitsListOfTheDay(){
        List<Visite> visites = visiteRepository.visitsOfTheDay();
        return visites;
    }
    public List<Visite> getVisitsListOfTheDayBefore(){
        List<Visite> visites = visiteRepository.visitsOfTheDay();
        return visites;
    }
    public List<Visite> findByOrganisationId(UUID id){
        List<Visite> visites = new ArrayList<>();
        visiteRepository.findByOrganisation_OrganisationIdAndActiveStatusTrue(id).forEach(visites::add);
        return visites;
    }

    public List<Visite> visiteBydate(LocalDateTime d, LocalDateTime f){
        List<Visite> visites = new ArrayList<>();
        visiteRepository.visiteByDate(d,f).forEach(visites::add);
        return visites;
    }
    public int getOrganisationOccurence(Organisation o, List<Visite> visites){
        int i = 0;
        for(Visite v : visites){
            if(o.getOrganisationId() == v.getOrganisation().getOrganisationId())
                i ++;
        }
        return i;
    }
    public List<OrganisationTopDTO> getTopOrganisation(){
        List<Organisation> orgs = os.findAllChildForSelect();
        List<Visite> visiteDay = visiteBydate(LocalDateTime.now().toLocalDate().atStartOfDay().minusDays(0),LocalDateTime.now());
        List<Visite> visiteDayBefore = visiteBydate(LocalDateTime.now().toLocalDate().atStartOfDay().minusDays(2),LocalDateTime.now().toLocalDate().atStartOfDay().minusDays(1));
        int visiteDayOrganisation;
        int visiteDayBeforOrganisation ;
        List<OrganisationTopDTO> org = new ArrayList<>();
        OrganisationTopDTO o;
        for(Organisation or : orgs){
            o = new OrganisationTopDTO();
            visiteDayOrganisation = getOrganisationOccurence(or,visiteDay);
            visiteDayBeforOrganisation = getOrganisationOccurence(or,visiteDayBefore);
            o.setOrganisation(or);
            o.setValue( Math.round(visiteDay.size() == 0 ? 0 : visiteDayOrganisation * 100 /visiteDay.size()));
            o.setPourcentage(pourcentageComparator(visiteDayBeforOrganisation, visiteDayOrganisation));
            org.add(o);
        }

        Collections.sort(org, Comparator.comparing(OrganisationTopDTO::getValue).reversed());
        return org;
    }
    public int pourcentageComparator(int i, int j){
        if(i==0)
            return j*100;
        return Math.round((j-i) *100/i);


    }

    public void  dispatchEdit(Visite visite)  {

        for(SseEmitter emitter:emitters){
            try{
                log.info("-----sse----");
                if(visite.getStatut() >=1){
                    emitter.send(SseEmitter.event().name("edit_visit").data(
                            new NewListView(visite.getIdVisite(), visite.getCarteGrise().getProduit(), visite.typeRender(), visite.getCarteGrise().getNumImmatriculation(),
                                    (visite.getCarteGrise().getVehicule()==null
                                            ? "": (visite.getCarteGrise().getVehicule().getChassis()==null
                                            ? "" : visite.getCarteGrise().getVehicule().getChassis())),
                                    (visite.getCarteGrise().getProprietaireVehicule()
                                            .getPartenaire()
                                            .getNom()
                                            == null
                                            ? null : visite.getCarteGrise().getProprietaireVehicule()
                                            .getPartenaire()
                                            .getNom())
                                    ,Utils.parseDate(visite.getCreatedDate()), visite.getCreatedDate(),
                                    getHTML(visite), visite.getStatut(), visite.getIdVisite(),visite.isContreVisite(),
                                    visite.getInspection()==null? null : visite.getInspection().getIdInspection(), visite.getCarteGrise(), visite.getOrganisation().isConformity(),
                                    visite.getIsConform(),
                                    visite.getOrganisation().getNom() ,visite.getInspection()==null? null : visite.getInspection().getBestPlate(), visite.getInspection()==null? 0 : visite.getInspection().getDistancePercentage(),
                                    visite.getCreatedDate().format(SseController.dateTimeFormatter), true, visite.getDocument())));
                    emitter.send(SseEmitter.event().name("controleur_visit").data(visite));
                }
                else{

                    emitter.send(SseEmitter.event().name("new_visit").data(new NewListView(visite.getIdVisite(), visite.getCarteGrise().getProduit(),visite.typeRender(), visite.getCarteGrise().getNumImmatriculation(),
                            (visite.getCarteGrise().getVehicule()==null
                                    ? "": (visite.getCarteGrise().getVehicule().getChassis()==null
                                    ? "" : visite.getCarteGrise().getVehicule().getChassis())),
                            (visite.getVente().getClient() == null
                                    ? visite.getVente().getContact().getPartenaire().getNom() : visite.getVente().getClient().getPartenaire().getNom()),
                            Utils.parseDate(visite.getCreatedDate()), visite.getCreatedDate(),
                            getHTML(visite), visite.getStatut(), visite.getIdVisite(),visite.isContreVisite(),
                            visite.getInspection()==null? null : visite.getInspection().getIdInspection(), visite.getCarteGrise(), visite.getOrganisation().isConformity(),
                            visite.getIsConform(),
                            visite.getOrganisation().getNom() ,visite.getInspection()==null? null : visite.getInspection().getBestPlate(),
                            visite.getInspection()==null? 0 : visite.getInspection().getDistancePercentage(),
                            visite.getCreatedDate().format(SseController.dateTimeFormatter), false, visite.getDocument())));
                }

            }catch(IOException e){
                log.info("---SSE ERROR---");
                emitters.remove(emitter);
            }
        }
    }

    //dispatching all event
    public void dispatchNewVisit(Visite visite){
        for(SseEmitter emitter:emitters){
            System.out.println("SSE send a info");
            try{
                emitter.send(SseEmitter.event().name("new_visit").data(
                        new NewListView(visite.getIdVisite(), visite.getCarteGrise().getProduit(),visite.typeRender(), visite.getCarteGrise().getNumImmatriculation(),
                                (visite.getCarteGrise().getVehicule()==null
                                        ? "": (visite.getCarteGrise().getVehicule().getChassis()==null
                                        ? "" : visite.getCarteGrise().getVehicule().getChassis())),
                                (visite.getCarteGrise().getProprietaireVehicule()
                                        .getPartenaire()
                                        .getNom()
                                        == null
                                        ? null : visite.getCarteGrise().getProprietaireVehicule()
                                        .getPartenaire()
                                        .getNom()),
                                Utils.parseDate(visite.getCreatedDate()), visite.getCreatedDate(),
                                getHTML(visite), visite.getStatut(), visite.getIdVisite(),visite.isContreVisite(),
                                visite.getInspection()==null? null : visite.getInspection().getIdInspection(), visite.getCarteGrise(), visite.getOrganisation().isConformity(),
                                visite.getIsConform(),
                                visite.getOrganisation().getNom() ,visite.getInspection()==null? null : visite.getInspection().getBestPlate(),
                                visite.getInspection()==null? 0 : visite.getInspection().getDistancePercentage(),
                                visite.getCreatedDate().format(SseController.dateTimeFormatter), true,  visite.getDocument())));

            }catch(IOException e){
                emitters.remove(emitter);
            }
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
                    return  "<span class=\"badge badge-info\"> REFUSE " + visite.statutRender() + "</span>";

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
        List<GieglanFileIcon> icons =new ArrayList<>();
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
        return icons;

    }



}
