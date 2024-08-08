package com.catis.control.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.control.entities.GieglanFile.FileType;
import com.catis.control.entities.inherited.JournalData;

import lombok.*;

@Entity @Getter @Setter @Audited
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CategorieTestProduit extends JournalData{

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
	@Column(columnDefinition = "varchar(255) default 'MEASURE'")
	private FileType type;

	@ManyToOne
	private CategorieTest categorieTest;

	@ManyToOne
	private Produit produit;

	@ManyToMany(cascade={CascadeType.MERGE,CascadeType.REMOVE, CascadeType.DETACH,CascadeType.REFRESH})
	private Set<Mesure> mesures = new HashSet<>();

	public void addMesure(Mesure mesure) {
		if (!mesures.contains(mesure)) {
			mesures.add(mesure);
			mesure.getCategorieTestProduits().add(this);
		}
	}
}
