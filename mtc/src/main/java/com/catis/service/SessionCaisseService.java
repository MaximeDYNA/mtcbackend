package com.catis.service;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.SessionCaisse;
import com.catis.repository.SessionCaisseRepository;

@Service
public class SessionCaisseService {

	@Autowired
	private SessionCaisseRepository sessionCaisseRepository;
	
	public void enregistrerSessionCaisse(SessionCaisse sessionCaisse) {
		sessionCaisseRepository.save(sessionCaisse);
	}
	public SessionCaisse findSessionCaisseById(long idSessionCaisse) {
		return sessionCaisseRepository.findBySessionCaisseId(idSessionCaisse);
	}
	@Transactional
	public SessionCaisse fermerSessionCaisse(Long sessionCaisseId) {
		SessionCaisse sessionCaisse = sessionCaisseRepository.findBySessionCaisseId(sessionCaisseId);
		sessionCaisse.setActive(false);
		sessionCaisse.setDateHeureFermeture(new Date());
		sessionCaisse = sessionCaisseRepository.save(sessionCaisse);
		return sessionCaisse;
	}
}
