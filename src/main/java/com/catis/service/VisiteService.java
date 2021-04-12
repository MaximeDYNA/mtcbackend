package com.catis.service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.catis.Event.VisiteCreatedEvent;
import com.catis.model.*;
import com.catis.repository.faileTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.catis.Controller.ApiResponseHandler;
import com.catis.Controller.VisiteController;
import com.catis.Controller.exception.VisiteEnCoursException;
import com.catis.model.Control.StatusType;
import com.catis.objectTemporaire.Listview;
import com.catis.repository.ControlRepository;
import com.catis.repository.VisiteRepository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;

@Service
public class VisiteService {
    @Autowired
    private VisiteRepository visiteRepository;
    @Autowired
    private OrganisationService os;
    @Autowired
    private ProduitService ps;
    @Autowired
    private CategorieTestVehiculeService cat;
    @Autowired
    private VenteService venteService;
    @Autowired
    private ControlRepository controlRepository;
    @Autowired
    private GieglanFileService gieglanFileService;
    @Autowired
    private CarteGriseService cgs;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    private static Logger log = LoggerFactory.getLogger(VisiteController.class);

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

    public Visite visiteWithLastMissedTests(Visite visite){
        List <Visite> v = visiteRepository
            .getBeforeLastVisite(visite.getControl(), visite, PageRequest.of(0,1));
        if(!v.isEmpty()) {
            return v.get(0);
        }
        return null;
    }

    /*public Flux<ServerSentEvent<ResponseEntity<Object>>> refreshVisiteAfterAdd() {

        log.info("Liste des visites en cours");
        List<Listview> listVisit = new ArrayList<>();
        for (Visite visite : enCoursVisitList()) {
            Listview lv = new Listview(visite.getIdVisite());
            lv.setCategorie(ps.findByImmatriculation(visite.getCarteGrise()
                    .getNumImmatriculation()));

            if (venteService.findByVisite(visite.getIdVisite())
                    == null)
                lv.setClient(null);
            else
                lv.setClient(venteService.findByVisite(visite.getIdVisite())
                        .getClient()
                        .getPartenaire()
                        .getNom());
            lv.setDate(visite.getDateDebut());
            lv.setReference(visite.getCarteGrise().getNumImmatriculation());
            lv.setStatut(visite.statutRender(visite.getStatut()));
            lv.setType(visite.typeRender());
            listVisit.add(lv);


        }
        //return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Affichage en mode liste des visites", listVisit);
        ResponseEntity<Object> o = ApiResponseHandler.generateResponse(HttpStatus.OK, true, "Affichage en mode liste des visites", listVisit);
        return processor
                .map(sequence -> ServerSentEvent.<ResponseEntity<Object>>builder()
                        .event("new_visit")
                        .data(o)
                        .build());

    }*/

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
        VisiteController.dispatchEdit(visite, this, gieglanFileService, cat, ps);
        return visite;
    }

    public Visite modifierVisite(Visite visite) throws IOException {
        Visite v = visiteRepository.save(visite);
        applicationEventPublisher.publishEvent(new VisiteCreatedEvent(visite));
        VisiteController.dispatchEdit(visite, this, gieglanFileService, cat, ps);
        return v;
    }

    public boolean visiteEncours(String imCha, Long organisationId) {
        return !visiteRepository.findByCarteGriseNumImmatriculationIgnoreCaseOrCarteGrise_Vehicule_ChassisIgnoreCaseAndOrganisation_OrganisationId(imCha, imCha, organisationId)
                .stream().filter(visites -> visites.getDateFin() == null).collect(Collectors.toList())
                .isEmpty();
    }

    public List<Visite> enCoursVisitList() {
        List<Visite> visiteEnCours = new ArrayList<>();
        visiteRepository.findByEncoursTrueOrderByCreatedDateDesc().forEach(visiteEnCours::add);
        return visiteEnCours;
    }

    public void terminerInspection(Long visiteId) throws IOException {
        Visite visite = new Visite();
        visite = visiteRepository.findById(visiteId).get();
        visite.setEncours(false);
        visite.setDateFin(LocalDateTime.now());
        visite.setStatut(4);
        visite = visiteRepository.save(visite);
        VisiteController.dispatchEdit(visite, this, gieglanFileService, cat, ps);

        applicationEventPublisher.publishEvent(new VisiteCreatedEvent(visite));

    }

    public List<Visite> listParStatus(int status) {
        return visiteRepository.findByEncoursTrueAndStatut(status, Sort.by(Sort.Direction.DESC, "dateDebut"));
    }

    public void commencerInspection(Long visiteId) throws IOException {
        Visite visite = new Visite();
        visite = visiteRepository.findById(visiteId).get();
        visite.setDateFin(LocalDateTime.now());
        visite.setStatut(2);
        visite = visiteRepository.save(visite);
        VisiteController.dispatchEdit(visite, this, gieglanFileService, cat, ps);

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

    @Async
    @TransactionalEventListener
    public void dispatchVisite(VisiteCreatedEvent event) {

        Visite visite = event.getVisite();
        System.out.println("Visite test ---------"+ visite.getIdVisite());
        VisiteController.dispatcheventoclients(visite, this, gieglanFileService, cat, ps);
    }
}
