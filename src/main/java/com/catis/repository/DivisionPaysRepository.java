package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.DivisionPays;

import java.util.UUID;

public interface DivisionPaysRepository extends CrudRepository<DivisionPays, UUID> {

}
