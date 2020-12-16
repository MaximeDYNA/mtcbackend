package com.catis.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class VersionLexique extends JournalData {

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