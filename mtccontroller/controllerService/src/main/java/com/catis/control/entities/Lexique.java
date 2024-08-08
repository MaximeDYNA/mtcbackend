package com.catis.control.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.control.entities.inherited.JournalData;

import lombok.*;

@Entity @Getter @Setter @Audited
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Lexique extends JournalData {

	@Id
	@Column(name = "id", updatable = false, nullable = false)
	private String id;

	private String libelle;

	private String code;

	private Boolean visuel;

	private boolean haschild;

	@ManyToOne
	private VersionLexique versionLexique;
	
	@ManyToMany(mappedBy = "lexiques")
	private Set<Client> clients;

	@ManyToOne
	private CategorieVehicule categorieVehicule;

	@OneToMany(mappedBy = "parent")
	private Set<Lexique> childs = new HashSet<>();

	@ManyToOne
	private Lexique parent;

	@ManyToMany(mappedBy = "lexiques")
	private Set<Inspection> inspections;

	@ManyToOne
	private Classification classification;
	
	@OneToMany(mappedBy = "lexique")
	private Set<Seuil> seuils;
}
