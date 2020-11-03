package com.catis.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="t_lignemachine")
public class LigneMachine {
	/*table pivot qui nous permet de savoir quels sont les machines par lignes et les lignes
		par machine*/
	@Id
	private String ligneMachine;
	private String idOrganisation;
	@ManyToOne
	@JoinColumn(name="idMachine")
	private Machine machine;
	public String getIdOrganisation() {
		return idOrganisation;
	}
	public void setIdOrganisation(String idOrganisation) {
		this.idOrganisation = idOrganisation;
	}

	@ManyToOne
	@JoinColumn(name="idLigne")
	private Ligne ligne;

	public LigneMachine() {

	}
	public LigneMachine(String ligneMachine, Machine machine, Ligne ligne) {
		super();
		this.ligneMachine = ligneMachine;
		this.machine = machine;
		this.ligne = ligne;
	}

	public String getLigneMachine() {
		return ligneMachine;
	}

	public void setLigneMachine(String ligneMachine) {
		this.ligneMachine = ligneMachine;
	}

	public Machine getMachine() {
		return machine;
	}

	public void setMachine(Machine machine) {
		this.machine = machine;
	}

	public Ligne getLigne() {
		return ligne;
	}

	public void setLigne(Ligne ligne) {
		this.ligne = ligne;
	}

	
	
	
}
