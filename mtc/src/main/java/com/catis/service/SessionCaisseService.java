package com.catis.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.SessionCaisse;
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
	public List<SessionCaisse> findActiveSessionCaisseByUser(long userId) {
		return sessionCaisseRepository.findByActiveTrueAndUser_UtilisateurId(userId);
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
	public void inactiveSession(Long userId) {
		for(SessionCaisse sessionCaisse : sessionCaisseRepository.findByUser_UtilisateurId(userId)) {
			Date date = new Date();
			sessionCaisse.setActive(false);
			sessionCaisse.setDateHeureFermeture(date);
			sessionCaisseRepository.save(sessionCaisse);
		}
	}
	
}
