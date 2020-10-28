package com.catis.service;

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
		return cgr.save(carteGrise);
	}
	public List<CarteGrise> findByImmatriculationOuCarteGrise(String imOrCha){
		return cgr.findByNumImmatriculationIgnoreCaseOrVehicule_ChassisIgnoreCase(imOrCha, imOrCha);
	}
	
}
