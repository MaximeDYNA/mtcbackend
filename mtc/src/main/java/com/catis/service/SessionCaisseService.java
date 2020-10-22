package com.catis.service;

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
}
