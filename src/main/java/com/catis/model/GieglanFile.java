package com.catis.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

/**
 * @author AubryYvan
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
public class GieglanFile extends JournalData {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private Date fileCreatedAt;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(255) default 'MEASURE'")
	private FileType type;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(255) default 'INITIALIZED'")
	private StatusType status;

	@ManyToOne(cascade = CascadeType.ALL)
	private Inspection inspection;

	@ManyToOne(cascade = CascadeType.ALL)
	private Machine machine;

	@OneToMany(mappedBy = "gieglanFile", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<ValeurTest> valeurTests;

	@OneToMany(mappedBy = "gieglanFile")
	private Set<RapportDeVisite> rapportDeVisites;

	@ManyToOne
	private CategorieTestVehicule categorieTestVehicule;

	public enum FileType {
	    MEASURE, MACHINE, CARD_REGISTRATION
	}

	public enum StatusType {
	    INITIALIZED, REJECTED, VALIDATED
	}

	public GieglanFile() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GieglanFile(Long id, String name, Date fileCreatedAt, FileType type, StatusType status,
			Inspection inspection, Machine machine, Set<ValeurTest> valeurTests, Set<RapportDeVisite> rapportDeVisites,
			CategorieTestVehicule categorieTestVehicule) {
		super();
		this.id = id;
		this.name = name;
		this.fileCreatedAt = fileCreatedAt;
		this.type = type;
		this.status = status;
		this.inspection = inspection;
		this.machine = machine;
		this.valeurTests = valeurTests;
		this.rapportDeVisites = rapportDeVisites;
		this.categorieTestVehicule = categorieTestVehicule;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getFileCreatedAt() {
		return fileCreatedAt;
	}

	public void setFileCreatedAt(Date fileCreatedAt) {
		this.fileCreatedAt = fileCreatedAt;
	}

	public FileType getType() {
		return type;
	}

	public void setType(FileType type) {
		this.type = type;
	}

	public StatusType getStatus() {
		return status;
	}

	public void setStatus(StatusType status) {
		this.status = status;
	}

	public Inspection getInspection() {
		return inspection;
	}

	public void setInspection(Inspection inspection) {
		this.inspection = inspection;
	}

	public Machine getMachine() {
		return machine;
	}

	public void setMachine(Machine machine) {
		this.machine = machine;
	}

	public Set<ValeurTest> getValeurTests() {
		return valeurTests;
	}

	public void setValeurTests(Set<ValeurTest> valeurTests) {
		this.valeurTests = valeurTests;
	}

	public Set<RapportDeVisite> getRapportDeVisites() {
		return rapportDeVisites;
	}

	public void setRapportDeVisites(Set<RapportDeVisite> rapportDeVisites) {
		this.rapportDeVisites = rapportDeVisites;
	}

	public CategorieTestVehicule getCategorieTestVehicule() {
		return categorieTestVehicule;
	}

	public void setCategorieTestVehicule(CategorieTestVehicule categorieTestVehicule) {
		this.categorieTestVehicule = categorieTestVehicule;
	}

	
}