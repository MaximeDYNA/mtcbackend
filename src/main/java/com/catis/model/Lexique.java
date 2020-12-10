package com.catis.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Lexique {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String libelle;

	private String code;

	private String Visuel;

	@ManyToOne
	private VersionLexique versionLexique;
}