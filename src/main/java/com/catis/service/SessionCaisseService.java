package com.catis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.catis.model.entity.SessionCaisse;
import com.catis.repository.SessionCaisseRepository;

@Service
public class SessionCaisseService {

    @Autowired
    private SessionCaisseRepository sessionCaisseRepository;

    public SessionCaisse enregistrerSessionCaisse(SessionCaisse sessionCaisse) {

            return sessionCaisseRepository.save(sessionCaisse);

    }

    public SessionCaisse findSessionCaisseById(long idSessionCaisse) {
        return sessionCaisseRepository.findBySessionCaisseId(idSessionCaisse);
    }

    public SessionCaisse findSessionCaisseByKeycloakId(String keycloakId) {
        SessionCaisse sessionCaisse = sessionCaisseRepository.findByActiveTrueAndCaissier_User_KeycloakId(keycloakId);

        return sessionCaisse;
    }

    public SessionCaisse findSessionCaisseByUserId(long userId) {
        return sessionCaisseRepository.findByActiveTrueAndCaissier_User_UtilisateurId(userId);
    }

    public SessionCaisse findActiveSessionCaissierById(long caissierId) {
        return sessionCaisseRepository.findByActiveTrueAndCaissierCaissierId(caissierId);
    }

    @Transactional
    public SessionCaisse fermerSessionCaisse(Long sessionCaisseId, double montantFermeture) {
        SessionCaisse sessionCaisse = sessionCaisseRepository.findBySessionCaisseId(sessionCaisseId);

        sessionCaisse.setMontantfermeture(montantFermeture);
        sessionCaisse.setActive(false);
        sessionCaisse.setDateHeureFermeture(new Date());
        sessionCaisse = sessionCaisseRepository.save(sessionCaisse);


        return sessionCaisse;
    }

    public List<SessionCaisse> getAll(){
        List<SessionCaisse> sessionCaisses = new ArrayList<>();

        sessionCaisseRepository.findByActiveStatusTrue().forEach(sessionCaisses::add);

        return sessionCaisses;
    }

    public List<SessionCaisse> getAll(Pageable pageable){
        List<SessionCaisse> sessionCaisses = new ArrayList<>();

        sessionCaisseRepository.findByActiveStatusTrue(pageable).forEach(sessionCaisses::add);

        return sessionCaisses;
    }


}
