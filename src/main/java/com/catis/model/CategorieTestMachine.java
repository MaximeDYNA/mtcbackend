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
	
	private String idOrganisation;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idcategorietest")
	private CategorieTest categorieTest;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idMachine")
	private Machine machine;

	public CategorieTestMachine() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CategorieTestMachine(Long idCategorieTestMachine, String idOrganisation, CategorieTest categorieTest,
			Machine machine) {
		super();
		this.idCategorieTestMachine = idCategorieTestMachine;
		this.idOrganisation = idOrganisation;
		this.categorieTest = categorieTest;
		this.machine = machine;
	}

	public Long getIdCategorieTestMachine() {
		return idCategorieTestMachine;
	}

	public void setIdCategorieTestMachine(Long idCategorieTestMachine) {
		this.idCategorieTestMachine = idCategorieTestMachine;
	}

	public String getIdOrganisation() {
		return idOrganisation;
	}

	public void setIdOrganisation(String idOrganisation) {
		this.idOrganisation = idOrganisation;
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
