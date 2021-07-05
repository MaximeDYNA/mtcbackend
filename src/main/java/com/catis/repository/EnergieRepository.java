package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Energie;

import java.util.List;

public interface EnergieRepository extends CrudRepository<Energie, Long> {
    List<Energie> findByActiveStatusTrue();

}
