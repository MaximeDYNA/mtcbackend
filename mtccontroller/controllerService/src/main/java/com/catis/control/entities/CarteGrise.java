package com.catis.control.entities;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.control.entities.inherited.JournalData;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="t_cartegrise")
@Getter @Setter @Audited
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CarteGrise extends JournalData {

	@Id
	@GeneratedValue(generator = "UUID")
	@Type(type="uuid-char")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID carteGriseId;
	
	private String numImmatriculation;
	
	private String preImmatriculation;// immatriculation précédente
	
	private Date dateDebutValid; //debut de validité
	
	private Date dateFinValid;// fin de validité
	
	private String ssdtId;
	
	private String commune;
	
	private double montantPaye;
	
	private boolean vehiculeGage; // véhicule gagé
	
	private String genreVehicule;
	
	private String enregistrement;
	
	private Date dateDelivrance;
	
	private String lieuDedelivrance;// lieu de délivrance
	
	private String centreSsdt;
	
	@ManyToOne
	private ProprietaireVehicule proprietaireVehicule;
	
	@ManyToOne
	private Vehicule vehicule;
	
	@ManyToOne
	private Produit produit;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="carteGrise")
	@JsonIgnore
	private Set<Visite> visites = new HashSet<>();

	@OneToMany(mappedBy = "carteGrise")
	private Set<Control> controls = new HashSet<>();
}
