package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.Lexique;

public interface LexiqueRepository extends CrudRepository<Lexique, Long> {

	Lexique findByCode(String code);
}
