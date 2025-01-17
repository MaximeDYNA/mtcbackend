package com.catis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.entity.Adresse;
import com.catis.repository.AdresseRepository;

@Service
public class AdresseService {

    @Autowired
    private AdresseRepository adresseRepository;

    public List<Adresse> findAll() {
        List<Adresse> adresses = new ArrayList<>();
        adresseRepository.findAll().forEach(adresses::add);
        return adresses;

    }

    public Adresse addAdresse(Adresse adresse) {
        return adresseRepository.save(adresse);
    }

    public Adresse updateAdresse(Adresse adresse) {
        return adresseRepository.save(adresse);
    }

    public void deleteAdresse(UUID idAdresse) {
        Adresse adresse = adresseRepository.findById(idAdresse).get();
        adresseRepository.deleteById(idAdresse);

    }
}
