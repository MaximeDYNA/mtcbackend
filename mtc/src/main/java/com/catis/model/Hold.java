package com.catis.model;


import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_holds")
public class Hold {
	
	@Id
	private int id;
	
	private int number;
	private String time;
	
	@ManyToOne
	private SessionCaisse sessioncaisse;
	
	public Hold() {
		
	}

	public Hold(int id, int number, String time, SessionCaisse sessioncaisse) {
		super();
		this.id = id;
		this.number = number;
		this.time = time;
		this.sessioncaisse = sessioncaisse;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public SessionCaisse getSessioncaisse() {
		return sessioncaisse;
	}

	public void setSessioncaisse(SessionCaisse sessioncaisse) {
		this.sessioncaisse = sessioncaisse;
	}
	
	

}
