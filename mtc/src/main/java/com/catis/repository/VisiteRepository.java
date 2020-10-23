package com.catis.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.catis.model.Visite;

public interface VisiteRepository extends CrudRepository<Visite, Long> {

	List<Visite> findByCarteGriseNumImmatriculationStartsWithIgnoreCaseOrCarteGrise_Vehicule_ChassisStartsWithIgnoreCase(String imOrCha, String imOrCha2);
	List<Visite> findByContreVisiteFalse();
}
