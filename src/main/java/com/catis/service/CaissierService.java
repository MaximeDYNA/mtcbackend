package com.catis.service;

import com.catis.model.entity.Caissier;
import com.catis.repository.CaissierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CaissierService {
    @Autowired
    private CaissierRepository caissierRepository;

    public Caissier add(Caissier caissier) throws SQLIntegrityConstraintViolationException {
        return caissierRepository.save(caissier);
    }

    public List<Caissier> findAll(Pageable pageable){
        List<Caissier> caissiers = new ArrayList<>();
        caissierRepository.findByActiveStatusTrue(pageable).forEach(caissiers::add);
        return caissiers;
    }
    public List<Caissier> findAll(){
        List<Caissier> caissiers = new ArrayList<>();
        caissierRepository.findByActiveStatusTrue().forEach(caissiers::add);
        return caissiers;
    }

    public Caissier findById(UUID id){
        Optional<Caissier> caissier = caissierRepository.findById(id);
            if(caissier.isPresent())
                return caissier.get();
        return null;
    }

    public Caissier findBylogin(String login){
        Optional<Caissier> caissier = caissierRepository.findByUser_Login(login);
        if(caissier.isPresent())
            return caissier.get();
        return null;
    }

    public List<Caissier> findByCaisse(UUID id){
         return caissierRepository.findByCaisse_caisseId(id);
    }

    public void deleteById(UUID id){
        caissierRepository.deleteById(id);
    }
}
