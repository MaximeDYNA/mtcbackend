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

@Entity @Table(name="t_machine")
@Getter @Setter @Audited
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Machine extends JournalData {
	@Id
	@GeneratedValue(generator = "UUID")
	@Type(type="uuid-char")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID idMachine;
	
	private String numSerie;
	
	private String fabriquant;
	
	private String model;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="machine")
	private Set<LigneMachine> ligneMachines = new HashSet<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy="machine")
	private Set<CategorieTestMachine> categorieTestMachine = new HashSet<>();

	@OneToMany(mappedBy = "machine", cascade = CascadeType.ALL)
	private Set<GieglanFile> gieglanFiles = new HashSet<>();

	@ManyToOne
	private ConstructorModel constructorModel;
}