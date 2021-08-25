package com.catis.model.control;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import com.catis.model.entity.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

/**
 * @author AubryYvan
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
public class GieglanFile extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Date fileCreatedAt;

    private Boolean isAccept;

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
    @JsonIgnore
    private Set<ValeurTest> valeurTests;

    @OneToMany(mappedBy = "gieglanFile")
    @JsonIgnore
    private Set<RapportDeVisite> rapportDeVisites;

    @OneToOne(mappedBy = "gieglanFile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private MesureVisuel mesureVisuel;

    @ManyToOne
    @JsonIgnore
    private CategorieTest categorieTest;


    public enum FileType {
        MEASURE, MACHINE, CARD_REGISTRATION
    }

    public enum StatusType {
        INITIALIZED, REJECTED, VALIDATED, NOT_DEFINED
    }

    public GieglanFile(Long id, String name, Date fileCreatedAt, FileType type, StatusType status,
                       Inspection inspection, Machine machine, Set<ValeurTest> valeurTests, Set<RapportDeVisite> rapportDeVisites,
                       CategorieTest categorieTest) {
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
        this.categorieTest = categorieTest;
    }

    public StatusType getStatus() {
        return status;
    }


    public void setStatus(StatusType status) {
        this.status = status;
    }


    public GieglanFile() {

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

    public Boolean getAccept() {
        return isAccept;
    }
    public CategorieTest getCategorieTest() {
        return categorieTest;
    }


    public void setCategorieTest(CategorieTest categorieTest) {
        this.categorieTest = categorieTest;
    }

    public void setAccept(Boolean accept) {
        isAccept = accept;
    }

    public MesureVisuel getMesureVisuel() {
        return mesureVisuel;
    }

    public void setMesureVisuel(MesureVisuel mesureVisuel) {
        this.mesureVisuel = mesureVisuel;
    }
}