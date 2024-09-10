package com.catis.service;

import java.io.IOException;
import java.util.*;

import javax.persistence.EntityNotFoundException;

import com.catis.model.entity.Controleur;
import com.catis.model.entity.Visite;
import com.catis.repository.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;


import com.catis.model.entity.Inspection;
import com.catis.model.entity.Ligne;
import com.catis.repository.InspectionRepository;
import com.catis.repository.LigneRepository;

@Service
public class InspectionService {

    @Autowired
    private InspectionRepository inspectionR;
    

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private LigneRepository ligneRepository;


    public Ligne getLigneByInspectionId(UUID inspectionId) throws EntityNotFoundException {
        // Fetch the Inspection entity by its ID
        Inspection inspection = inspectionR.findById(inspectionId)
                .orElseThrow(() -> new EntityNotFoundException("Inspection not found"));

        // Return the Ligne associated with the Inspection
        return inspection.getLigne();
    }

    
    public Inspection updateInspectionLigne(UUID inspectionId, UUID ligneId) throws EntityNotFoundException {
        // Fetch the Inspection entity by its ID
        Inspection inspection = inspectionR.findById(inspectionId)
                .orElseThrow(() -> new EntityNotFoundException("Inspection not found"));

        // Fetch the Ligne entity by its ID
        Ligne ligne = ligneRepository.findById(ligneId)
                .orElseThrow(() -> new EntityNotFoundException("Ligne not found"));

        // Set the new Ligne for the Inspection
        inspection.setLigne(ligne);

        // Save and return the updated Inspection
        return inspectionR.save(inspection);
    }

    

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
    @CacheEvict(value = "VisiteCache", allEntries = true)
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
        System.out.println("saved inpection signature");
        Visite visite = inspection.getVisite();

        inspection.getOrganisation().getUtilisateurs().forEach(utilisateur -> {
            notificationService.dipatchVisiteToMember(utilisateur.getKeycloakId(), visite, true);
        });
        System.out.println("notification service is complete");
        return inspection;

    }

    public void deleteInspection (UUID id){
        System.out.println(id +" to delete");
        inspectionR.deleteById(id);
    }
}