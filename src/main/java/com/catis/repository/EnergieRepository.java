package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Energie;

import java.util.List;
import java.util.UUID;

public interface EnergieRepository extends CrudRepository<Energie, UUID> {
    List<Energie> findByActiveStatusTrue();

}
