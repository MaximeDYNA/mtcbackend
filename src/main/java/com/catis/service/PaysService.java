package com.catis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.entity.Pays;
import com.catis.repository.PaysRepository;

@Service
public class PaysService {

    @Autowired
    private PaysRepository paysRepository;

    public Pays addPays(Pays pays) {
        return paysRepository.save(pays);
    }

    public List<Pays> findAllPays() {
        List<Pays> pays = new ArrayList<>();
        paysRepository.findAll().forEach(pays::add);
        return pays;
    }
}
