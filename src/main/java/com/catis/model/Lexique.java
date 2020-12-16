package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Lexique {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String libelle;

	private String code;

	private Boolean Visuel;

	@ManyToOne
	private VersionLexique versionLexique;
	
	@OneToMany(mappedBy = "parent")
	private Set<Lexique> childs;

	@ManyToOne
	private Lexique parent;
	
	private String decision;
}