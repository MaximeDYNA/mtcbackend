package com.catis.service;

import java.io.IOException;
import java.util.*;

import com.catis.controller.SseController;
import com.catis.controller.VisiteController;
import com.catis.model.entity.Controleur;
import com.catis.model.entity.Visite;
import com.catis.repository.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.entity.Inspection;
import com.catis.repository.InspectionRepository;

@Service
public class InspectionService {

    @Autowired
    private InspectionRepository inspectionR;
    @Autowired
    private VisiteService visiteService;
    @Autowired
    private ProduitService ps;
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CategorieTestVehiculeService cat;
    @Autowired
    private GieglanFileService gieglanFileService;

    public Inspection addInspection(Inspection inspection) {
        return inspectionR.save(inspection);
    }

    public List<Inspection> findAllInspection() {
        List<Inspection> inspections = new ArrayList<>();
        inspectionR.findAll().forEach(inspections::add);
        return inspections;
    }

    public Inspection findInspectionById(UUID id) {

        return inspectionR.findById(id).get();
    }

    public Inspection findInspectionByVisite(UUID idvisite) {
        return inspectionR.findByVisite_IdVisite(idvisite);
    }

    public Inspection findLastByRef(String ref) {
        List<Inspection> inspections = inspectionR.findByVisite_CarteGrise_numImmatriculationOrVisite_CarteGrise_Vehicule_Chassis(ref, ref);
        Inspection inspection = inspections.stream().max(Comparator.comparing(Inspection::getCreatedDate))
                .orElse(null);
        return inspection;
    }

    public Inspection setSignature(UUID id, String signature, Controleur controleur) throws IOException {

        System.out.println("id visite " + id + " signature " + signature);
        Inspection inspection = findInspectionByVisite(id);
        inspection.setDateFin(new Date());
        inspection.setSignature(signature);
        if(inspection.getOrganisation().isConformity()){
            inspection.getVisite().setStatut(4);
        }
        else
            inspection.getVisite().setStatut(6);

        inspection.setControleur(controleur);

        inspection = inspectionR.save(inspection);
        Visite visite = inspection.getVisite();

        inspection.getOrganisation().getUtilisateurs().forEach(utilisateur -> {
            notificationService.dipatchVisiteToMember(utilisateur.getKeycloakId(), visite, true);
        });
        return inspection;

    }

    public void deleteInspection (UUID id){
        System.out.println(id +" to delete");
        inspectionR.deleteById(id);
    }
}