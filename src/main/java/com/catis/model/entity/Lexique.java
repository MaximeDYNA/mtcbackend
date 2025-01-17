package com.catis.model.entity;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
@Getter @Setter
@AllArgsConstructor
public class Lexique extends JournalData {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    private String libelle;

    private String code;

    private Boolean Visuel;


    @ManyToMany(mappedBy = "lexiques",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Client> clients;

    @OneToMany(mappedBy = "lexique", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Seuil> seuils;

    @ManyToOne(fetch = FetchType.LAZY)
    private CategorieVehicule categorieVehicule;

    @ManyToOne(fetch = FetchType.LAZY)
    private VersionLexique versionLexique;

    @OneToMany(mappedBy = "parent",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Lexique> childs;


    @ManyToOne(fetch = FetchType.LAZY)
    private Lexique parent;

    @ManyToMany(mappedBy = "lexiques",fetch = FetchType.LAZY)
    private List<Inspection> inspections;

    @ManyToOne(fetch = FetchType.LAZY)
    private Classification classification;

    private Boolean haschild;

    public Lexique() {
        super();
        // TODO Auto-generated constructor stub
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lexique)) return false;
        Lexique lexique = (Lexique) o;
        return Objects.equals(getId(), lexique.getId());
    }

}