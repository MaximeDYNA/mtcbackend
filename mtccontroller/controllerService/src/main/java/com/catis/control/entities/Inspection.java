package com.catis.control.entities;

import java.util.*;

import javax.persistence.*;

import lombok.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.control.entities.inherited.JournalData;

@Entity @Table(name="t_inspection")
@Getter @Setter @Audited
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Inspection extends JournalData {

	@Id
	@GeneratedValue(generator = "UUID")
	@Type(type="uuid-char")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID idInspection;

	private Date dateDebut;
	
	private Date dateFin;

	private boolean visibleToTab = true;

	private String bestPlate;

	private double distancePercentage;
	
	private String signature;

	private double kilometrage;
	
	private String chassis;
	
	private int essieux;
	
	private String position;

	private UUID visiteIdReseted;
	
	@ManyToOne
	private Controleur controleur;

	@ManyToOne
	private Ligne ligne;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Visite visite;

	@OneToMany(mappedBy = "inspection", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<GieglanFile> gieglanFiles = new HashSet<>();

	@ManyToOne
	private Produit produit;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Lexique> lexiques;
}
