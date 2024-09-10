package com.catis.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import com.catis.model.control.Control;
import com.catis.model.control.GieglanFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "t_organisation")
@EntityListeners(AuditingEntityListener.class)
@Audited
@SQLDelete(sql = "UPDATE t_organisation SET active_status=false WHERE id=?")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Organisation implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID organisationId;
    private String name;
    private String nom;
    private String adress;
    private String tel1;
    private String tel2;
    private boolean parent;
    private boolean conformity = false;
    private double score;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<ModeleVehicule> modelVehicule;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Caissier> caissier;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Controleur> controleur;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Camera> cameras ;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Lexique> lexiques;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Pattern> patterns;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Pays> pays;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Posales> posales;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Produit> produits;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<ProprietaireVehicule> proprietaireVehicules;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<RapportDeVisite> rapportDeVisites;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<RapportMachine> rapportMachines;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<SessionCaisse> sessionCaisses;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Seuil> seuils;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<StatutCode> statutCodes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Taxe> taxes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<TaxeProduit> taxeProduits;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<DetailVente> detailVentes;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<DivisionPays> divisionPays;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Energie> energies;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Formule> formules;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<GieglanFile> gieglanFiles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Hold> holds;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<CategorieTestMachine> categorieTestMachine;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Ligne> ligne;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Inspection> inspection;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<CategorieTestMachine> categorieTestMachines;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<CategorieTestProduit> categorieTestVehicules;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<CategorieVehicule> categorieVehicules;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Classification> classifications;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Client> clients;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Constructor> constructors;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<ConstructorModel> constructorModels;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Control> controls;



    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<CategorieProduit> categorieProduits;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<CategorieTest> categorieTests;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Machine> machine;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Adresse> adresse;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Mesure> mesure;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Visite> visite;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<MesureVisuel> mesureVisuels;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<OperationCaisse> operationCaisses;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<ModeleVehicule> modeleVehicules;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<ValeurTest> valeurTests;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Partenaire> partenaires;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<CarteGrise> carteGrises;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<LigneMachine> ligneMachine;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<MarqueVehicule> marqueVehicules;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Vehicule> vehicules;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<ProprietaireVehicule> proprietaireVehicule;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Utilisateur> utilisateurs;

    @OneToMany(mappedBy = "parentOrganisation")
    @JsonIgnore
    private Set<Organisation> childOrganisations;

    @OneToMany(mappedBy = "organisation")
    @JsonIgnore
    private Set<Vente> ventes;

    @OneToMany(mappedBy = "organisation")
    @JsonIgnore
    private Set<VerbalProcess> verbalProcesses;

    @OneToMany(mappedBy = "organisation")
    @JsonIgnore
    private Set<VersionLexique> versionLexiques;

    @OneToMany(mappedBy = "organisation")
    @JsonIgnore
    private Set<Visite> visites;

    @ManyToOne
    @JsonIgnore
    private Organisation parentOrganisation;

    @OneToMany(mappedBy = "organisation")
    @JsonIgnore
    private Set<Caisse> caisses;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @JsonIgnore
    private Set<Vendeur> vendeurs;

    @Column(name = "created_date", updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Column(columnDefinition = "bit default 1")
    private boolean activeStatus = true;

    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;

    @Column(name = "modified_by")
    @LastModifiedBy
    private String modifiedBy;


    private String patente;
    private String statutJurique;
    private String numeroDeContribuable;
    private String lang;
    private String region;
    private String devise;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Organisation)) return false;
        Organisation that = (Organisation) o;
        return Objects.equals(getOrganisationId(), that.getOrganisationId());
    }


}
