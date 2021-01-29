package com.catis.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.Vendeur;

public interface VendeurRepository extends CrudRepository<Vendeur, Long> {

    Vendeur findByPartenaire_PartenaireId(long partenaireId);
}
