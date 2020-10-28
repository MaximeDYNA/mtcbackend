package com.catis.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.CarteGrise;

public interface CarteGriseRepository extends CrudRepository<CarteGrise, String>{

	List<CarteGrise> findByNumImmatriculationIgnoreCaseOrVehicule_ChassisIgnoreCase(String immatriculation, String Chassis);
}
