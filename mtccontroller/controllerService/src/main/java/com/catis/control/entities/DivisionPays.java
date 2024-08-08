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

@Entity @Table(name = "t_divisionpays")
@Getter @Setter @Audited
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class DivisionPays extends JournalData {

	@Id
	@GeneratedValue(generator = "UUID")
	@Type(type="uuid-char")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID divisionPaysId;
	
	private String libelle;
	
	private String description;
	
	@ManyToOne
	private DivisionPays parent;
	
	@OneToMany(mappedBy = "parent")
	private Set<DivisionPays> childs = new HashSet<>();

	@ManyToOne
	private Pays pays;

	@OneToMany(mappedBy = "divisionPays")
	private Set<Adresse> adresses = new HashSet<>();

}
