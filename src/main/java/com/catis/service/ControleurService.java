package com.catis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.entity.Controleur;
import com.catis.repository.ControleurRepository;

@Service
public class ControleurService {

    @Autowired
    private ControleurRepository controleurR;

    public Controleur addControleur(Controleur controleur) {
        return controleurR.save(controleur);
    }

    public List<Controleur> findAllControleur() {
        List<Controleur> controleurs = new ArrayList<>();
        controleurR.findByActiveStatusTrue().forEach(controleurs::add);
        return controleurs;
    }

    public List<Controleur> findAllByOrganisation(Long organisationId) {
        List<Controleur> controleurs = new ArrayList<>();
        controleurR.findByOrganisation_OrganisationId(organisationId).forEach(controleurs::add);
        return controleurs;
    }

    public Controleur findControleurById(Long id) {
        return controleurR.findById(id).get();
    }

    public Controleur findControleurBykeycloakId(String keycloakId) {
        return controleurR.findByUtilisateur_keycloakId(keycloakId);
    }

    public void delete(Long id) {
        controleurR.deleteById(id);
    }
}
