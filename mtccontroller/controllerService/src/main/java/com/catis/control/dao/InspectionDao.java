package com.catis.control.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.catis.control.entities.Inspection;

@Repository
public interface InspectionDao extends JpaRepository<Inspection, UUID> {
	
	@Query(value = "select i from Inspection i join fetch i.gieglanFiles f "
	+ "left join fetch f.valeurTests v where "//v.status = 'INITIALIZED' "
	+ " f.status = 'INITIALIZED' and (f.type = 'MEASURE' OR f.type = 'MACHINE') ")
	List<Inspection> findInspectionWithFile(Pageable pageable);
}
