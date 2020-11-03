package com.catis.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.Client;

public interface ClientRepository extends CrudRepository<Client, String>{

	Client findByPartenaire_PartenaireId(long id);
	Client findByClientId(long id);
}
