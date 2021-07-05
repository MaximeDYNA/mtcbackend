package com.catis.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.DetailVente;

public interface DetailVenteRepository extends CrudRepository<DetailVente, String> {

    List<DetailVente> findByVente_IdVente(Long id);
}
