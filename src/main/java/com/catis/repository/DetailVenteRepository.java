package com.catis.repository;


import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.DetailVente;

public interface DetailVenteRepository extends CrudRepository<DetailVente, UUID> {

    List<DetailVente> findByVente_IdVente(UUID id);
    List<DetailVente> findByVente_NumFactureAndActiveStatusTrue(String ref);
    List<DetailVente> findByActiveStatusTrue(Pageable pageable);
}
