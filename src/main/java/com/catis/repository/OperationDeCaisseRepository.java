package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.OperationCaisse;

public interface OperationDeCaisseRepository extends CrudRepository<OperationCaisse, String> {

	OperationCaisse findByNumeroTicket(String ticket);
}
