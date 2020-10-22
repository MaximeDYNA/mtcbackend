package com.catis.service;

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
}
