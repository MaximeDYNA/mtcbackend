package com.catis.control.entities;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.control.entities.inherited.JournalData;

import lombok.*;

@Entity @Table(name = "t_partenaire")
@Getter @Setter @Audited
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Partenaire extends JournalData {

	@Id
	@GeneratedValue(generator = "UUID")
	@Type(type="uuid-char")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID partenaireId;
	
	private String nom;
	
	private String prenom;
	
	@Column(nullable=true)
	private Date dateNaiss; // date de naissance
	
	private String lieuDeNaiss; // lieu de naissance
	
	@Column(unique=true)
	private String passport;
	
	@Column(unique=true)
	private String permiDeConduire;
	
	@Column(unique=true)
	private String cni;
	
	@Column(unique=true)
	private String telephone;
	
	@Column(unique=true)
	private String email;

	private String numeroContribuable;
	
	@OneToMany(mappedBy="partenaire")
	private Set<ProprietaireVehicule> proprietaireVehicule = new HashSet<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="partenaire")
	private Set<Client> client = new HashSet<>();

	@OneToMany(mappedBy="partenaire")
	private Set<Contact> contact = new HashSet<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="partenaire")
	private Set<Adresse> adresses  = new HashSet<>();
	
	@OneToMany(mappedBy="partenaire")
	private Set<Controleur> controleurs = new HashSet<>();
	
	@OneToMany(mappedBy="partenaire")
	private Set<Caissier> caissiers = new HashSet<>();
	
	@OneToMany(mappedBy="partenaire")
	private Set<Vendeur> vendeurs = new HashSet<>();
}
