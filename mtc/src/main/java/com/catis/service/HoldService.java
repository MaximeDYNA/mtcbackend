package com.catis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.catis.model.Hold;
import com.catis.model.Posales;
import com.catis.repository.HoldRepository;
import com.catis.repository.PosaleRepository;

@Service
public class HoldService {

	@Autowired
	private HoldRepository holdRepository;
	@Autowired
	private PosaleRepository posaleRepository;
	
	public Hold addHold(Hold hold) {
		return holdRepository.save(hold);
	}
	
	public long maxNumber() {
		if(holdRepository.max() != null)
			return holdRepository.max();
		else
			return 0;
	}
	@Transactional
	public void deleteHoldByNumber(Long number, Long sessionCaisseId) {
		for(Posales posale : posaleRepository.findByHold_NumberAndSessionCaisse_SessionCaisseId(number, sessionCaisseId)) {
			posaleRepository.delete(posale);
		}
		holdRepository.deleteByNumberAndSessionCaisse_SessionCaisseId(number, sessionCaisseId);
	}
	@Transactional
	public void deleteHoldBySessionCaisse(Long sessionCaisseId) {
		 posaleRepository.deleteBySessionCaisse_SessionCaisseId(sessionCaisseId);
		holdRepository.deleteBySessionCaisse_SessionCaisseId(sessionCaisseId);
	}
	
	public List<Hold> findHoldBySessionCaisse(Long sessionCaisseId){
		return holdRepository.findBySessionCaisse_SessionCaisseId(sessionCaisseId);
	}
	
	public Hold findByNumberSessionCaisse(Long number, Long sessionCaisseId) {
		return holdRepository.findByNumberAndSessionCaisse_SessionCaisseId(number, sessionCaisseId);
	}
	
	public Hold findByHoldId(Long holdId) {
		return holdRepository.findById(holdId).get();
	}
	
	
	
}
