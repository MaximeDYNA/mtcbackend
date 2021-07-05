package com.catis.repository;

import com.catis.model.entity.Formule;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FormuleRepository extends CrudRepository<Formule, Long> {

    List<Formule> findByActiveStatusTrue();
}
