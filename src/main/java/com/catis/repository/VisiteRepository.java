package com.catis.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.catis.model.Visite;

public interface VisiteRepository extends CrudRepository<Visite, Long> {

	
	List<Visite> findByCarteGriseNumImmatriculationIgnoreCaseOrCarteGrise_Vehicule_ChassisIgnoreCase(String imOrCha, String imOrCha2);
	List<Visite> findByContreVisiteFalse();
	List<Visite> findByContreVisiteFalseAndCarteGriseNumImmatriculationIgnoreCaseOrCarteGrise_Vehicule_ChassisIgnoreCase(String imOrCha, String imOrCha2);
	List<Visite> findByEncoursTrue();
	List<Visite> findByStatut(int status);
}
