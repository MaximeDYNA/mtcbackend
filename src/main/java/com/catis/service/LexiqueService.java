package com.catis.service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.entity.Lexique;
import com.catis.repository.LexiqueRepository;

@Service
public class LexiqueService {

    @Autowired
    private LexiqueRepository lexiqueRepository;

    public Lexique add(Lexique l) {
        return lexiqueRepository.save(l);
    }

    public Lexique findByCode(String code) {
        return lexiqueRepository.findByCode(code);
    }

    public Lexique findById(String id) {
        Optional<Lexique> lexique = lexiqueRepository.findById(id);
        if(lexique.isPresent())
            return lexique.get();
        return null;
    }

    public List<Lexique> findByVersionLexique(UUID versionLexiqueId) {
        return lexiqueRepository.findByVersionLexique_id(versionLexiqueId);
    }

    public List<Lexique> findByVersionLexiqueAndCategorie(UUID versionLexiqueId, UUID categorie) {
        return lexiqueRepository.findByVersionLexique_idAndCategorieVehicule_id(versionLexiqueId, categorie);
    }

    public Lexique editLexiqueByCode(Lexique lexique){
        Lexique lexique1 = lexiqueRepository.findByCode(lexique.getCode());
        if(lexique1 !=null){
            lexique.setId(lexique1.getId());
        }
        return lexique;
    }

    public List<Lexique> findAllActive() {
        return lexiqueRepository.findByActiveStatusTrue();
    }
}
