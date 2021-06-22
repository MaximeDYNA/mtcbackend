package com.catis.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.DetailVente;
import com.catis.model.Vente;
import com.catis.objectTemporaire.DetailDTO;
import com.catis.objectTemporaire.OpCaisseDTO;
import com.catis.repository.VenteRepository;

@Service
public class VenteService {

    @Autowired
    private VenteRepository venteRepository;

    public Vente addVente(Vente vente) {
        return venteRepository.save(vente);
    }

    public List<Vente> findAll() {
        List<Vente> ventes = new ArrayList<>();
        venteRepository.findByActiveStatusTrue().forEach(ventes::add);
        return ventes;
    }

    public Vente findById(Long id) {
        return venteRepository.findById(id).get();
    }

    public Vente findByVisite(Long id) {
        return venteRepository.findByVisite_IdVisite(id);
    }

    public String genererNumFacture() {
        Date now = new Date();
        DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String start = "F"
                +
                formatter.format(now);

        return start;
    }

    public List<Vente> recap(Long caissierId, LocalDateTime start, LocalDateTime end) {

        List<Vente> ventes = venteRepository.findBySessionCaisseCaissierCaissierIdAndCreatedDateGreaterThanOrderByCreatedDateDesc(caissierId, start)
                .stream().filter(vente -> vente.getCreatedDate().isBefore(end)).collect(Collectors.toList());
        return ventes;

    }

    public List<OpCaisseDTO> recapOp(Long caissierId, LocalDateTime start, LocalDateTime end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<Vente> ventes = recap(caissierId, start, end);
        List<OpCaisseDTO> opCaisses = new ArrayList<>();
        List<DetailDTO> dt;
        DetailDTO d;
        OpCaisseDTO op;
        for (Vente vente : ventes) {
            op = new OpCaisseDTO();
            op.setPrixT(vente.getMontantTotal());
            op.setDate(vente.getCreatedDate().format(formatter));
            op.setClient(vente.getClient().getPartenaire().toString());
            dt = new ArrayList<>();
            for (DetailVente o : vente.getDetailventes()) {
                d = new DetailDTO();
                d.setReference(o.getReference());
                d.setPrix(o.getProduit().getPrix());
                dt.add(d);
            }
            op.setDetails(dt);
            opCaisses.add(op);
        }
        return opCaisses;
    }
}
