package com.catis.service;

import com.catis.controller.SseController;
import com.catis.model.entity.Visite;
import com.catis.objectTemporaire.*;
import com.catis.repository.EmitterRepository;
import com.catis.repository.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
@AllArgsConstructor
@Slf4j
public class SseNotificationService implements NotificationService {

    private final EmitterRepository emitterRepository;
    private final EventMapper eventMapper;
    @Autowired
    private GieglanFileService gieglanFileService;

    @Override
    public void sendNotification(String memberId, EventDto event) {
        if (event == null) {
            log.debug("No server event to send to device.");
            return;
        }
        doSendNotification(memberId, event);
    }


    private void doSendNotification(String memberId, EventDto event) {

        if(emitterRepository.get(memberId).isPresent()){
            try {
                log.debug("Sending event: {} for member: {}", event, memberId);
                emitterRepository.get(memberId).get().send(eventMapper.toSseEventBuilder(event));
            } catch (IOException | IllegalStateException e) {
                log.debug("Error while sending event: {} for member: {} - exception: {}", event, memberId, e);
                emitterRepository.remove(memberId);
            }
        }
        else{
            log.debug("No emitter for member {}", memberId);
        }

    }

    public void dipatchVisiteToMember(String memberId, Visite visite, boolean edited) {
        if(edited) {
            if (emitterRepository.get(memberId).isPresent()) {
                try {
                    log.info("Sending visite: {} for member: {}", visite.getIdVisite(), memberId);
                    if (visite.getStatut() == 1) {
                        emitterRepository.get(memberId).get().send(SseEmitter.event().name("new_visit").data(
                                new NewListView(visite.getIdVisite(), visite.getCarteGrise().getProduit(), visite.typeRender(), visite.getCarteGrise().getNumImmatriculation(),
                                        (visite.getCarteGrise().getVehicule() == null
                                                ? "" : (visite.getCarteGrise().getVehicule().getChassis() == null
                                                ? "" : visite.getCarteGrise().getVehicule().getChassis())),
                                        (visite.getCarteGrise().getProprietaireVehicule()
                                                .getPartenaire()
                                                .getNom()
                                                == null
                                                ? null : visite.getCarteGrise().getProprietaireVehicule()
                                                .getPartenaire()
                                                .getNom()),
                                        Utils.parseDate(visite.getCreatedDate()), visite.getCreatedDate(),
                                        getHTML(visite), visite.getStatut(), visite.getIdVisite(), visite.isContreVisite(),
                                        visite.getInspection() == null ? null : visite.getInspection().getIdInspection(), visite.getCarteGrise(), visite.getOrganisation().isConformity(),
                                        visite.getIsConform(),
                                        visite.getOrganisation().getNom(), visite.getInspection() == null ? null : visite.getInspection().getBestPlate(),
                                        visite.getInspection() == null ? 0 : visite.getInspection().getDistancePercentage(),
                                        visite.getCreatedDate().format(SseController.dateTimeFormatter), true, visite.getDocument())));
                        emitterRepository.get(memberId).get().send(SseEmitter.event().name("controleur_visit").data(visite));
                    } else {
                        emitterRepository.get(memberId).get().send(SseEmitter.event().name("edit_visit").data(new NewListView(visite.getIdVisite(), visite.getCarteGrise().getProduit(), visite.typeRender(), visite.getCarteGrise().getNumImmatriculation(),
                                (visite.getCarteGrise().getVehicule() == null
                                        ? "" : (visite.getCarteGrise().getVehicule().getChassis() == null
                                        ? "" : visite.getCarteGrise().getVehicule().getChassis())),
                                (visite.getVente().getClient() == null
                                        ? visite.getVente().getContact().getPartenaire().getNom() : visite.getVente().getClient().getPartenaire().getNom()),
                                Utils.parseDate(visite.getCreatedDate()), visite.getCreatedDate(),
                                getHTML(visite), visite.getStatut(), visite.getIdVisite(), visite.isContreVisite(),
                                visite.getInspection() == null ? null : visite.getInspection().getIdInspection(), visite.getCarteGrise(), visite.getOrganisation().isConformity(),
                                visite.getIsConform(),
                                visite.getOrganisation().getNom(), visite.getInspection() == null ? null : visite.getInspection().getBestPlate(),
                                visite.getInspection() == null ? 0 : visite.getInspection().getDistancePercentage(),
                                visite.getCreatedDate().format(SseController.dateTimeFormatter), false, visite.getDocument())));
                    }
                } catch (IOException | IllegalStateException e) {
                    log.info("Error while sending visite to member for member: {} - exception: {}", memberId, e);
                    emitterRepository.remove(memberId);
                }
            } else {
                log.info("No emitter for member {}", memberId);
            }
        }else{
            if (emitterRepository.get(memberId).isPresent()) {
                try {
                    log.info("Sending visite: {} for member: {}", visite.getIdVisite(), memberId);
                        emitterRepository.get(memberId).get().send(SseEmitter.event().name("new_visit").data(
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

                } catch (IOException | IllegalStateException e) {
                    log.info("Error while sending visite to member for member: {} - exception: {}", memberId, e);
                    emitterRepository.remove(memberId);
                }
            } else {
                log.info("No emitter for member {}", memberId);
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
