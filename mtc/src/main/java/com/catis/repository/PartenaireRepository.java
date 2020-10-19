package com.catis.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.Partenaire;

public interface PartenaireRepository extends CrudRepository<Partenaire, String>{
	List<Partenaire> findByNom(String nom);
}
