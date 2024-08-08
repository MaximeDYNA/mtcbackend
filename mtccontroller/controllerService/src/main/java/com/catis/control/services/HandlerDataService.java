package com.catis.control.services;

import java.util.*;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.catis.control.event.VisiteCreatedEvent;
import com.catis.control.dao.*;
import com.catis.control.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.catis.control.entities.GieglanFile.StatusType;
import org.springframework.transaction.event.TransactionalEventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class HandlerDataService {

	@Autowired
	private VisiteDao visiteDao;

	@Autowired
	private ProcessVerbalService processVerbalService;

	@Autowired
	private FormuleDao formuleDao;

	@Autowired
	private ScheduledService scheduledService;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	private Set<RapportDeVisite> rapportDeVisites;

	private GieglanFile gieglanFile;

	private Inspection inspection;

	private Set<GieglanFile> files = new HashSet<>();

	private boolean status = true;

	private boolean isAccept = true;

	private Visite visite;

	private static Logger log = LoggerFactory.getLogger(HandlerDataService.class);

	/**
	 * Treatment of data of banc test
	 */
	@Scheduled(fixedDelay = 20000)
	// @Scheduled(fixedDelay = 30000)
	public void start() {
		try {
			log.info("Starting handler service for banc test");
			List<Visite> visites = this.visiteDao.getVisiteWithReceivedTest(PageRequest.of(0, 10));
			visites.forEach(v -> { handlerAllMeasures(v); });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Treatment of data of banc test
	 *
	 * @param v Visite
	 */
	public void handlerAllMeasures(Visite v) {
		log.info("processing all measures of banc test");
		this.visite = v;
		status = true;
		isAccept = true;
		rapportDeVisites = new HashSet<>();
		System.err.println("visite numero :" +v.getIdVisite()+"\n");

		if (this.checkIfReceivedTestIsComplete(v)) {
			log.info("checking if received test is complete for the current visite");
			Produit produit = v.getCarteGrise().getProduit();
			this.inspection = v.getInspection();
			log.info("processing measure of bank using product");
			this.checkMeasureOfBank(produit);
			log.info("checking visuel");
			this.checkVisualDefault();
			log.info("updating visite");
			this.updatedVisite(v);
			log.info("creating visite verbal process with status" + this.status);
			this.processVerbalService.save(v, this.rapportDeVisites, this.status);
			VisiteCreatedEvent event = new VisiteCreatedEvent(v);
			log.info("publishing visite update/create event");
			applicationEventPublisher.publishEvent(event);
		}
	}

	/**
	 * Check if test received is complete
	 *
	 * @param visite
	 * @return
	 */
	private boolean checkIfReceivedTestIsComplete(Visite visite) {
		log.info("Starting handler service for banc test");
		List<CategorieTest> testsAttempts = new ArrayList<>();
		if(!visite.isContreVisite()) {
			log.info("processing data for control initial");
			visite.getCarteGrise().getProduit().getCategorieTestProduits().forEach(categorieTestVehicule ->
				testsAttempts.add(categorieTestVehicule.getCategorieTest())
			);
		} else {
			log.info("processing data for control visite");
			this.visiteDao.getLastVisiteWithTestFailed(visite.getControl(), visite, PageRequest.of(0, 1))
			.forEach(v ->
				v.getInspection().getGieglanFiles().forEach(file ->
					testsAttempts.add(file.getCategorieTest())
				)
			);
		}

		files = visite.getInspection().getGieglanFiles().stream()
			.filter(file -> testsAttempts.contains(file.getCategorieTest()))
			.collect(Collectors.toSet());
		
		if(files.isEmpty()){
			log.info("no files found for current visite");

		}

		System.err.println("Test attendu : "+ testsAttempts.size() + "test recu  :" +files.size());

		return testsAttempts.size() == files.size();
	}

	/**
	 * This allows you to check all measure sent by the test banc
	 * and verify them about seuil
	 *
	 * @param produit
	 */
	private void checkMeasureOfBank(Produit produit) {
		log.info("checking measure of bank");
		List<Formule> formules = this.formuleDao.getFormuleByProducts(produit);
		if(formules == null) {
			log.info("oops could not find formule by products");
		}
		files.forEach(file -> {
			if(!"json".equalsIgnoreCase(file.getCategorieTest().getLibelle())) {
				this.gieglanFile = file;
				this.isAccept = true;
				Map<String, String> data = new HashMap<>();
				file.getValeurTests().forEach(vt -> data.put("${"+vt.getCode()+"}", vt.getValeur()));
				log.info("setting valuer test codes for geiglan files");
				formules.forEach(f -> this.handlerSpecifyMeasure(f, data));
				log.info("setting status of gieglan file to " + isAccept);
				this.gieglanFile.setIsAccept(isAccept);
				this.status = !isAccept && status ? isAccept : status;
			}
		});
	}

	/**
	 * This is method allows you to verify the measure
	 * to the limit seuil define
	 *
	 * @param f
	 * @param data
	 */
	private void handlerSpecifyMeasure(Formule f, Map<String, String> data) {
		log.info("processing seuil using formule and creating rapport de viistes");
		if (data.containsKey(f.getDescription())) {
			log.info("found formule with description");
			try {
				String content = data.get(f.getDescription());
				Double result = Double.valueOf(content.isEmpty() ? "0": content);
				log.info("retrieveing seuil for formule");
				f.getSeuils().stream()
				.sorted(Comparator.comparing(Seuil::getValue, Comparator.reverseOrder()))
				.filter(s ->
					(">".equals(s.getOperande())  && result > s.getValue())  ||
                    (">=".equals(s.getOperande()) && result >= s.getValue()) ||
					("<".equals(s.getOperande())  && result < s.getValue())  ||
                    ("<=".equals(s.getOperande()) && result <= s.getValue()) ||
					("=".equals(s.getOperande())  && result.equals(s.getValue()))
				)
				.findFirst()
				.ifPresent(seuil -> this.setRapportDeVisite(seuil, content) );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Initialize RapportDeVisite and links to seuil
	 *
	 * @param s
	 * @param result
	 */
	private void setRapportDeVisite(Seuil s, String result) {
		log.info("creating rapport de visite for the inspection");
		RapportDeVisite rdv = new RapportDeVisite();
		this.isAccept = !s.isDecision() && isAccept ? s.isDecision() : isAccept;
		log.info("seuil decision is " + this.isAccept);
		rdv.setResult(result);
		rdv.setDecision(s.isDecision());
		rdv.setCodeMessage(s.getCodeMessage());
		rdv.setSeuil(s);
		rdv.setGieglanFile(this.gieglanFile);
		rdv.setVisite(this.visite);
		rdv.setOrganisation(this.visite.getOrganisation());
		this.rapportDeVisites.add(rdv);
		log.info("rapport de visite added");
	}

	/**
	 * This method allows you to check otherwise is Major.
	 * if that'case, the process verbal'll rejected
	 *
	 */
	// private void checkVisualDefault() {
	// 	log.info("starting visuel check");
	// 	GieglanFile jsonFile = files.stream().filter(
	// 		file -> "JSON".equalsIgnoreCase(file.getCategorieTest().getLibelle())
	// 	).findFirst().map( f -> {
	// 		log.info("setting visuel test passed");
	// 		f.setIsAccept(true);
	// 		return f;
	// 	}).orElseGet(null);

	// 	this.inspection.getLexiques().forEach(lexique -> {
	// 		if ("majeure".equalsIgnoreCase(lexique.getClassification().getCode())) {
    //             this.status = false;
	// 			log.info("setting visuel test failed");
    //             jsonFile.setIsAccept(false);
	// 		}
	// 	});
	// }

	// flemming implimented

	private void checkVisualDefault() {
		log.info("starting visuel check");
		GieglanFile jsonFile = files.stream().filter(
			file -> "JSON".equalsIgnoreCase(file.getCategorieTest().getLibelle())
		).findFirst().map(f -> {
			log.info("setting visuel test passed");
			f.setIsAccept(true);
			return f;
		}).orElse(null);  // Use orElse instead of orElseGet
	
		this.inspection.getLexiques().forEach(lexique -> {
			if ("majeure".equalsIgnoreCase(lexique.getClassification().getCode())) {
				this.status = false;
				log.info("setting visuel test failed");
				if (jsonFile != null) {  // Add null check for jsonFile
					jsonFile.setIsAccept(false);
				}else{
					log.info("json file is null");
				}
			}
		});
	}
	

	/**
	 * update Control and visite when treatement is finished
	 *
	 * @param visite {@link Visite}
	 * @return void
	 */
	private void updatedVisite(Visite visite) {
		log.info("updating visite");
		Control control = visite.getControl();
		if(control == null) {
			log.info("received visite has no control associated to it");
		}
		Produit prod = control.getCarteGrise().getProduit();
		if(prod == null) {
			log.info("control has no cartegrise associated to it");
		}
		control.setStatus(this.status ? StatusType.VALIDATED : StatusType.REJECTED);
		log.info("checking control status" + this.status);
		if (this.status) {
			log.info("setting validity date for passed control inspection");
			control.setValidityAt(control.getCreatedDate().plusMonths(prod.getDelaiValidite()));
		} else {
			log.info("setting validity date for failed control inspection");
			control.setContreVDelayAt(control.getCreatedDate().plusDays(15));
		}
		visite.setStatut(3);
		visite.getInspection().setDateFin(new Date());
		log.info("associating visiste to control");
		visite.setControl(control);
	}

	/**
	 * This method is call after transaction complete
	 *
	 * @param event VisiteCreatedEvent
	 */
	@TransactionalEventListener
	public void doAfterCommit(VisiteCreatedEvent event) {
		System.err.println("event listener   "+event.getVisite().getIdVisite());
		scheduledService.notifyCaisseForModification(event.getVisite());
	}
}
