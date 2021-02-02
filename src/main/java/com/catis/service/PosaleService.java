package com.catis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.catis.model.Posales;
import com.catis.repository.PosaleRepository;

@Service
public class PosaleService {

    @Autowired
    private PosaleRepository psr;

    public void activatePosale(Long number, Long sessionCaisseId) {
        desactivateAll();
        for (Posales posales : psr.findByHold_NumberAndSessionCaisse_SessionCaisseId(number, sessionCaisseId)) {
            posales.setStatus(true);
            psr.save(posales);
        }
    }

    public void desactivateAll() {
         psr.findAll().forEach(
                 posales -> {
                     posales.setStatus(false);
                     psr.save(posales);
                 }
         );

    }

    public void deletePosale(Long number, Long sessionCaisseId) {
        desactivateAll();
        for (Posales posales : psr.findByHold_NumberAndSessionCaisse_SessionCaisseId(number, sessionCaisseId)) {
            psr.delete(posales);
        }
    }

    public List<Posales> findByNumberSessionCaisse(Long number, Long sessionCaisseId) {
        return psr.findByHold_NumberAndSessionCaisse_SessionCaisseId(number, sessionCaisseId);
    }

    public List<Posales> findByReferenceSessionCaisse(String reference, Long sessionCaisseId) {
        return psr.findByReferenceAndSessionCaisse_SessionCaisseId(reference, sessionCaisseId);
    }

    public List<Posales> findBySessionCaisse(Long sessionCaisseId) {
        return psr.findBySessionCaisse_SessionCaisseId(sessionCaisseId);
    }

    public List<Posales> findActivePosale() {
        return psr.findByStatusTrue();
    }

    public Posales addPosales(Posales posales) {
        return psr.save(posales);

    }

    @Transactional
    public void deletePosalesByReference(String reference, Long sessionCaisseId) {
        psr.deleteByReferenceAndSessionCaisse_SessionCaisseId(reference, sessionCaisseId);
    }

    public boolean isDecaissementExist(Long holdId, Long sessionId) {
        if (psr.findByHold_HoldIdAndSessionCaisse_SessionCaisseIdAndProduit_ProduitId(holdId, sessionId, 1L).isEmpty())
            return false;
        else
            return true;
    }
}
