package com.catis.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Client;

public interface ClientRepository extends CrudRepository<Client, UUID> {

    Client findByPartenaire_PartenaireId(UUID id);

    Client findByClientId(UUID id);

    List<Client> findByActiveStatusTrue();
    List<Client> findByActiveStatusTrue(Pageable pageable);
}
