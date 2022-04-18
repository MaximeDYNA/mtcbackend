package com.catis.model.entity;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
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
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String libelle;

    private String code;

    private Boolean Visuel;

    @ManyToMany(mappedBy = "lexiques")
    @JsonIgnore
    private Set<Client> clients;

    @OneToMany(mappedBy = "lexique")
    @JsonIgnore
    private Set<Seuil> seuils;

    @ManyToOne
    private CategorieVehicule categorieVehicule;

    @ManyToOne
    private VersionLexique versionLexique;

    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    private Set<Lexique> childs;


    @ManyToOne
    private Lexique parent;

    @ManyToMany(mappedBy = "lexiques")
    private List<Inspection> inspections;

    @ManyToOne
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