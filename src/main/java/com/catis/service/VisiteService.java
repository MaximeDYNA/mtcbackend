package com.catis.service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.catis.controller.SseController;
import com.catis.model.control.Control;
import com.catis.model.control.GieglanFile;
import com.catis.model.entity.*;
import com.catis.objectTemporaire.*;

import com.catis.repository.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.catis.controller.exception.VisiteEnCoursException;
import com.catis.model.control.Control.StatusType;
import com.catis.repository.VisiteRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static com.catis.controller.SseController.emitters;

@Service
public class VisiteService {

    @Autowired
    private VisiteRepository visiteRepository;

    @Autowired
    private OrganisationService os;
    @Autowired
    private CategorieTestVehiculeService cat;
    @Autowired
    private GieglanFileService gieglanFileService;
    @Autowired
    private NotificationService notificationService;



    private static Logger log = LoggerFactory.getLogger(VisiteService.class);

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



    public Visite add(Visite visite) {
        return visiteRepository.save(visite);
    }



    public List<Visite> findAll() {
        List<Visite> visites = new ArrayList<Visite>();
        visiteRepository.findAll().forEach(visites::add);
        return visites;
    }



    public List<Visite> findByReference(String ref, Long organisationId) {
        return visiteRepository.findByCarteGriseNumImmatriculationIgnoreCaseOrCarteGrise_Vehicule_ChassisIgnoreCaseAndOrganisation_OrganisationId(ref, ref, organisationId);
    }

    public Visite findById(Long i) {
        return visiteRepository.findById(i).get();
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

    @Transactional
    public Visite ajouterVisite(CarteGrise cg, double montantTotal, double montantEncaisse, Long organisationId, Caissier caissier, String document) throws VisiteEnCoursException {
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
            visite.setControl(control);

        } else {
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

        if (cg.getProduit().getProduitId() == 1) {
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

        visite = visiteRepository.save(visite);
        final Visite v = visite;
        //dispatchNewVisit(visite);
        /*organisation.getUtilisateurs().forEach(utilisateur -> {
            notificationService.dipatchVisiteToMember(utilisateur.getKeycloakId(), v, false);
        });*/

        notificationService.sendNotification(v.getCaissier().getUser().getKeycloakId(), new EventDto("test", new HashMap<>()));

        return visite;
    }

    public Visite modifierVisite(Visite visite) throws IOException {
        Visite v = visiteRepository.save(visite);
        dispatchEdit(visite);
        return v;
    }

    public boolean visiteEncours(String imCha, Long organisationId) {
        return !visiteRepository.findByActiveStatusTrueAndCarteGriseNumImmatriculationIgnoreCaseOrCarteGrise_Vehicule_ChassisIgnoreCaseAndOrganisation_OrganisationId(imCha, imCha, organisationId)
                .stream().filter(visites -> visites.getDateFin() == null).collect(Collectors.toList())
                .isEmpty();
    }
    public List<Visite> enCoursVisitList(Long orgId) {
        List<Visite> visiteEnCours = visiteRepository.findByOrganisation_OrganisationIdAndEncoursTrueAndActiveStatusTrueOrderByCreatedDateDesc(orgId);

        return visiteEnCours;
    }
    public List<Visite> getOrganisationVisiteWithTest(Long orgId, Pageable pageable) {
        List<Visite> visiteEnCours = visiteRepository.getOrganisationVisiteWithTest(orgId, pageable);

        return visiteEnCours;
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public List<Visite> enCoursVisitList(Long orgId, Pageable pageable) {
        List<Visite> visiteEnCours = visiteRepository.findByOrganisation_OrganisationIdAndEncoursTrueAndActiveStatusTrue(orgId, pageable);

        return visiteEnCours;
    }
    public Page<Visite> endedVisitList(Long orgId, Pageable pageable){
        Page<Visite> visiteEnCours = visiteRepository.findByOrganisation_OrganisationIdAndEncoursFalseAndActiveStatusTrueOrderByCreatedDateDesc(orgId, pageable);
        return visiteEnCours;
    }

    public List<Visite> searchedVisitList(String search, Long orgId, Pageable pageable){
        List<Visite> visiteEnCours = visiteRepository.findByRef(search, orgId, pageable);
        return visiteEnCours;
    }
    public List<Visite> visitListForAdmin(String search, Pageable pageable){
        List<Visite> visiteEnCours = visiteRepository.findByActiveStatusTrueAndCarteGrise_NumImmatriculationContainingIgnoreCaseOrCarteGrise_Vehicule_ChassisContainingIgnoreCaseOrCaissier_Partenaire_NomContainingIgnoreCaseOrCarteGrise_ProprietaireVehicule_Partenaire_NomContainingIgnoreCaseAndOrganisation_NomContainingIgnoreCaseOrderByCreatedDateDesc(search,search,search,search,search,pageable);
        return visiteEnCours;
    }
    public List<Visite> endedVisitList(Long orgId){
        List<Visite> visiteEnCours = visiteRepository.findByOrganisation_OrganisationIdAndEncoursFalseAndActiveStatusTrueOrderByCreatedDateDesc(orgId);
        return visiteEnCours;
    }
    public List<Visite> enCoursVisitListForContext(Long orgId) {
        List<Visite> visiteEnCours = visiteRepository.findByEncoursTrueAndOrganisation_OrganisationIdAndActiveStatusTrueOrderByCreatedDateDesc(orgId);
        return visiteEnCours;
    }
    public List<Visite> AllVisitList(Long orgId) {
        List<Visite> visiteEnCours = new ArrayList<>();
        visiteRepository.findByOrganisation_OrganisationIdAndActiveStatusTrueOrderByCreatedDateDesc(orgId).forEach(visiteEnCours::add);
        return visiteEnCours;
    }

    public void terminerInspection(Long visiteId) throws IOException {
        Visite visite = new Visite();
        visite = visiteRepository.findById(visiteId).get();
        visite.setEncours(false);
        visite.setDateFin(LocalDateTime.now());
        visite.setStatut(4);
        visite = visiteRepository.save(visite);
        dispatchEdit(visite);

    }

    public List<Visite> listParStatus(int status, Long orgId) {
        return visiteRepository.findByActiveStatusTrueAndEncoursTrueAndStatutAndOrganisation_OrganisationId(status, orgId, Sort.by(Sort.Direction.DESC, "createdDate"));
    }

    public List<KanBanSimpleData> listParStatusForkanban(int status, Long orgId) {
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
    public Visite commencerInspection(Visite visite) throws IOException {

        visite.setDateFin(LocalDateTime.now());
        visite.setStatut(2);
        visite = visiteRepository.save(visite);
        dispatchEdit(visite);
        return visite;

    }

    public boolean isVisiteInitial(String ref, Long organisationId) throws VisiteEnCoursException {
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

    public boolean isVehiculeExist(String ref, Long organisationId) {
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
    public List<Visite> findByOrganisationId(Long id){
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
                if(visite.getStatut()==1){
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

                    emitter.send(SseEmitter.event().name("edit_visit").data(new NewListView(visite.getIdVisite(), visite.getCarteGrise().getProduit(), visite.typeRender(), visite.getCarteGrise().getNumImmatriculation(),
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
            try{
                emitter.send(SseEmitter.event().name("new_visit").data(
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

    public List<GieglanFileIcon> replaceIconIfNecessary(Visite visite){
        System.out.println("build visite +++++++++++++++"+ visite.getIdVisite());
        ProduitCategorieTest p = Utils.tests.stream()
                .filter(produitCategorieTest -> produitCategorieTest.getProduitId()== visite.getCarteGrise().getProduit().getProduitId())
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
