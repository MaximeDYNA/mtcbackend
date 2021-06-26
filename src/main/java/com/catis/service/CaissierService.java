package com.catis.service;

import com.catis.model.Caissier;
import com.catis.repository.CaissierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CaissierService {
    @Autowired
    private CaissierRepository caissierRepository;

    public Caissier add(Caissier caissier) throws SQLIntegrityConstraintViolationException {
        return caissierRepository.save(caissier);
    }

    public List<Caissier> findAll(){
        List<Caissier> caissiers = new ArrayList<>();
        caissierRepository.findByActiveStatusTrue().forEach(caissiers::add);
        return caissiers;
    }

    public Caissier findById(Long id){
        Optional<Caissier> caissier = caissierRepository.findById(id);
            if(caissier.isPresent())
                return caissier.get();
        return null;
    }

    public List<Caissier> findByCaisse(Long id){
         return caissierRepository.findByCaisse_caisseId(id);
    }

    public void deleteById(Long id){
        caissierRepository.deleteById(id);
    }
}
