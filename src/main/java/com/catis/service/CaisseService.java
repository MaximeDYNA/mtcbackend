package com.catis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.Caisse;
import com.catis.repository.CaisseRepository;

@Service
public class CaisseService {

    @Autowired
    private CaisseRepository caisseRepository;

    public void addCaisse(Caisse caisse) {
        caisseRepository.save(caisse);
    }

    public void updateCaisse(Caisse caisse) {
        caisseRepository.save(caisse);
    }

    public List<Caisse> findAllCaisse() {
        List<Caisse> caisses = new ArrayList<>();
        caisseRepository.findAll().forEach(caisses::add);
        return caisses;
    }

    public Caisse findCaisseById(String idCaisse) {
        return caisseRepository.findById(idCaisse).get();
    }

    public void deleteCaisseById(String idCaisse) {
        caisseRepository.deleteById(idCaisse);
    }

}
