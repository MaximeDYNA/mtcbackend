/**
 * 
 */
package com.catis.control.entities;

import java.util.*;

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
@Entity
@Getter @Setter @Audited
@AllArgsConstructor @NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class GieglanFile extends JournalData {

	@Id
	@GeneratedValue(generator = "UUID")
	@Type(type="uuid-char")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	private String name;

	private Date fileCreatedAt;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(255) default 'MEASURE'")
	private FileType type;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(255) default 'INITIALIZED'")
	private StatusType status;

	private Boolean isAccept;

	@ManyToOne(cascade = CascadeType.ALL)
	private Inspection inspection;

	@ManyToOne(cascade = CascadeType.ALL)
	private Machine machine;

	@OneToMany(mappedBy = "gieglanFile", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<ValeurTest> valeurTests = new HashSet<>();

	@OneToMany(mappedBy = "gieglanFile")
	private Set<RapportDeVisite> rapportDeVisites = new HashSet<>();

	@ManyToOne
	private CategorieTest categorieTest;

	@OneToOne(mappedBy = "gieglanFile")
	private MesureVisuel mesureVisuel;

	public enum FileType {
	    MEASURE, MACHINE, CARD_REGISTRATION
	}

	public enum StatusType {
	    INITIALIZED, REJECTED, NOT_DEFINED,  VALIDATED
	}

}