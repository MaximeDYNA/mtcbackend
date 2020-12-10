package com.catis.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class VersionLexique {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String libelle;

	private Date date;

	@OneToMany(mappedBy = "versionLexique")
	private Set<Lexique> lexiques;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "visite")
	private List<RapportDeVisite> rapportDeVisites;

	@OneToOne(mappedBy = "visite")
	private VerbalProcess process;
}