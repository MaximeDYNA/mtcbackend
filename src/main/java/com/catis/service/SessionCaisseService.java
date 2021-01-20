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
	
	
}
