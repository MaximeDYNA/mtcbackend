package com.catis.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_categorietestmachine")
public class CategorieTestMachine {
	//table pivot entre catégorietest et machine
	@Id
	private String idCategorieTestMachine;
	private String idOrganisation;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idcategorietest")
	private CategorieTest categorieTest;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idMachine")
	private Machine machine;
	
	public CategorieTestMachine() {
	
	}
	public CategorieTestMachine(String idCategorieTestMachine, String idOrganisation, CategorieTest categorieTest,
			Machine machine) {
		super();
		this.idCategorieTestMachine = idCategorieTestMachine;
		this.idOrganisation = idOrganisation;
		this.categorieTest = categorieTest;
		this.machine = machine;
	}
	public String getIdCategorieTestMachine() {
		return idCategorieTestMachine;
	}
	public void setIdCategorieTestMachine(String idCategorieTestMachine) {
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
