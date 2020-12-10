package com.catis.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * @author AubryYvan
 */
@Entity
public class Classification {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String code;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "classification")
	private Set<Seuil> seuils;

	public Classification() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Classification(Long id, String code, Set<Seuil> seuils) {
		super();
		this.id = id;
		this.code = code;
		this.seuils = seuils;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Set<Seuil> getSeuils() {
		return seuils;
	}

	public void setSeuils(Set<Seuil> seuils) {
		this.seuils = seuils;
	}
}