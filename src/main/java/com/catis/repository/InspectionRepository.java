package com.catis.repository;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.Inspection;

public interface InspectionRepository extends CrudRepository<Inspection, Long> {

	Inspection findByVisite_IdVisite(Long idvisite);
	
}
