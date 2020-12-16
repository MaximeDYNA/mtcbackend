package com.catis.model;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_categorietestmachine")
public class CategorieTestMachine extends JournalData {
	//table pivot entre catégorietest et machine
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idCategorieTestMachine;
	
	@ManyToOne
	private Organisation organisation;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idcategorietest")
	private CategorieTest categorieTest;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idMachine")
	private Machine machine;

	public CategorieTestMachine() {
		// TODO Auto-generated constructor stub
	}

	

	public CategorieTestMachine(Long idCategorieTestMachine, Organisation organisation, CategorieTest categorieTest,
			Machine machine) {
		this.idCategorieTestMachine = idCategorieTestMachine;
		this.organisation = organisation;
		this.categorieTest = categorieTest;
		this.machine = machine;
	}



	public Long getIdCategorieTestMachine() {
		return idCategorieTestMachine;
	}

	public void setIdCategorieTestMachine(Long idCategorieTestMachine) {
		this.idCategorieTestMachine = idCategorieTestMachine;
	}

	
	public Organisation getOrganisation() {
		return organisation;
	}



	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}



	public CategorieTest getCategorieTest() {
		return categorieTest;
	}

	public void setCategorieTest(CategorieTest categorieTest) {
		this.categorieTest = categorieTest;
	}

	public Machine getMachine() {
		return machine;
	}

	public void setMachine(Machine machine) {
		this.machine = machine;
	}
	
	
}
