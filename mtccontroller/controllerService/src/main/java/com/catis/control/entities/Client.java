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

@Entity @Table(name = "t_client")
@Getter @Setter @Audited
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Client extends JournalData {

	@Id
	@GeneratedValue(generator = "UUID")
	@Type(type="uuid-char")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID clientId;

	private String description;

	@ManyToOne
	private Partenaire partenaire;

	@OneToMany(mappedBy="client")
	private Set<Vente> ventes = new HashSet<>();
	
	@ManyToMany
	private Set<Lexique> lexiques;
}