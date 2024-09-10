package com.catis.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.catis.model.entity.DetailVente;
import com.catis.model.entity.TaxeProduit;
import com.catis.model.entity.Vente;
import com.catis.objectTemporaire.DetailDTO;
import com.catis.objectTemporaire.OpCaisseDTO;
import com.catis.objectTemporaire.ProduitTicketdto;
import com.catis.objectTemporaire.TaxeTicketdto;
import com.catis.objectTemporaire.Ticketdto;
import com.catis.repository.VenteRepository;

import pl.allegro.finance.tradukisto.ValueConverters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
@CacheConfig(cacheNames={"ticketCache"})
public class VenteService {

    @Autowired
    private VenteRepository venteRepository;
    Logger logger = LoggerFactory.getLogger(VenteService.class);

    @CacheEvict(allEntries = true)
    public Vente addVente(Vente vente) {
        logger.info("SAVING VENTE");
        return venteRepository.save(vente);
    }

    public List<Vente> findAll(Pageable pageable) {
        List<Vente> ventes = new ArrayList<>();
        venteRepository.findByActiveStatusTrue(pageable).forEach(ventes::add);
        return ventes;
    }
    
    public Vente findById(UUID id) {
        return venteRepository.findById(id).get();
    }
    public Vente findByVisite(UUID id) {
        return venteRepository.findByVisite_IdVisite(id);
    }

    public List<Vente> findByRef(String ref, Pageable pageable){
        return venteRepository.findByRef(ref, pageable);
    }


@Transactional
@Cacheable(key = "'ticket-' + #organisationId.toString() + '-' + #pageable.toString()")
public Page<Ticketdto> findByRefMain(String title, UUID organisationId, Pageable pageable, String lang) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    Page<Vente> ventePage = venteRepository.findByRefAndOrganisationId(title, organisationId, pageable);

    List<Ticketdto> ticketdtos = ventePage.getContent()
            .stream()
            .map(vente -> new Ticketdto(
                    vente.getNumFacture(),
                    vente.getClient() == null ? vente.getContact().getPartenaire().getNom() : vente.getClient().getPartenaire().getNom(),
                    vente.getClient() == null ? vente.getContact().getPartenaire().getTelephone() : vente.getClient().getPartenaire().getTelephone(),
                    vente.getCreatedDate().format(formatter),
                    vente.getDetailventes().stream()
                            .map(detailVente1 -> new ProduitTicketdto(
                                    detailVente1.getReference(),
                                    detailVente1.getProduit().getLibelle(),
                                    detailVente1.getPrix(),
                                    getPrix(detailVente1.getPrix(), detailVente1.getProduit().getTaxeProduit()),
                                    detailVente1.getProduit().getDescription(),
                                    detailVente1.getProduit().getTaxeProduit().stream()
                                            .map(taxeProduit -> new TaxeTicketdto(taxeProduit.getTaxe().getNom(), taxeProduit.getTaxe().getValeur()))
                                            .collect(Collectors.toList())
                            ))
                            .collect(Collectors.toList()),
                    vente.getMontantTotal(),
                    convert(lang, vente.getMontantTotal()),
                    vente.getMontantHT()
            ))
            .collect(Collectors.toList());

    // The total number of elements is fetched from ventePage.getTotalElements()
    return new PageImpl<>(ticketdtos, pageable, ventePage.getTotalElements());
}

