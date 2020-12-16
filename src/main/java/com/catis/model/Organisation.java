package com.catis.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

@Entity
@Table(name = "t_organisation")
@EntityListeners(AuditingEntityListener.class)
public class Organisation extends JournalData {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long organisationId;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="organisation")
	private Set<ModeleVehicule> modelVehicule;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="organisation")
	private Set<Caissier> caissier;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="organisation")
	private Set<CaissierCaisse> caissierCaisse;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="organisation")
	private Set<Controleur> controleur;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="organisation")
	private Set<CategorieTestMachine> categorieTestMachine;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="organisation")
	private Set<Ligne> ligne;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="organisation")
	private Set<Inspection> inspection;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="organisation")
	private Set<Machine> machine;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="organisation")
	private Set<Adresse> adresse;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="organisation")
	private Set<Mesure> mesure;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="organisation")
	private Set<Visite> visite;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="organisation")
	private Set<ValeurTest> valeurTests;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="organisation")
	private Set<Partenaire> partenaires;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="organisation")
	private Set<LigneMachine> ligneMachine;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="organisation")
	private Set<ProprietaireVehicule> proprietaireVehicule;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="organisation")
	private Set<Utilisateur> utilisateurs;
	
	@OneToMany(mappedBy = "parentOrganisation")
	private Set<Organisation> childOrganisations; 

	@ManyToOne
	private Organisation parentOrganisation;
	
	private String patente;
	private String statutJurique;
	private String numeroDeContribuable;
	
	public Organisation() {
		
	}


	public Organisation(Long organisationId, Set<ModeleVehicule> modelVehicule, Set<Caissier> caissier,
			Set<CaissierCaisse> caissierCaisse, Set<Controleur> controleur,
			Set<CategorieTestMachine> categorieTestMachine, Set<Ligne> ligne, Set<Inspection> inspection,
			Set<Machine> machine, Set<Adresse> adresse, Set<Mesure> mesure, Set<Visite> visite,
			Set<ValeurTest> valeurTests, Set<Partenaire> partenaires, Set<LigneMachine> ligneMachine,
			Set<ProprietaireVehicule> proprietaireVehicule, Set<Utilisateur> utilisateurs,
			Set<Organisation> childOrganisations, Organisation parentOrganisation, String patente, String statutJurique,
			String numeroDeContribuable) {
	
		this.organisationId = organisationId;
		this.modelVehicule = modelVehicule;
		this.caissier = caissier;
		this.caissierCaisse = caissierCaisse;
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
		this.patente = patente;
		this.statutJurique = statutJurique;
		this.numeroDeContribuable = numeroDeContribuable;
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










	public Set<Utilisateur> getUtilisateur() {
		return utilisateurs;
	}










	public void setUtilisateur(Set<Utilisateur> utilisateur) {
		this.utilisateurs = utilisateur;
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


	public Set<CaissierCaisse> getCaissierCaisse() {
		return caissierCaisse;
	}


	public void setCaissierCaisse(Set<CaissierCaisse> caissierCaisse) {
		this.caissierCaisse = caissierCaisse;
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



	
}
