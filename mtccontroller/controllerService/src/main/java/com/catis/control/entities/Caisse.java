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

@Entity @Table(name = "t_caisse")
@Getter @Setter @Audited
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Caisse extends JournalData {
	@Id
	@GeneratedValue(generator = "UUID")
	@Type(type="uuid-char")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID caisseId;

	private String libelle;
	
	private String description;
	
	@OneToMany(mappedBy = "caisse")
	private Set<Caissier> caissiers = new HashSet<>();
}
