package com.catis.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.catis.model.entity.Posales;
import com.catis.repository.PosaleRepository;

@Service
public class PosaleService {

    @Autowired
    private PosaleRepository psr;

    public void activatePosale(Long number, UUID sessionCaisseId) {
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
                     //psr.save(posales);
                 }
         );

    }

    public void deletePosale(Long number, UUID sessionCaisseId) {
        desactivateAll();
         List<Posales> posales = psr.findByHold_NumberAndSessionCaisse_SessionCaisseId(number, sessionCaisseId);
         for(Posales p : posales){
             psr.delete(p);
         }

    }

    public List<Posales> findByNumberSessionCaisse(UUID number, UUID sessionCaisseId) {
        return psr.findByHold_HoldIdAndSessionCaisse_SessionCaisseId(number, sessionCaisseId);
    }

    public List<Posales> findByReferenceSessionCaisse(String reference, UUID sessionCaisseId) {
        return psr.findByReferenceAndSessionCaisse_SessionCaisseId(reference, sessionCaisseId);
    }

    public List<Posales> findBySessionCaisse(UUID sessionCaisseId) {
        return psr.findBySessionCaisse_SessionCaisseId(sessionCaisseId);
    }

    public List<Posales> findActivePosaleBySessionId(UUID sessionId) {
        return psr.findByStatusTrueAndSessionCaisse_SessionCaisseId(sessionId);
    }

    public Posales addPosales(Posales posales) {
        return psr.save(posales);
    }

    @Transactional
    public void deletePosalesByReference(String reference, UUID sessionCaisseId) {
        psr.deleteByReferenceAndSessionCaisse_SessionCaisseId(reference, sessionCaisseId);
    }

    public boolean isDecaissementExist(UUID holdId, UUID sessionId) {
        if (psr.findByHold_HoldIdAndSessionCaisse_SessionCaisseIdAndProduit_Libelle(holdId, sessionId, "dec").isEmpty())
            return false;
        else
            return true;
    }
}
