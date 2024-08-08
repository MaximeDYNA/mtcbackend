package com.catis.control.entities;

import java.util.Set;
import java.util.UUID;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.control.entities.inherited.JournalData;
import lombok.*;

@Entity @Table(name = "t_caissier")
@Getter @Setter @Audited
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Caissier extends JournalData {

	@Id
	@GeneratedValue(generator = "UUID")
	@Type(type="uuid-char")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID caissierId;

	private String codeCaissier;

	private String nom;

	@ManyToOne
	private Partenaire partenaire;

	@ManyToOne(optional = true)
	private Utilisateur user;

	@ManyToOne
	private Caisse caisse;
	
	@OneToMany(mappedBy = "caissier")
	private Set<SessionCaisse> sessionCaisses;
}