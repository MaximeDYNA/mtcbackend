package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Vendeur;

public interface VendeurRepository extends CrudRepository<Vendeur, Long> {

    Vendeur findByPartenaire_PartenaireId(long partenaireId);
}
