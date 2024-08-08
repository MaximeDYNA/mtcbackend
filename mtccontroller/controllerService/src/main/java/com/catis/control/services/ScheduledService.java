package com.catis.control.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.catis.control.dao.MachineDao;
import com.catis.control.dto.FraudDto;
import com.catis.control.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.catis.control.dao.CategorieTestDao;
import com.catis.control.dao.GieglanFileDao;
import com.catis.control.dao.InspectionDao;
import com.catis.control.entities.GieglanFile.FileType;
import com.catis.control.entities.GieglanFile.StatusType;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author AubryYvan
 */
@Service
public class ScheduledService {

	@Autowired
	private CategorieTestDao categorieTestDao;

	@Autowired
	private InspectionDao inspectionDao;

	@Autowired
	private GieglanFileDao gieglanFileDao;

	@Autowired
	private MachineDao machineDao;

	@Autowired
	private Environment env;

	private Inspection inspection;

	private Visite visite;

	private boolean isFraud;

	private static Logger log = LoggerFactory.getLogger(ScheduledService.class);

	/**
	 * Job loaded to check integrity of measure value
	 */
	@Scheduled(fixedDelay = 10000)
	// @Scheduled(fixedDelay = 15000)
	public void checkIntegrityFile() {
		log.info("Began integrity check for geiglan files");
		List<Inspection> inspections = this.inspectionDao.findInspectionWithFile(PageRequest.of(0, 1));
		if(inspections.isEmpty()){
			log.info("No inspection found");
		}
		log.info("Processing inspection relations like visite, gieglan etc");
		inspections.forEach(i -> {
			inspection = i;
			visite = i.getVisite();
			isFraud = false;
			i.getGieglanFiles().forEach(this::checkIntegrityData);
			i.setVisite(visite);
			notifyFraudService(isFraud, isFraud ? "FR002" : "" ,visite);
		});

	}

	/**
	 * check if value of measure match to crc and change the status of 
	 * object ValeurTest
	 * 
	 * verifier que toutes les mesures attendues ont été reçue
	 * 
	 * @param file GieglanFile
	 */
	private void checkIntegrityData(GieglanFile file) {
		log.info("checking gieglan file integrity");
		if(file.getType().equals(FileType.MEASURE)) {
			Optional<CategorieTest> categTest = this.categorieTestDao
				.findByLibelleIgnoreCase(file.getName().split("\\.")[1]);
			categTest.ifPresent(file::setCategorieTest); 
		}

		if (!file.getName().split("\\.")[1].equalsIgnoreCase("json") && !file.getValeurTests().isEmpty()) {
			file.setStatus(StatusType.VALIDATED);
			file.getValeurTests().forEach(v -> {
				Boolean isValid = Boolean.TRUE;
				/**Boolean isValid = this.generateCrc(v.getValeur()).equals(v.getCrc());
				if (!isValid.booleanValue()){
					file.setStatus(StatusType.REJECTED);
					isFraud = true;
				}*/

				v.setStatus( isValid.booleanValue()
					? StatusType.VALIDATED
					: StatusType.REJECTED
				);
			});
			log.info("updating visite by tests category in I");
			updateVisiteByTestCat(file);
		} else if (file.getName().split("\\.")[1].equalsIgnoreCase("json")) {
			file.setStatus(StatusType.VALIDATED);
			log.info("updating visite by tests category in II");
			updateVisiteByTestCat(file);
		} else {
			log.info("setting file status to undefined");
			file.setStatus(StatusType.NOT_DEFINED);
		}

		if (file.getType().equals(FileType.MACHINE) && file.getStatus().equals(StatusType.VALIDATED)) {
			log.info("gieglan file status type is VALIDATED and FileType is MACHINE, updating machine status"); 
    		findMachineAndUpdate(file);
		}

		gieglanFileDao.save(file);
		if (!file.getStatus().equals(StatusType.NOT_DEFINED)) {
			log.info("gieglan file status type is NOT DEFINED");
            notifyCaisseForModification(visite);
        }
	}

