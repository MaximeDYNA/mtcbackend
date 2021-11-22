package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Vendeur;

import java.util.UUID;

public interface VendeurRepository extends CrudRepository<Vendeur, UUID> {

    Vendeur findByPartenaire_PartenaireId(long partenaireId);
}
