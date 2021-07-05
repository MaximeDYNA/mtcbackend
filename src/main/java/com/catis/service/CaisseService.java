package com.catis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.entity.Caisse;
import com.catis.repository.CaisseRepository;

@Service
public class CaisseService {

    @Autowired
    private CaisseRepository caisseRepository;

    public Caisse addCaisse(Caisse caisse) {
        return caisseRepository.save(caisse);
    }

    public void updateCaisse(Caisse caisse) {
        caisseRepository.save(caisse);
    }

    public List<Caisse> findAllCaisse() {
        List<Caisse> caisses = new ArrayList<>();
        caisseRepository.findByActiveStatusTrue().forEach(caisses::add);
        return caisses;
    }

    public Caisse findCaisseById(Long idCaisse) {
        return caisseRepository.findById(idCaisse).get();
    }

    public void deleteCaisseById(Long idCaisse) {
        caisseRepository.deleteById(idCaisse);
    }

}
