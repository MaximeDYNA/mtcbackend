package com.catis.control.entities;

import java.time.LocalDateTime;
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

@Entity @Table(name="t_visite")
@Getter @Setter @Audited
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Visite extends JournalData {

	@Id
	@GeneratedValue(generator = "UUID")
	@Type(type="uuid-char")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID idVisite;

	private boolean contreVisite;

	private LocalDateTime dateDebut;

	private LocalDateTime dateFin;

	private int statut;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(255) default 'PENDING'")
	private TestResult ripage;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(255) default 'PENDING'")
	private TestResult suspension;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(255) default 'PENDING'")
	private TestResult freinage;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(255) default 'PENDING'")
	private TestResult pollution;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(255) default 'PENDING'")
	private TestResult reglophare;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(255) default 'PENDING'")
	private TestResult visuel;

	public enum TestResult{
		PENDING, SUCCESS, ERROR
	};

	private int isConform;
	private String certidocsId;


	@Column(columnDefinition = "bit default 1")
	private boolean encours;

	@ManyToOne
	private Caissier caissier;

	@ManyToOne
	private CarteGrise carteGrise;

	@OneToOne(mappedBy = "visite", cascade = CascadeType.ALL)
	private Inspection inspection;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "visite")
	private Set<RapportDeVisite> rapportDeVisites = new HashSet<>();

	@ManyToOne(cascade = CascadeType.ALL)
	private Control control;
	
	@OneToOne(mappedBy = "visite", fetch = FetchType.LAZY)
	private VerbalProcess verbalProcess;
}
