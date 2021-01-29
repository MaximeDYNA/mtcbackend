package com.catis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.catis.model.DetailVente;
import com.catis.repository.DetailVenteRepository;

@Service
public class DetailVenteService {

    @Autowired
    private DetailVenteRepository detailVenteRepository;

    public void addVente(DetailVente detailVente) {
        detailVenteRepository.save(detailVente);
    }


    public void addVentes(List<DetailVente> detailVentes) {
        for (DetailVente detailVente : detailVentes) {
            detailVenteRepository.save(detailVente);
        }
    }

    public List<DetailVente> findByVente(Long id) {
        return detailVenteRepository.findByVente_IdVente(id);


    }

}
