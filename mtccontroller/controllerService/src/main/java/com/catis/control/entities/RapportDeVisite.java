package com.catis.control.entities;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.control.entities.inherited.JournalData;
import lombok.*;

import java.util.UUID;

/**
 * @author AubryYvan
 */
@Entity @Getter @Setter @Audited
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class RapportDeVisite extends JournalData {

	@Id
	@GeneratedValue(generator = "UUID")
	@Type(type="uuid-char")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	private String result;

	private boolean decision;

	private String codeMessage;

	@ManyToOne(cascade = CascadeType.ALL)
	private Visite visite;

	@ManyToOne(cascade = CascadeType.ALL)
	private Seuil seuil;

	@ManyToOne
	private GieglanFile gieglanFile;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private VerbalProcess verbalProcess;
}
