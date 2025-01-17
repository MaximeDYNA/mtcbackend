package com.catis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.entity.VersionLexique;
import com.catis.repository.VersionLexiqueRepository;

@Service
public class VersionLexiqueService {

    @Autowired
    private VersionLexiqueRepository versionLexiqueRepo;

    public VersionLexique add(VersionLexique v) {
        return versionLexiqueRepo.save(v);
    }

    public VersionLexique findById(UUID id) {
        return versionLexiqueRepo.findById(id).get();
    }
    public VersionLexique findByName(String name) {
        return versionLexiqueRepo.findByLibelle(name);
    }

    public List<VersionLexique> findAll() {
        List<VersionLexique> versionLexiques = new ArrayList<>();
        versionLexiqueRepo.findAll().forEach(versionLexiques::add);
        return versionLexiques;
    }
}
