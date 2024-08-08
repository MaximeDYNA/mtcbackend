/**
 * 
 */
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

/**
 * @author AubryYvan
 */
@Entity @Getter @Setter @Audited
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Seuil extends JournalData {

	@Id
	@Column(name = "id", updatable = false, nullable = false)
	private String id;

	private double value;

	private String operande;

	private String codeMessage;

	private boolean decision;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "seuil")
	private Set<RapportDeVisite> rapportDeVisites = new HashSet<>();

	@ManyToOne
	private Formule formule;

	@ManyToOne
	private Lexique lexique;

	@ManyToMany(mappedBy = "seuils")
	private Set<Produit> produits = new HashSet<>();
}
