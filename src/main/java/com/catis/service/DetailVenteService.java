package com.catis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.catis.model.entity.DetailVente;
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
    public List<DetailVente> findByRefVente(String ref) {
        return detailVenteRepository.findByVente_NumFactureAndActiveStatusTrue(ref);

    }
    public List<DetailVente> findAll(Pageable pageable) {
        return detailVenteRepository.findByVente_NumFactureAndActiveStatusTrue(pageable);

    }

}
