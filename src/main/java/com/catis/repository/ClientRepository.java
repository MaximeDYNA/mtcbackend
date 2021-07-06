package com.catis.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {

    Client findByPartenaire_PartenaireId(long id);

    Client findByClientId(long id);

    List<Client> findByActiveStatusTrue();
}
