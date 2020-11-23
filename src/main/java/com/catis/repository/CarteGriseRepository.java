package com.catis.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.CarteGrise;

public interface CarteGriseRepository extends CrudRepository<CarteGrise, Long>{
	
	List<CarteGrise> findByNumImmatriculationIgnoreCaseOrVehicule_ChassisIgnoreCase(String immatriculation, String Chassis);
	List<CarteGrise> findByVehicule_ChassisStartsWithIgnoreCase(String chassis);
	
}
