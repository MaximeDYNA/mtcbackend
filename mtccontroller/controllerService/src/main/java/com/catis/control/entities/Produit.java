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

@Entity @Table(name = "t_produit")
@Getter @Setter @Audited
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Produit extends JournalData{

	@Id
	@GeneratedValue(generator = "UUID")
	@Type(type="uuid-char")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID produitId;

	private String libelle;
	
	private String description;
	
	private double prix;
	
	private int delaiValidite;
	
	private String img;

	@OneToMany(fetch = FetchType.LAZY, mappedBy="produit")
	private Set<DetailVente> detailVentes = new HashSet<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="produit")
	private Set<CarteGrise> carteGrises = new HashSet<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="produit")
	private Set<TaxeProduit> taxeProduits = new HashSet<>();
	
	@OneToMany( mappedBy="produit")
	private Set<Posales> posales = new HashSet<>();

	@ManyToOne
	private CategorieProduit categorieProduit;

	@ManyToOne
	private CategorieVehicule categorieVehicule;

	@ManyToMany
	private Set<Seuil> seuils = new HashSet<>();

	@OneToMany(mappedBy = "produit")
	private Set<CategorieTestProduit> categorieTestProduits = new HashSet<>();
}
