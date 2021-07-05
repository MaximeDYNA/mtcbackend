package com.catis.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.catis.model.entity.SessionCaisse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.catis.model.entity.Hold;
import com.catis.model.entity.Posales;
import com.catis.repository.HoldRepository;
import com.catis.repository.PosaleRepository;

@Service
public class HoldService {

    @Autowired
    private HoldRepository holdRepository;
    @Autowired
    private PosaleRepository posaleRepository;
    @Autowired
    private SessionCaisseService scs;

    public Hold addHold(Hold hold) {
        return holdRepository.save(hold);
    }

    public long maxNumber(SessionCaisse s) {
        if (holdRepository.max(s) != null)
            return holdRepository.max(s);
        else
            return 0;
    }

    @Transactional
    public void deleteHoldByNumber(Long number, Long sessionCaisseId) throws ParseException {
        for (Posales posale : posaleRepository.findByHold_NumberAndSessionCaisse_SessionCaisseId(number, sessionCaisseId)) {
            posaleRepository.delete(posale);
        }
        holdRepository.deleteByNumberAndSessionCaisse_SessionCaisseId(number, sessionCaisseId);
        if (findHoldBySessionCaisse(sessionCaisseId).isEmpty()) {
            Hold hold = new Hold();
            hold.setNumber(1L);
            hold.setSessionCaisse(scs.findSessionCaisseById(sessionCaisseId));
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            Date date = format.parse(format.format(new Date()));
            hold.setTime(date);
            addHold(hold);
        }
    }

    @Transactional
    public void deleteHoldBySessionCaisse(Long sessionCaisseId) {
        if (!posaleRepository.findBySessionCaisse_SessionCaisseId(sessionCaisseId).isEmpty())
            posaleRepository.deleteBySessionCaisse_SessionCaisseId(sessionCaisseId);
        if (!holdRepository.findBySessionCaisse_SessionCaisseId(sessionCaisseId).isEmpty())
            holdRepository.deleteBySessionCaisse_SessionCaisseId(sessionCaisseId);
    }

    public List<Hold> findHoldBySessionCaisse(Long sessionCaisseId) {
        return holdRepository.findBySessionCaisse_SessionCaisseId(sessionCaisseId);
    }

    public Hold findByNumberSessionCaisse(Long number, Long sessionCaisseId) {
        return holdRepository.findByNumberAndSessionCaisse_SessionCaisseId(number, sessionCaisseId);
    }

    public Hold findByHoldId(Long holdId) {
        if (holdId == null) {
            return null;
        }
        return holdRepository.findById(holdId).get();
    }


}
