package com.catis.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

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
	
	public String genererTicket() {
		LocalDate now = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddhhmmssSSSS");
		String start = "T" + now.format(formatter);
		
		return start;
	}
	public int randomNumber() {
		Random rand = new Random();
		return rand.nextInt(1000);
	}
}
