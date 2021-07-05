package com.catis.model.entity;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_organisation")
@EntityListeners(AuditingEntityListener.class)
@Audited
@SQLDelete(sql = "UPDATE t_organisation SET active_status=false WHERE organisation_id=?")
public class Organisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long organisationId;
    private String name;
    private String nom;
    private String adress;
    private String tel1;
    private String tel2;
    private boolean parent;

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
    private Set<CategorieTestVehicule> categorieTestVehicules;

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

    public Organisation() {

    }


    public Organisation(Long organisationId, Set<ModeleVehicule> modelVehicule, Set<Caissier> caissier,
                        Set<Controleur> controleur,
                        Set<CategorieTestMachine> categorieTestMachine, Set<Ligne> ligne, Set<Inspection> inspection,
                        Set<Machine> machine, Set<Adresse> adresse, Set<Mesure> mesure, Set<Visite> visite,
                        Set<ValeurTest> valeurTests, Set<Partenaire> partenaires, Set<LigneMachine> ligneMachine,
                        Set<ProprietaireVehicule> proprietaireVehicule, Set<Utilisateur> utilisateurs,
                        Set<Organisation> childOrganisations, Organisation parentOrganisation, Set<Caisse> caisses, String patente,
                        String statutJurique, String numeroDeContribuable) {
        super();
        this.organisationId = organisationId;
        this.modelVehicule = modelVehicule;
        this.caissier = caissier;

        this.controleur = controleur;
        this.categorieTestMachine = categorieTestMachine;
        this.ligne = ligne;
        this.inspection = inspection;
        this.machine = machine;
        this.adresse = adresse;
        this.mesure = mesure;
        this.visite = visite;
        this.valeurTests = valeurTests;
        this.partenaires = partenaires;
        this.ligneMachine = ligneMachine;
        this.proprietaireVehicule = proprietaireVehicule;
        this.utilisateurs = utilisateurs;
        this.childOrganisations = childOrganisations;
        this.parentOrganisation = parentOrganisation;
        this.caisses = caisses;
        this.patente = patente;
        this.statutJurique = statutJurique;
        this.numeroDeContribuable = numeroDeContribuable;
    }


    public Set<Caisse> getCaisses() {
        return caisses;
    }


    public void setCaisses(Set<Caisse> caisses) {
        this.caisses = caisses;
    }


    public Long getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Long organisationId) {
        this.organisationId = organisationId;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getStatutJurique() {
        return statutJurique;
    }

    public void setStatutJurique(String statutJurique) {
        this.statutJurique = statutJurique;
    }

    public String getNumeroDeContribuable() {
        return numeroDeContribuable;
    }

    public void setNumeroDeContribuable(String numeroDeContribuable) {
        this.numeroDeContribuable = numeroDeContribuable;
    }

    public Set<Adresse> getAdresse() {
        return adresse;
    }

    public void setAdresse(Set<Adresse> adresse) {
        this.adresse = adresse;
    }

    public Set<Partenaire> getPartenaires() {
        return partenaires;
    }

    public void setPartenaire(Set<Partenaire> partenaires) {
        this.partenaires = partenaires;
    }



    public Set<Organisation> getChildOrganisations() {
        return childOrganisations;
    }

    public void setChildOrganisations(Set<Organisation> childOrganisations) {
        this.childOrganisations = childOrganisations;
    }

    public Organisation getParentOrganisation() {
        return parentOrganisation;
    }

    public void setParentOrganisation(Organisation parentOrganisation) {
        this.parentOrganisation = parentOrganisation;
    }

    public void setPartenaires(Set<Partenaire> partenaires) {
        this.partenaires = partenaires;
    }

    public Set<Visite> getVisite() {
        return visite;
    }

    public void setVisite(Set<Visite> visite) {
        this.visite = visite;
    }

    public Set<ValeurTest> getValeurTests() {
        return valeurTests;
    }

    public void setValeurTests(Set<ValeurTest> valeurTests) {
        this.valeurTests = valeurTests;
    }

    public Set<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(Set<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    public Set<LigneMachine> getLigneMachine() {
        return ligneMachine;
    }

    public void setLigneMachine(Set<LigneMachine> ligneMachine) {
        this.ligneMachine = ligneMachine;
    }

    public Set<ProprietaireVehicule> getProprietaireVehicule() {
        return proprietaireVehicule;
    }

    public void setProprietaireVehicule(Set<ProprietaireVehicule> proprietaireVehicule) {
        this.proprietaireVehicule = proprietaireVehicule;
    }

    public Set<ModeleVehicule> getModelVehicule() {
        return modelVehicule;
    }

    public void setModelVehicule(Set<ModeleVehicule> modelVehicule) {
        this.modelVehicule = modelVehicule;
    }

    public Set<Caissier> getCaissier() {
        return caissier;
    }

    public void setCaissier(Set<Caissier> caissier) {
        this.caissier = caissier;
    }

    public Set<Controleur> getControleur() {
        return controleur;
    }

    public void setControleur(Set<Controleur> controleur) {
        this.controleur = controleur;
    }

    public Set<CategorieTestMachine> getCategorieTestMachine() {
        return categorieTestMachine;
    }

    public void setCategorieTestMachine(Set<CategorieTestMachine> categorieTestMachine) {
        this.categorieTestMachine = categorieTestMachine;
    }

    public Set<Ligne> getLigne() {
        return ligne;
    }

    public void setLigne(Set<Ligne> ligne) {
        this.ligne = ligne;
    }

    public Set<Inspection> getInspection() {
        return inspection;
    }

    public void setInspection(Set<Inspection> inspection) {
        this.inspection = inspection;
    }

    public Set<Machine> getMachine() {
        return machine;
    }

    public void setMachine(Set<Machine> machine) {
        this.machine = machine;
    }

    public Set<Mesure> getMesure() {
        return mesure;
    }

    public void setMesure(Set<Mesure> mesure) {
        this.mesure = mesure;
    }

    public Set<CarteGrise> getCarteGrises() {
        return carteGrises;
    }

    public void setCarteGrises(Set<CarteGrise> carteGrises) {
        this.carteGrises = carteGrises;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    protected void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public boolean isActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Set<Lexique> getLexiques() {
        return lexiques;
    }

    public void setLexiques(Set<Lexique> lexiques) {
        this.lexiques = lexiques;
    }

    public Set<Pattern> getPatterns() {
        return patterns;
    }

    public void setPatterns(Set<Pattern> patterns) {
        this.patterns = patterns;
    }

    public Set<Pays> getPays() {
        return pays;
    }

    public void setPays(Set<Pays> pays) {
        this.pays = pays;
    }

    public Set<Posales> getPosales() {
        return posales;
    }

    public void setPosales(Set<Posales> posales) {
        this.posales = posales;
    }

    public Set<Produit> getProduits() {
        return produits;
    }

    public void setProduits(Set<Produit> produits) {
        this.produits = produits;
    }

    public Set<ProprietaireVehicule> getProprietaireVehicules() {
        return proprietaireVehicules;
    }

    public void setProprietaireVehicules(Set<ProprietaireVehicule> proprietaireVehicules) {
        this.proprietaireVehicules = proprietaireVehicules;
    }

    public Set<RapportDeVisite> getRapportDeVisites() {
        return rapportDeVisites;
    }

    public void setRapportDeVisites(Set<RapportDeVisite> rapportDeVisites) {
        this.rapportDeVisites = rapportDeVisites;
    }

    public Set<RapportMachine> getRapportMachines() {
        return rapportMachines;
    }

    public void setRapportMachines(Set<RapportMachine> rapportMachines) {
        this.rapportMachines = rapportMachines;
    }

    public Set<SessionCaisse> getSessionCaisses() {
        return sessionCaisses;
    }

    public void setSessionCaisses(Set<SessionCaisse> sessionCaisses) {
        this.sessionCaisses = sessionCaisses;
    }

    public Set<Seuil> getSeuils() {
        return seuils;
    }

    public void setSeuils(Set<Seuil> seuils) {
        this.seuils = seuils;
    }

    public Set<StatutCode> getStatutCodes() {
        return statutCodes;
    }

    public void setStatutCodes(Set<StatutCode> statutCodes) {
        this.statutCodes = statutCodes;
    }

    public Set<Taxe> getTaxes() {
        return taxes;
    }

    public void setTaxes(Set<Taxe> taxes) {
        this.taxes = taxes;
    }

    public Set<TaxeProduit> getTaxeProduits() {
        return taxeProduits;
    }

    public void setTaxeProduits(Set<TaxeProduit> taxeProduits) {
        this.taxeProduits = taxeProduits;
    }

    public Set<DetailVente> getDetailVentes() {
        return detailVentes;
    }

    public void setDetailVentes(Set<DetailVente> detailVentes) {
        this.detailVentes = detailVentes;
    }



    public Set<DivisionPays> getDivisionPays() {
        return divisionPays;
    }

    public void setDivisionPays(Set<DivisionPays> divisionPays) {
        this.divisionPays = divisionPays;
    }

    public Set<Energie> getEnergies() {
        return energies;
    }

    public void setEnergies(Set<Energie> energies) {
        this.energies = energies;
    }

    public Set<Formule> getFormules() {
        return formules;
    }

    public void setFormules(Set<Formule> formules) {
        this.formules = formules;
    }

    public Set<GieglanFile> getGieglanFiles() {
        return gieglanFiles;
    }

    public void setGieglanFiles(Set<GieglanFile> gieglanFiles) {
        this.gieglanFiles = gieglanFiles;
    }

    public Set<Hold> getHolds() {
        return holds;
    }

    public void setHolds(Set<Hold> holds) {
        this.holds = holds;
    }

    public Set<CategorieTestMachine> getCategorieTestMachines() {
        return categorieTestMachines;
    }

    public void setCategorieTestMachines(Set<CategorieTestMachine> categorieTestMachines) {
        this.categorieTestMachines = categorieTestMachines;
    }

    public Set<CategorieTestVehicule> getCategorieTestVehicules() {
        return categorieTestVehicules;
    }

    public void setCategorieTestVehicules(Set<CategorieTestVehicule> categorieTestVehicules) {
        this.categorieTestVehicules = categorieTestVehicules;
    }

    public Set<CategorieVehicule> getCategorieVehicules() {
        return categorieVehicules;
    }

    public void setCategorieVehicules(Set<CategorieVehicule> categorieVehicules) {
        this.categorieVehicules = categorieVehicules;
    }

    public Set<Classification> getClassifications() {
        return classifications;
    }

    public void setClassifications(Set<Classification> classifications) {
        this.classifications = classifications;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }

    public Set<Constructor> getConstructors() {
        return constructors;
    }

    public void setConstructors(Set<Constructor> constructors) {
        this.constructors = constructors;
    }

    public Set<ConstructorModel> getConstructorModels() {
        return constructorModels;
    }

    public void setConstructorModels(Set<ConstructorModel> constructorModels) {
        this.constructorModels = constructorModels;
    }

    public Set<Control> getControls() {
        return controls;
    }

    public void setControls(Set<Control> controls) {
        this.controls = controls;
    }

    public Set<CategorieProduit> getCategorieProduits() {
        return categorieProduits;
    }

    public void setCategorieProduits(Set<CategorieProduit> categorieProduits) {
        this.categorieProduits = categorieProduits;
    }

    public Set<CategorieTest> getCategorieTests() {
        return categorieTests;
    }

    public void setCategorieTests(Set<CategorieTest> categorieTests) {
        this.categorieTests = categorieTests;
    }

    public Set<MesureVisuel> getMesureVisuels() {
        return mesureVisuels;
    }

    public void setMesureVisuels(Set<MesureVisuel> mesureVisuels) {
        this.mesureVisuels = mesureVisuels;
    }

    public Set<OperationCaisse> getOperationCaisses() {
        return operationCaisses;
    }

    public void setOperationCaisses(Set<OperationCaisse> operationCaisses) {
        this.operationCaisses = operationCaisses;
    }

    public Set<ModeleVehicule> getModeleVehicules() {
        return modeleVehicules;
    }

    public void setModeleVehicules(Set<ModeleVehicule> modeleVehicules) {
        this.modeleVehicules = modeleVehicules;
    }

    public Set<MarqueVehicule> getMarqueVehicules() {
        return marqueVehicules;
    }

    public void setMarqueVehicules(Set<MarqueVehicule> marqueVehicules) {
        this.marqueVehicules = marqueVehicules;
    }

    public Set<Vehicule> getVehicules() {
        return vehicules;
    }

    public void setVehicules(Set<Vehicule> vehicules) {
        this.vehicules = vehicules;
    }

    public Set<Vente> getVentes() {
        return ventes;
    }

    public void setVentes(Set<Vente> ventes) {
        this.ventes = ventes;
    }

    public Set<VerbalProcess> getVerbalProcesses() {
        return verbalProcesses;
    }

    public void setVerbalProcesses(Set<VerbalProcess> verbalProcesses) {
        this.verbalProcesses = verbalProcesses;
    }

    public Set<VersionLexique> getVersionLexiques() {
        return versionLexiques;
    }

    public void setVersionLexiques(Set<VersionLexique> versionLexiques) {
        this.versionLexiques = versionLexiques;
    }

    public Set<Visite> getVisites() {
        return visites;
    }

    public void setVisites(Set<Visite> visites) {
        this.visites = visites;
    }

    public Set<Vendeur> getVendeurs() {
        return vendeurs;
    }

    public void setVendeurs(Set<Vendeur> vendeurs) {
        this.vendeurs = vendeurs;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }
}
