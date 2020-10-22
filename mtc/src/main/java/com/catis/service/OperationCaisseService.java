package com.catis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.OperationCaisse;
import com.catis.repository.OperationDeCaisseRepository;

@Service
public class OperationCaisseService {

	@Autowired
	private OperationDeCaisseRepository operationCaisseRepository;
	
	public void addOperationCaisse(OperationCaisse op) {
		operationCaisseRepository.save(op);
	}
	
}
