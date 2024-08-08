package com.catis.control.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.*;

@Entity @Table(name = "t_organisation")
@Getter @Setter @Audited
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Organisation {

	@Id
	@GeneratedValue(generator = "UUID")
	@Type(type="uuid-char")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID organisationId;

	private String name;

	private boolean parent;

	private double score;

	@Column(name = "created_date", updatable = false)
	@CreatedDate
	private LocalDateTime createdDate;

	@LastModifiedDate
	private LocalDateTime modifiedDate;

	@Column(name = "created_by")
	@CreatedBy
	private String createdBy;

	@Column(name = "modified_by")
	@LastModifiedBy
	private String modifiedBy;

	@Column(columnDefinition = "bit default 1")
	private boolean activeStatus;

	@OneToMany(mappedBy="organisation")
	private Set<Adresse> adresses = new HashSet<>();

	@OneToMany(mappedBy = "organisation")
	private Set<ProprietaireVehicule> proprietaireVehicules = new HashSet<>();
	
	@OneToMany(mappedBy="organisation")
	private Set<Partenaire> partenaires = new HashSet<>();
	
	@OneToMany(mappedBy = "parentOrganisation")
	private Set<Organisation> childOrganisations = new HashSet<>();

	@ManyToOne
	private Organisation parentOrganisation;
	
	@OneToMany(mappedBy = "organisation")
	private Set<Caisse> caisses = new HashSet<>();
	
	@OneToMany(mappedBy = "organisation")
	private Set<Caissier> caissiers  = new HashSet<>();
	
	@OneToMany(mappedBy = "organisation")
	private Set<Controleur> controleurs = new HashSet<>();

	@OneToMany(mappedBy = "organisation")
	private Set<CategorieTestMachine> categorieTestMachines = new HashSet<>();
	
	@OneToMany(mappedBy = "organisation")
	private Set<Inspection> inspections = new HashSet<>();
	
	@OneToMany(mappedBy = "organisation")
	private Set<Ligne> lignes = new HashSet<>();
	
	@OneToMany(mappedBy = "organisation")
	private Set<LigneMachine> ligneMachines = new HashSet<>();
	
	@OneToMany(mappedBy = "organisation")
	private Set<Machine> machines = new HashSet<>();
	
	@OneToMany(mappedBy = "organisation")
	private Set<Mesure> mesures = new HashSet<>();
	
	@OneToMany(mappedBy = "organisation")
	private Set<ModeleVehicule> modeleVehicules = new HashSet<>();
	
	@OneToMany(mappedBy = "organisation")
	private Set<Utilisateur> utilisateurs = new HashSet<>();
	
	@OneToMany(mappedBy = "organisation")
	private Set<ValeurTest> valeurTests = new HashSet<>();
	
	@OneToMany(mappedBy = "organisation")
	private Set<Visite> visites = new HashSet<>();

	private String patente;
	
	private String statutJurique;
	
	private String numeroDeContribuable;

	private String adress;

	private String devise;

	private String lang;

	private String region;

	private String nom;

	private String tel1;

	private String tel2;
}
