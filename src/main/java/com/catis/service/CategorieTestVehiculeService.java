package com.catis.service;

import com.catis.model.entity.CategorieTest;
import com.catis.repository.CategorieTestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategorieTestVehiculeService {
    @Autowired
    private CategorieTestRepo cr;

    public List<CategorieTest> findActiveCateGories(){
        return cr.findByActiveStatusTrue();
    }
}