@Transactional
public Page<Ticketdto> findByRefMainNoCache(String title, UUID organisationId, Pageable pageable, String lang) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    Page<Vente> ventePage = venteRepository.findByRefAndOrganisationId(title, organisationId, pageable);

    List<Ticketdto> ticketdtos = ventePage.getContent()
            .stream()
            .map(vente -> new Ticketdto(
                    vente.getNumFacture(),
                    vente.getClient() == null ? vente.getContact().getPartenaire().getNom() : vente.getClient().getPartenaire().getNom(),
                    vente.getClient() == null ? vente.getContact().getPartenaire().getTelephone() : vente.getClient().getPartenaire().getTelephone(),
                    vente.getCreatedDate().format(formatter),
                    vente.getDetailventes().stream()
                            .map(detailVente1 -> new ProduitTicketdto(
                                    detailVente1.getReference(),
                                    detailVente1.getProduit().getLibelle(),
                                    detailVente1.getPrix(),
                                    getPrix(detailVente1.getPrix(), detailVente1.getProduit().getTaxeProduit()),
                                    detailVente1.getProduit().getDescription(),
                                    detailVente1.getProduit().getTaxeProduit().stream()
                                            .map(taxeProduit -> new TaxeTicketdto(taxeProduit.getTaxe().getNom(), taxeProduit.getTaxe().getValeur()))
                                            .collect(Collectors.toList())
                            ))
                            .collect(Collectors.toList()),
                    vente.getMontantTotal(),
                    convert(lang, vente.getMontantTotal()),
                    vente.getMontantHT()
            ))
            .collect(Collectors.toList());

    // The total number of elements is fetched from ventePage.getTotalElements()
    return new PageImpl<>(ticketdtos, pageable, ventePage.getTotalElements());
}

    
    @Transactional
    @Cacheable(key = "'ticket-' + #pageable.toString()")
    public Page<Ticketdto> findByRefMain(String title, Pageable pageable, String lang) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<Ticketdto> ticketdtos = venteRepository.findByRef(title, pageable)
                .stream()
                .map(vente -> new Ticketdto(
                        vente.getNumFacture(),
                        vente.getClient() == null ? vente.getContact().getPartenaire().getNom() : vente.getClient().getPartenaire().getNom(),
                        vente.getClient() == null ? vente.getContact().getPartenaire().getTelephone() : vente.getClient().getPartenaire().getTelephone(),
                        vente.getCreatedDate().format(formatter),
                        vente.getDetailventes().stream()
                                .map(detailVente1 -> new ProduitTicketdto(
                                        detailVente1.getReference(),
                                        detailVente1.getProduit().getLibelle(),
                                        detailVente1.getPrix(),
                                        getPrix(detailVente1.getPrix(), detailVente1.getProduit().getTaxeProduit()),
                                        detailVente1.getProduit().getDescription(),
                                        detailVente1.getProduit().getTaxeProduit().stream()
                                                .map(taxeProduit -> new TaxeTicketdto(taxeProduit.getTaxe().getNom(), taxeProduit.getTaxe().getValeur()))
                                                .collect(Collectors.toList())
                                ))
                                .collect(Collectors.toList()),
                        vente.getMontantTotal(),
                        convert(lang, vente.getMontantTotal()),
                        vente.getMontantHT()
                ))
                .collect(Collectors.toList());

        return new PageImpl<>(ticketdtos, pageable, 300);
    }


    
     public double getPrix(double prixTTC, Set<TaxeProduit> taxeProduits){

        double sum = taxeProduits.stream()
                .map(taxeTicketdto -> new Double(taxeTicketdto.getTaxe().getValeur()))
                .reduce((a,b) -> a+b)
                .get();
        double prix = prixTTC * 100/(100+ sum);
        return prix;
    }
    public String convert(String lang, double price){
        ValueConverters converter;
        if (lang.equalsIgnoreCase("fr")) {
            converter = ValueConverters.FRENCH_INTEGER;
        } else
            converter = ValueConverters.ENGLISH_INTEGER;

        return converter.asWords((int) price);
    }

    // @Async("taskExecutorForHeavyTasks")
    // public CompletableFuture<List<Vente>> findByRefAsync(String ref, Pageable pageable) {
    //     logger.info("get list of ticket by " + Thread.currentThread().getName());
    //     return CompletableFuture.supplyAsync(() -> venteRepository.findByRef(ref, pageable));
    // }
    
    public String genererNumFacture() {
        Date now = new Date();
        DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String start = "F"
                +
                formatter.format(now);

        return start;
    }
    public List<Vente> recap(UUID caissierId, LocalDateTime start, LocalDateTime end) {

        List<Vente> ventes = venteRepository.findBySessionCaisseCaissierCaissierIdAndCreatedDateGreaterThanOrderByCreatedDateDesc(caissierId, start)
                .stream().filter(vente -> vente.getCreatedDate().isBefore(end)).collect(Collectors.toList());
        return ventes;

    }

    public List<OpCaisseDTO> recapOp(UUID caissierId, LocalDateTime start, LocalDateTime end) {
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
    public double getTodayCA(){
        double ca = 0;
        List<Vente> ventes = venteRepository.venteOftheDay();
        for(Vente v : ventes){
            ca += v.getMontantTotal();
        }
        return ca;
    }

    public double TaxeOfTheDay(){
        double tax = 0;
        List<Vente> ventes = venteRepository.venteOftheDay();
        for(Vente v : ventes){
            tax += v.getMontantTotal() - v.getMontantHT();
        }
        return tax;
    }

    public List<Double> caGraphWeek(){
        List<Double> caWeek=new ArrayList<>();
        List<Vente> ventes;
        for(int i=0;i<7;i++){
            if(i==0)
                ventes = venteRepository.ventebyDate(LocalDateTime.now().toLocalDate().atStartOfDay().minusDays(i),LocalDateTime.now());
            else
                ventes = venteRepository.ventebyDate(LocalDateTime.now().toLocalDate().atStartOfDay().minusDays(i),LocalDateTime.now().toLocalDate().atStartOfDay().minusDays(i-1));

            caWeek.add(0,getCA(ventes));

        }
        return caWeek;
    }

    public double getCA(List<Vente> ventes){
        double ca = 0;
        for(Vente v : ventes){
            ca += v.getMontantTotal();
        }
        return ca;
    }
}
