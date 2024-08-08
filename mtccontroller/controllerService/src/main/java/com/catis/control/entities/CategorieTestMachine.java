package com.catis.control.entities;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.control.entities.inherited.JournalData;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity @Table(name = "t_categorietestmachine")
@Getter @Setter @Audited
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CategorieTestMachine extends JournalData {

	@Id
	@GeneratedValue(generator = "UUID")
	@Type(type="uuid-char")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID idCategorieTestMachine;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private CategorieTest categorieTest;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Machine machine;

	@OneToMany(mappedBy = "categorieTestMachine")
	private Set<Pattern> patterns;

	@ManyToOne
	private RapportMachine rapportMachine;
}
