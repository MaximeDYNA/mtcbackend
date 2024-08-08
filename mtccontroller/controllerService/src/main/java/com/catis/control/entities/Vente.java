
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

@Entity @Table(name = "t_vente")
@Getter @Setter @Audited
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Vente extends JournalData {
	@Id
	@GeneratedValue(generator = "UUID")
	@Type(type="uuid-char")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID idVente;
	
	private double montantTotal;
	
	private double montantHT;
	
	private String numFacture;

	private int statut;
	
	@ManyToOne
	private Client client;
	
	@ManyToOne
	private Vendeur vendeur;
	
	@ManyToOne
	private Contact contact;
	
	@ManyToOne
	private SessionCaisse sessionCaisse;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="vente")
	private Set<OperationCaisse> operationCaisse = new HashSet<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="vente")
	private Set<DetailVente> detailventes = new HashSet<>();
	
	@OneToOne
	private Visite visite;
}
