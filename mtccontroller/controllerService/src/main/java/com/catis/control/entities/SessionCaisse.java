package com.catis.control.entities;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.control.entities.inherited.JournalData;
import lombok.*;

@Entity @Table(name="t_sessioncaisse")
@Getter @Setter @Audited
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SessionCaisse extends JournalData {

	@Id
	@GeneratedValue(generator = "UUID")
	@Type(type="uuid-char")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID sessionCaisseId;

	private Date dateHeureOuverture;
	
	private Date dateHeureFermeture;
		
	private double montantOuverture;
	
	private double montantfermeture;
	
	private boolean active;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="sessionCaisse")
	private Set<OperationCaisse> operationCaisses = new HashSet<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="sessionCaisse")
	private Set<Vente> ventes = new HashSet<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="sessionCaisse")
	private Set<Hold> holds = new HashSet<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="sessionCaisse")
	private Set<Posales> posales = new HashSet<>();
	
	@ManyToOne
	private Caissier caissier;

}
