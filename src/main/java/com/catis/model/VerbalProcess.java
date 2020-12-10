package com.catis.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class VerbalProcess {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String reference;

	private String signature; //qrc

	@OneToOne
	private Visite visite;
}