	private void updateVisiteByTestCat(GieglanFile file) {
		log.info("updating visite by test cat using gieglan file");
		CategorieTest test = file.getCategorieTest();
		if(test == null){
			log.info("categories test for gieglan file is mull");
		}
		log.info("ensuring giegland file status is 'validated' if otherwise fail visite instantly");
		Visite.TestResult testResult = file.getStatus().equals(StatusType.VALIDATED)
				? Visite.TestResult.SUCCESS : Visite.TestResult.ERROR;
		
		log.info("checking categories test for giegland file libelles");
		switch (test.getLibelle()) {
			case "F":
				visite.setFreinage(testResult);
				log.info("categorie test  libelle is F, setting visite Freinage to ", testResult);
				break;
			case "R":
				visite.setRipage(testResult);
				log.info("categorie test libelle is R, setting visite Ripage result to ", testResult);
				break;
			case "S":
				visite.setSuspension(testResult);
				log.info("categorie test  libelle is S, setting visite Suspension result to ", testResult);
				break;
			case "P":
				visite.setReglophare(testResult);
				log.info("categorie test libelle is P, setting visite Reglophare result to ", testResult);
				break;
			case "G":
				visite.setPollution(testResult);
				log.info("categorie test libelle is G, setting visite Pollution result to ", testResult);
				break;
			case "JSON":
				visite.setVisuel(testResult);
				log.info("categorie test libelle is Json, setting visite Visuel result to ", testResult);
				break;
			default:
				System.out.println("file extension not found "+file.getName());
				break;
		}
	}

	/**
	 * This method convert string value to crc
	 * 
	 * @param v String
	 * @return Integer
	 */
	private Integer generateCrc(String v) {

		if (v.isEmpty()) return (int)'\0';

		int code = 0;
		for (int i = 0, j = 1; i < v.length(); i++, j++) {
			code += (int)v.charAt(i)*j;
			if(5 == j) j = 0;
		}

		return code;
	}

	/**
	 * Allows to request caisse app with visite id processed
	 *
	 */
	public void notifyCaisseForModification(Visite visite) {
		log.info("notifying caisse app with visite id processed");
		try {
			RestTemplate restTemplate = new RestTemplate();
			UUID id = visite.getIdVisite();
			String endPoint = env.getProperty("endPoint.sse.notify")+id;
			ResponseEntity<String> response = restTemplate.getForEntity(endPoint, String.class);
			System.err.println("body request inspection "+id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Allows to notify Fraud service wether integrity is right or not
	 *
	 */
	public void notifyFraudService(boolean isFraud, String code, Visite visite) {
		try {

			System.out.println("code "+code);

			RestTemplate restTemplate = new RestTemplate();
			String endPoint = env.getProperty("endpoint.notify_fraud");
			FraudDto fraud = new FraudDto(isFraud, code ,visite.getIdVisite());
			ResponseEntity<Object> response = restTemplate.postForEntity(endPoint, fraud, Object.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Find machine about serie number and update machine if we found
	 *
	 * @param file
	 */
	private void findMachineAndUpdate(GieglanFile file) {
		log.info("updating  Machine for gieglan file");
		file.getValeurTests().stream()
			.filter(vTest -> "0302".equals(vTest.getCode()))
			.findFirst()
			.ifPresent(vTest -> {
				Optional<Machine>  machine = machineDao.findByNumSerie(vTest.getValeur());
				file.setMachine(machine.orElseGet(()-> createMachine(file)));
			});
	}

	/**
	 * Create Machine
	 *
	 * @param file
	 */
	public Machine createMachine(GieglanFile file) {
		log.info("Creating Machine for gieglan file");
		Machine machine = new Machine();
		file.getValeurTests().forEach(vTest -> {
			switch (vTest.getCode()) {
				case "0300":
					machine.setFabriquant(vTest.getValeur());
					break;
				case "0301":
					machine.setModel(vTest.getValeur());
					break;
				case "0302":
					machine.setNumSerie(vTest.getValeur());
					break;
			}
		});

		return machineDao.save(machine);
	}
}
