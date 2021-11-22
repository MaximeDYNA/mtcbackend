package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Adresse;

import java.util.UUID;

public interface AdresseRepository extends CrudRepository<Adresse, UUID> {


}
