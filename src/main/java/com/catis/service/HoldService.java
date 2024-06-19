package com.catis.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.catis.model.entity.SessionCaisse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.catis.model.entity.Hold;
import com.catis.model.entity.Posales;
import com.catis.repository.HoldRepository;
import com.catis.repository.PosaleRepository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Service
public class HoldService {

    @Autowired
    private HoldRepository holdRepository;
    @Autowired
    private PosaleRepository posaleRepository;
    @Autowired
    private SessionCaisseService scs;

    @PersistenceContext
    private EntityManager entityManager;

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
    public void deleteHoldByNumber(Long number, UUID sessionCaisseId) throws ParseException {
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
    public void deleteHoldBySessionCaisse(UUID sessionCaisseId) {
        if (!posaleRepository.findBySessionCaisse_SessionCaisseId(sessionCaisseId).isEmpty())
            posaleRepository.deleteBySessionCaisse_SessionCaisseId(sessionCaisseId);
        if (!holdRepository.findBySessionCaisse_SessionCaisseId(sessionCaisseId).isEmpty())
            holdRepository.deleteBySessionCaisse_SessionCaisseId(sessionCaisseId);
    }

    public List<Hold> findHoldBySessionCaisse(UUID sessionCaisseId) {
        return holdRepository.findBySessionCaisse_SessionCaisseId(sessionCaisseId);
    }

    // flemming implimented
    public Hold findHoldByNumberAndSessionCaisseId(Long number, UUID sessionCaisseId) {
        String queryStr = String.format(
            "SELECT JSON_OBJECT(" +
            "'active_status', CASE h.active_status WHEN b'1' THEN 1 ELSE 0 END, " +
            "'number', h.number, " +
            "'holdId', h.id" +
            ") as json " +
            "FROM t_hold h " +
            "WHERE h.session_caisse_id='%s' AND h.number = %d",
            sessionCaisseId.toString() ,number);

        Query query = entityManager.createNativeQuery(queryStr);

        try {
            String jsonString = (String) query.getSingleResult();
            return parseJsonToHold(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Hold parseJsonToHold(String jsonString) {
        // Use your preferred JSON library to parse jsonString
        // Here is an example using Jackson:
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            Hold hold = new Hold();
            hold.setActiveStatus(jsonNode.get("active_status").asInt() == 1);
            hold.setNumber(jsonNode.get("number").asLong());
            hold.setHoldId(UUID.fromString(jsonNode.get("holdId").asText()));
            return hold;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // oldversion
    public Hold findByNumberSessionCaisse(Long number, UUID sessionCaisseId) {
        return holdRepository.findByNumberAndSessionCaisse_SessionCaisseId(number, sessionCaisseId);
    }



    public Hold findByHoldId(UUID holdId) {
        if (holdId == null) {
            return null;
        }
        return holdRepository.findById(holdId).get();
    }
    public Hold findBynumberAndSession(Long number, UUID sessionId) {
        if (number == null) {
            return null;
        }
        return holdRepository.findByNumberAndSessionCaisse_SessionCaisseId(number, sessionId);
    }

}
