package com.catis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.entity.DivisionPays;
import com.catis.repository.DivisionPaysRepository;

@Service
public class DivisionPaysService {

    @Autowired
    private DivisionPaysRepository dpr;

    public DivisionPays addDivisionPays(DivisionPays dp) {
        return dpr.save(dp);
    }

    public List<DivisionPays> findAllDivisionPays() {
        List<DivisionPays> villes = new ArrayList<>();
        dpr.findAll().forEach(villes::add);
        return villes;
    }
}
