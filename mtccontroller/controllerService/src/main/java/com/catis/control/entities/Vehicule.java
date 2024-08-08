package com.catis.control.entities;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.control.entities.inherited.JournalData;
import lombok.*;

@Entity @Table(name="t_vehicule")
@Getter @Setter @Audited
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Vehicule extends JournalData {

	@Id
	@GeneratedValue(generator = "UUID")
	@Type(type="uuid-char")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID vehiculeId;
	
	private String typeVehicule;
	
	private String carrosserie;

	private double score;
	
	private int placeAssise;
	
	private int puissAdmin; // Puissance Administrative
	
	private int poidsTotalCha; // poids total en charge
	
	private int poidsVide;
	
	private int chargeUtile; // charge utile
	
	
	private String chassis;
	
	private Date premiereMiseEnCirculation;
	
	@ManyToOne
	private Energie energie;
	
	private int cylindre; //cm3
	
	@ManyToOne
	private MarqueVehicule marqueVehicule;
	
	@OneToMany( mappedBy="vehicule")
	private Set<CarteGrise> carteGrises = new HashSet<>();
}
