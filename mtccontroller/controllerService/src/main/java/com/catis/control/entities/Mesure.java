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

@Entity @Table(name = "t_mesure")
@Getter @Setter @Audited
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Mesure extends JournalData {

	@Id
	@GeneratedValue(generator = "UUID")
	@Type(type="uuid-char")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID idMesure;

	private String code;

	private String description;

	@ManyToOne
	private Formule formule;

	@ManyToMany(cascade={CascadeType.MERGE,CascadeType.REMOVE, CascadeType.DETACH,CascadeType.REFRESH}, mappedBy = "mesures")
	private Set<CategorieTestProduit> categorieTestProduits = new HashSet<>();
	
	@OneToMany(mappedBy = "mesure")
	private Set<ValeurTest> valeurTests = new HashSet<>();
}