package com.catis.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.entity.OperationCaisse;
import com.catis.repository.OperationDeCaisseRepository;

@Service
public class OperationCaisseService {

    @Autowired
    private OperationDeCaisseRepository operationCaisseRepository;

    public OperationCaisse addOperationCaisse(OperationCaisse op) {
        return operationCaisseRepository.save(op);
    }

    public List<OperationCaisse> encaissementList(int type) {

        return operationCaisseRepository.findByType(type);
    }

    public String genererTicket() {
        Date now = new Date();
        DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String start = "T"
                +
                formatter.format(now);

        return start;
    }

    public List<OperationCaisse> findBySession(UUID sessionCaisseId) {
        return operationCaisseRepository.findBySessionCaisse_SessionCaisseId(sessionCaisseId);
    }

    public int randomNumber() {
        Random rand = new Random();
        return rand.nextInt(1000);
    }

    public String type(boolean b) {
        if (b) {
            return "Encaissement";
        } else
            return "Décaissement";
    }

    public double montantTotalEncaisse(UUID idVente) {
        double montant = 0;
        for (OperationCaisse o : operationCaisseRepository.findByTypeTrueAndVente_IdVente(idVente)) {
            montant += o.getMontant();
        }
        return montant;
    }
}
