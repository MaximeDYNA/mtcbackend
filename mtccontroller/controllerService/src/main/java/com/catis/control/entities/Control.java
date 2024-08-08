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

import com.catis.control.entities.GieglanFile.StatusType;
import com.catis.control.entities.inherited.JournalData;
import lombok.*;

@Entity
@Getter @Setter @Audited
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Control extends JournalData {

	@Id
	@GeneratedValue(generator = "UUID")
	@Type(type="uuid-char")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Enumerated(EnumType.STRING)
	private StatusType status;

	private LocalDateTime contreVDelayAt;

	private LocalDateTime validityAt;

	@ManyToOne
	private CarteGrise carteGrise;

	@OneToMany(mappedBy = "control")
	private Set<Visite> visites = new HashSet<>();
}