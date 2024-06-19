package com.catis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.catis.model.entity.Partenaire;
import com.catis.model.mapper.PartenaireMapper;
import com.catis.objectTemporaire.PartenaireSearch;
import com.catis.repository.PartenaireRepository;

@Service
public class PartenaireService {

    @Autowired
    private PartenaireSearchService partenaireElasticService;

    @Autowired
    private PartenaireMapper partenaireMapper;
    

    @Autowired
    private PartenaireRepository partenaireRepository;

    public Partenaire addPartenaire(Partenaire partenaire) {
        return partenaireRepository.save(partenaire);
    }


    public void updatePartenaire(Partenaire partenaire) {
        partenaireRepository.save(partenaire);
    }

    public List<Partenaire> findAllPartenaire() {
        List<Partenaire> partenaires = new ArrayList<>();
        partenaireRepository.findAll().forEach(partenaires::add);
        return partenaires;
    }

    public Partenaire findPartenaireById(UUID idPartenaire) {
        return partenaireRepository.findById(idPartenaire).get();
    }

    public List<Partenaire> findPartenaireByNom(String nom) {
        System.out.println("Searching in elastic search index partenaire_index");
        List<PartenaireSearch> searchResults = partenaireElasticService.findPartenaireByNom(nom);
        //     // if (searchResults.isEmpty()) {
        //     //     System.out.println("Elastic search did not return data, falling back to mysql search");
        //     //     return partenaireRepository
        //     //     .findByNomStartsWithIgnoreCaseOrPrenomStartsWithIgnoreCaseOrPassportStartsWithIgnoreCaseOrTelephoneStartsWithIgnoreCase(nom, nom, nom, nom);
        //     // }
        System.out.println("Search results returned" );
        return searchResults.stream()
                .map(partenaireMapper::fromPartenaireSearch)
                .collect(Collectors.toList());
    }
    @Async("taskExecutorForHeavyTasks")
    public CompletableFuture<List<Partenaire>> findPartenaireByNomAsync(String nom) {
        System.out.println("Searching in elastic search index partenaire_index");
        List<PartenaireSearch> searchResults = partenaireElasticService.findPartenaireByNom(nom);
        System.out.println("Search results returned" );
        List<Partenaire> partenaires = searchResults.stream()
                .map(partenaireMapper::fromPartenaireSearch)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(partenaires);
    }

    public void deletePartenaireById(UUID idPartenaire) {
        partenaireRepository.deleteById(idPartenaire);
    }

    public boolean partenaireAlreadyExist(Partenaire partenaire) {

        return
                findPartenaireByNom(partenaire.getNom()).stream()
                        .filter(part -> part.getPrenom().equals(partenaire.getPrenom()))
                        .filter(part -> part.getCni().equals(partenaire.getCni()))
                        .collect(Collectors.toList())
                        .isEmpty();
    }

    public boolean isNomAlreadyExist(String nom) {
        return !partenaireRepository.findByNomIgnoreCase(nom).isEmpty();
    }

    public boolean isCniAlreadyExist(String cni) {
        return !partenaireRepository.findByCniIgnoreCase(cni).isEmpty();
    }

    public boolean isPassportAlreadyExist(String passport) {
        return !partenaireRepository.findByPassportIgnoreCase(passport).isEmpty();
    }

    public boolean isEmailAlreadyExist(String email) {
        return !partenaireRepository.findByEmail(email).isEmpty();
    }

    public boolean isNomEtPrenomAlreadyExist(String nom, String prenom) {
        return !partenaireRepository.findByNomIgnoreCaseAndPrenomIgnoreCase(nom, prenom).isEmpty();
    }

    public void isPartenaireAlreadyExist(Partenaire partenaire) {

        ;
    }
}
