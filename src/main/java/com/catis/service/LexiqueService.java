package com.catis.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.Lexique;
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

    public List<Lexique> findByVersionLexique(Long versionLexiqueId) {
        return lexiqueRepository.findByVersionLexique_id(versionLexiqueId);
    }

    public List<Lexique> findAllActive() {
        return lexiqueRepository.findByActiveStatusTrue();
    }
}
