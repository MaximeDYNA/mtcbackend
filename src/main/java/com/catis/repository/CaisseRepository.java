package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.Caisse;

import java.util.List;

public interface CaisseRepository extends CrudRepository<Caisse, Long> {
    List<Caisse> findByActiveStatusTrue();

}
