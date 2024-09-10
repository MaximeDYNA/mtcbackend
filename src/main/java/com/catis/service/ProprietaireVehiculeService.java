package com.catis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.catis.dtoprojections.ProprietaireVehiculeDTO;
import com.catis.model.entity.Client;
import com.catis.model.entity.Contact;
import com.catis.model.entity.Partenaire;
import com.catis.model.entity.ProprietaireVehicule;
import com.catis.repository.ProprietaireVehiculeRepository;

@Service
public class ProprietaireVehiculeService {

    @Autowired
    private ProprietaireVehiculeRepository pvr;
    @Autowired
    private PartenaireService partenaireService;





    @Transactional
    public ProprietaireVehicule updatePartenaireDetails(UUID proprietaireVehiculeId, String nom, String prenom) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            System.out.println("user: " + auth.getName());
            System.out.println("Roles: " + auth.getAuthorities());
        }
        // Fetch the ProprietaireVehicule by its ID
        ProprietaireVehicule proprietaireVehicule = pvr.findById(proprietaireVehiculeId)
                .orElseThrow(() -> new EntityNotFoundException("ProprietaireVehicule not found with id " + proprietaireVehiculeId));

        // Fetch the associated Partenaire
        Partenaire partenaire = proprietaireVehicule.getPartenaire();

        if (partenaire == null) {
            throw new EntityNotFoundException("Partenaire not found for ProprietaireVehicule with id " + proprietaireVehiculeId);
        }

        // Update the Partenaire's details
        partenaire.setNom(nom);
        partenaire.setPrenom(prenom);
        // Save the updated Partenaire and ProprietaireVehicule
        partenaireService.updatePartenaire(partenaire);
        return proprietaireVehicule;
    }


    public Page<ProprietaireVehicule> searchProprio(String nom, Pageable pageable) {
        return pvr.findByActiveStatusTrueAndPartenaire_NomStartsWithIgnoreCaseOrPartenaire_PrenomStartsWithIgnoreCase(nom,nom, pageable);
    }


    public List<ProprietaireVehicule> findAll() {
        List<ProprietaireVehicule> proprietaires = new ArrayList<>();
        pvr.findByActiveStatusTrue().forEach(proprietaires::add);
        return proprietaires;
    }


    public List<ProprietaireVehicule> findAllPage(Pageable pageable) {
        List<ProprietaireVehicule> proprietaires = new ArrayList<>();
        pvr.findByActiveStatusTrue(pageable).forEach(proprietaires::add);
        return proprietaires;
    }
    // flemming implimented
    public List<ProprietaireVehiculeDTO> findSearchPage(String keyword, Pageable pageable) {
        return pvr.findByActiveStatusTrueAndPartenaire_NomStartsWithIgnoreCase(keyword, pageable);
    }

    public List<ProprietaireVehicule> searchProprio(String nom) {
        List<ProprietaireVehicule> proprietaires = new ArrayList<>();
        pvr.findByActiveStatusTrueAndPartenaire_NomStartsWithIgnoreCase(nom).forEach(proprietaires::add);
        return proprietaires;
    }

    public ProprietaireVehicule addProprietaire(ProprietaireVehicule p) {
        return pvr.save(p);
    }

    public ProprietaireVehicule findById(UUID id) {
        return pvr.findById(id).get();
    }

    public void deleteById(UUID id){
        pvr.deleteById(id);
    }

    public ProprietaireVehicule addClientToProprietaire(Client client) {
        Partenaire p = partenaireService.findPartenaireById(client.getPartenaire().getPartenaireId());
        if (pvr.findByPartenaire_PartenaireId(p.getPartenaireId()).isEmpty()) {
            ProprietaireVehicule pro = new ProprietaireVehicule();
            pro.setPartenaire(p);
            return pvr.save(pro);
        }
        return pvr.findByPartenaire_PartenaireId(p.getPartenaireId()).get(0);

    }

    public ProprietaireVehicule addContactToProprietaire(Contact contact) {
        Partenaire p = partenaireService.findPartenaireById(contact.getPartenaire().getPartenaireId());
        if (pvr.findByPartenaire_PartenaireId(p.getPartenaireId()).isEmpty()) {
            ProprietaireVehicule pro = new ProprietaireVehicule();
            pro.setPartenaire(p);
            return pvr.save(pro);
        }
        return pvr.findByPartenaire_PartenaireId(p.getPartenaireId()).get(0);

    }
}
