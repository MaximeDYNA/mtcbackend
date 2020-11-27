package com.catis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.CarteGrise;
import com.catis.repository.CarteGriseRepository;

@Service
public class CarteGriseService {

	@Autowired
	private CarteGriseRepository cgr;
	
	public CarteGrise addCarteGrise(CarteGrise carteGrise) {
		if(cgr.findByNumImmatriculationIgnoreCaseOrVehicule_ChassisIgnoreCase(carteGrise.getNumImmatriculation(), 
				carteGrise.getNumImmatriculation()).isEmpty())
		return cgr.save(carteGrise);
		else
		return cgr.findByNumImmatriculationIgnoreCaseOrVehicule_ChassisIgnoreCase(carteGrise.getNumImmatriculation(), 
				carteGrise.getNumImmatriculation()).get(0);
	}
	public CarteGrise updateCarteGrise(CarteGrise carteGrise) {
		return cgr.save(carteGrise);
	}
	public List<CarteGrise> findAll() {
		List<CarteGrise> carteGrises = new ArrayList<>();
		cgr.findAll().forEach(carteGrises::add);
		return carteGrises;
	}
	public CarteGrise findCarteGriseById(Long carteGriseId) {
		return cgr.findById(carteGriseId).get();
	}
	public List<CarteGrise> findByImmatriculationOuCarteGrise(String imOrCha){
		return cgr.findByNumImmatriculationIgnoreCaseOrVehicule_ChassisIgnoreCase(imOrCha, imOrCha);
	}
	public List<CarteGrise> findBychassis(String chassis){
		return cgr.findByVehicule_ChassisStartsWithIgnoreCase(chassis);
	}
	
	
}
