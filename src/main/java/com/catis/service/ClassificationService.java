package com.catis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.entity.Classification;
import com.catis.repository.ClassificationRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class ClassificationService {

    @Autowired
    private ClassificationRepository cR;

    public Optional<Classification> findById(UUID id) {
        if (id == null)
            return null;
        return cR.findById(id);
    }
}
