package com.catis.model;


import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "t_hold")
public class Hold {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long holdId;
	
	@Column(unique=true)
	private Long number;
	private Date time;
	
	@ManyToOne
	@JsonIgnore
	private SessionCaisse sessionCaisse;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="hold")
	private Set<Posales> posales;
	
	public Hold() {
		
	}

	

	



	public Hold(Long holdId, Long number, Date time, SessionCaisse sessionCaisse) {
		super();
		this.holdId = holdId;
		this.number = number;
		this.time = time;
		this.sessionCaisse = sessionCaisse;
	}



	public Long getHoldId() {
		return holdId;
	}



	public void setHoldId(Long holdId) {
		this.holdId = holdId;
	}


	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	

	public Date getTime() {
		return time;
	}



	public void setTime(Date time) {
		this.time = time;
	}


	@JsonIgnore
	public SessionCaisse getSessionCaisse() {
		return sessionCaisse;
	}



	public void setSessionCaisse(SessionCaisse sessionCaisse) {
		this.sessionCaisse = sessionCaisse;
	}


	@JsonIgnore
	public Set<Posales> getPosales() {
		return posales;
	}


	public void setPosales(Set<Posales> posales) {
		this.posales = posales;
	}
	
	

}