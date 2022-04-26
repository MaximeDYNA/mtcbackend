package com.catis.repository;

import com.catis.model.entity.Formule;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface FormuleRepository extends CrudRepository<Formule, String> {

    List<Formule> findByActiveStatusTrue();
}
