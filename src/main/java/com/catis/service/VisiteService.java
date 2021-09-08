package com.catis.service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.catis.Event.VisiteCreatedEvent;
import com.catis.model.control.Control;
import com.catis.model.entity.*;
import com.catis.objectTemporaire.KanBanSimpleData;
import com.catis.objectTemporaire.OrganisationTopDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.catis.controller.VisiteController;
import com.catis.controller.exception.VisiteEnCoursException;
import com.catis.model.control.Control.StatusType;
import com.catis.repository.ControlRepository;
import com.catis.repository.VisiteRepository;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

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
    private ApplicationEventPublisher applicationEventPublisher;


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

    public Visite approuver(Visite visite) throws IOException {
        visite.setStatut(0);
        Visite v = visiteRepository.save(visite);
        applicationEventPublisher.publishEvent(new VisiteCreatedEvent(visite));

        return v;
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
    public Visite ajouterVisite(CarteGrise cg, double montantTotal, double montantEncaisse, Long organisationId) throws VisiteEnCoursException {
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
        visite.setOrganisation(organisation);
        visite = visiteRepository.save(visite);
        applicationEventPublisher.publishEvent(new VisiteCreatedEvent(visite));
        VisiteController.dispatchEdit(visite, this, gieglanFileService, cat);
        return visite;
    }

    public Visite modifierVisite(Visite visite) throws IOException {
        Visite v = visiteRepository.save(visite);
        applicationEventPublisher.publishEvent(new VisiteCreatedEvent(visite));
        VisiteController.dispatchEdit(visite, this, gieglanFileService, cat);
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

    public Page<Visite> enCoursVisitList(Long orgId, Pageable pageable) {
        Page<Visite> visiteEnCours = visiteRepository.findByOrganisation_OrganisationIdAndEncoursTrueAndActiveStatusTrueOrderByCreatedDateDesc(orgId, pageable);

        return visiteEnCours;
    }
    public Page<Visite> endedVisitList(Long orgId, Pageable pageable){
        Page<Visite> visiteEnCours = visiteRepository.findByOrganisation_OrganisationIdAndEncoursFalseAndActiveStatusTrueOrderByCreatedDateDesc(orgId, pageable);
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
        VisiteController.dispatchEdit(visite, this, gieglanFileService, cat);
        applicationEventPublisher.publishEvent(new VisiteCreatedEvent(visite));
    }

    public List<Visite> listParStatus(int status, Long orgId) {
        return visiteRepository.findByEncoursTrueAndStatutAndOrganisation_OrganisationId(status, orgId, Sort.by(Sort.Direction.DESC, "dateDebut"));
    }

    public List<KanBanSimpleData> listParStatusForkanban(int status, Long orgId) {
        List<Visite> visites = visiteRepository.findByEncoursTrueAndStatutAndOrganisation_OrganisationId(status, orgId, Sort.by(Sort.Direction.DESC, "dateDebut"));
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
    public void commencerInspection(Visite visite) throws IOException {

        visite.setDateFin(LocalDateTime.now());
        visite.setStatut(2);
        visite = visiteRepository.save(visite);
        VisiteController.dispatchEdit(visite, this, gieglanFileService, cat);

        applicationEventPublisher.publishEvent(new VisiteCreatedEvent(visite));
    }

    public boolean isVisiteInitial(String ref, Long organisationId) throws VisiteEnCoursException {
        List<Visite> visites = findByReference(ref, organisationId);

        Visite visite = visites.stream().max(Comparator.comparing(Visite::getCreatedDate))
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


    @Async
    @TransactionalEventListener
    public void dispatchVisite(VisiteCreatedEvent event) {

        Visite visite = event.getVisite();
        System.out.println("Visite test ---------"+ visite.getIdVisite());
        VisiteController.dispatcheventoclients(visite, this, gieglanFileService, cat);
    }


}
