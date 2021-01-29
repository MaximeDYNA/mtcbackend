package com.catis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.Classification;
import com.catis.repository.ClassificationRepository;

@Service
public class ClassificationService {

    @Autowired
    private ClassificationRepository cR;

    public Classification findById(Long id) {
        if (id == null)
            return null;
        return cR.findById(id).get();
    }
}
