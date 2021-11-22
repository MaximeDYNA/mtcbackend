package com.catis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.catis.model.entity.Utilisateur;
import com.catis.repository.UtilisateurRepository;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public Utilisateur addUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    public void updateUtilisateur(Utilisateur utilisateur) {
        utilisateurRepository.save(utilisateur);
    }

    public List<Utilisateur> findAllUtilisateur() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        utilisateurRepository.findByActiveStatusTrue().forEach(utilisateurs::add);
        return utilisateurs;
    }

    public Utilisateur findUtilisateurById(UUID idUtilisateur) {
        return utilisateurRepository.findById(idUtilisateur).get();
    }

    public Utilisateur findUtilisateurByKeycloakId(String keycloakId) {
        return utilisateurRepository.findByKeycloakId(keycloakId);
    }

    public void deleteUtilisateurById(UUID id) {
        utilisateurRepository.deleteById(id);
    }


}