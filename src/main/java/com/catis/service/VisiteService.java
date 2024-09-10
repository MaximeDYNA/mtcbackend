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
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import org.springframework.transaction.annotation.Isolation;
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
        return visiteRepository.LastVisiteWithTestIsOkDirectQuery(controlId, visiteId);
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
    @Transactional(rollbackFor = VisiteEnCoursException.class)
    public Visite ajouterVisite(CarteGrise cg, double montantTotal, double montantEncaisse, UUID organisationId, Caissier caissier, String document, String certidocsId) throws VisiteEnCoursException {
        Visite visite = new Visite();

        log.info("adding visite");

        Organisation organisation = os.findByOrganisationId(organisationId);
        if (montantEncaisse < montantTotal) {
            visite.setStatut(9);
        } else{
            visite.setStatut(0);
        }

        // Merged isVisiteInitial logic
        List<Visite> visites = findByReference(cg.getNumImmatriculation(), organisationId);
        log.info("found " + visites.size() + "  previous visites for this cartegrise");
        Visite latestVisite = visites.stream()
        .filter(v -> v.isActiveStatus())
        .max(Comparator.comparing(Visite::getCreatedDate))
        .orElse(null);

        log.info("check for previous visite completed");

        boolean isInitial = true;
        if (latestVisite != null) {
            log.info("latestVisite is not null, ID: " + latestVisite.getIdVisite());
            long start = System.currentTimeMillis();
            Control latestControl = latestVisite.getControl();
            long end = System.currentTimeMillis();
            log.info("Control retrieval took " + (end - start) + "ms");
            if(latestControl != null) {
                log.info("latestControl is not null, Status: " + latestControl.getStatus());
                log.info("Comparing: " + latestControl.getStatus() + " with " + StatusType.INITIALIZED);
                log.info("Comparison result: " + latestControl.getStatus().equals(StatusType.INITIALIZED));
                if (latestControl.getStatus().equals(StatusType.INITIALIZED)) {
    
                    throw new VisiteEnCoursException();
                }
                if (latestControl.getStatus().equals(StatusType.VALIDATED)) {
                    isInitial = true;
                }
                if (latestControl.getStatus().equals(StatusType.REJECTED)) {
                    LocalDateTime now = LocalDateTime.now();
    
                    if (latestControl.getContreVDelayAt().isAfter(now)) {
                        isInitial = false;
                    }
                    if (latestControl.getContreVDelayAt().isBefore(now) || latestControl.getContreVDelayAt().equals(now)) {
                        isInitial = true;
                    }
                 }
            }else {
                log.error("latestControl is null");
            }
            }else {
                log.error("latestVisite is null");
            }

        log.info("isInitial visited test was set to " + isInitial);

        if (isInitial) {
            visite.setContreVisite(false);
            visite.setEncours(true);
            visite.setCarteGrise(cg);
            visite.setDateDebut(LocalDateTime.now());
            Control control = new Control();
            List<Visite> visiteList = new ArrayList<>();
            visiteList.add(visite);
            control.setCarteGrise(cg);
            control.setStatus(StatusType.INITIALIZED);
            control.setVisites(visiteList);
            control.setOrganisation(organisation);
            visite.setControl(control);
            log.info("processed visite initial");
    
        }
        else {
            log.info("processing contre visite");
            visite.setContreVisite(true);
            visite.setStatut(1);
            List<Visite> vi = visiteRepository.getBeforeLastVisiteWithHisControl(cg.getNumImmatriculation(), PageRequest.of(0, 1));
            if (!vi.isEmpty()) {
                visite.setControl(vi.get(0).getControl());
            }
    
            visite.setEncours(true);
            visite.setCarteGrise(cg);
            visite.setDateDebut(LocalDateTime.now());
        }

        if (cg.getProduit().getLibelle().equals("dec")) {
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
    
    
    // old method cause occasional duplicates due to lack of transaction boundaries
    // @CacheEvict(allEntries = true)
    // @Transactional
    // public Visite ajouterVisite(CarteGrise cg, double montantTotal, double montantEncaisse, UUID organisationId, Caissier caissier, String document, String certidocsId) throws VisiteEnCoursException {
    //     Visite visite = new Visite();

    //     Organisation organisation = os.findByOrganisationId(organisationId);
    //     if (montantEncaisse < montantTotal) {
    //         visite.setStatut(9);
    //     } else
    //         visite.setStatut(0);

    //     if (isVisiteInitial(cg.getNumImmatriculation(), organisationId)) {
    //         visite.setContreVisite(false);
    //         visite.setEncours(true);
    //         visite.setCarteGrise(cg);
    //         visite.setDateDebut(LocalDateTime.now());
    //         Control control = new Control();
    //         List<Visite> visites = new ArrayList<>();
    //         visites.add(visite);
    //         control.setCarteGrise(cg);
    //         control.setStatus(StatusType.INITIALIZED);
    //         control.setVisites(visites);
    //         control.setOrganisation(organisation);
    //         visite.setControl(control);
    //         log.info("processed visite initial");
            
    //     } else {
    //         log.info("processing contre visite");
    //         visite.setContreVisite(true);
    //         visite.setStatut(1);
    //         List<Visite> vi =visiteRepository.getBeforeLastVisiteWithHisControl(cg.getNumImmatriculation(), PageRequest.of(0,1));
    //         if(!vi.isEmpty()) {
    //             visite.setControl(vi.get(0).getControl());
    //         }

    //         visite.setEncours(true);
    //         visite.setCarteGrise(cg);
    //         visite.setDateDebut(LocalDateTime.now());
    //     }

    //     if (cg.getProduit().getLibelle()== "dec") {
    //         return visite;
    //     }
    //     visite.setCaissier(caissier);
    //     visite.setOrganisation(organisation);
    //     visite.setReglophare(Visite.TestResult.PENDING);
    //     visite.setRipage(Visite.TestResult.PENDING);
    //     visite.setFreinage(Visite.TestResult.PENDING);
    //     visite.setSuspension(Visite.TestResult.PENDING);
    //     visite.setPollution(Visite.TestResult.PENDING);
    //     visite.setVisuel(Visite.TestResult.PENDING);
    //     visite.setDocument(document);
    //     visite.setCertidocsId(certidocsId);
    //     log.info("SAVING VISITE");
    //     visite = visiteRepository.save(visite);
    //     final Visite v = visite;
    //     dispatchNewVisit(visite);
    //     organisation.getUtilisateurs().forEach(utilisateur -> {
    //         notificationService.dipatchVisiteToMember(utilisateur.getKeycloakId(), v, false);
    //     });
    //     log.info("RETURNING VISITE");
    //     return visite;
    // }
    
    
    
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

    // flemming implimented
    @Cacheable
    public Page<NewListView> MainlistParStatus(int status, UUID orgId, Pageable pageable) {
        return visiteRepository.MainlistParStatus(status, orgId, pageable);
    }
   

    // flemming implimnted 
    @Cacheable
    public Page<NewListView> searchedVisitMainListstatus(String search, UUID orgId, int status, Pageable pageable) {
        return visiteRepository.searchedVisitMainListstatus(search, orgId, status, pageable);
    }
    
    
    // flemming implimented
    @Cacheable
    @Transactional
    public Page<NewListView> searchedVisitMainList(String search, UUID orgId, Pageable pageable) {
    return visiteRepository.searchedVisitMainList(search, orgId, pageable);
    }
   



    @Cacheable
    public Page<NewListView> endedMainVisitList(UUID organisationId, Pageable pageable) {
      return visiteRepository.endedMainVisitList(organisationId, pageable);
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

    // @Transactional
    public boolean isVisiteInitial(String ref, UUID organisationId) throws VisiteEnCoursException {
        log.info("isVisiteInitial check for organisation ongoing");
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
        // List<GieglanFileIcon> icons =new ArrayList<>();
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
        // return icons;

    }



}
