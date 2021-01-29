package com.catis.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.OperationCaisse;
import com.catis.repository.OperationDeCaisseRepository;

@Service
public class OperationCaisseService {

    @Autowired
    private OperationDeCaisseRepository operationCaisseRepository;

    public void addOperationCaisse(OperationCaisse op) {
        operationCaisseRepository.save(op);
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

    public List<OperationCaisse> findBySession(Long sessionCaisseId) {
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

    public double montantTotalEncaisse(Long idVente) {
        double montant = 0;
        for (OperationCaisse o : operationCaisseRepository.findByTypeTrueAndVente_IdVente(idVente)) {
            montant += o.getMontant();
        }
        return montant;
    }
}
