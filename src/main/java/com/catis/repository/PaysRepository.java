package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Pays;

import java.util.UUID;

public interface PaysRepository extends CrudRepository<Pays, UUID> {

}
