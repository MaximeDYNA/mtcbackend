package com.catis.model.entity;


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
@NoArgsConstructor @AllArgsConstructor
public class CategorieVehicule extends JournalData{

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String type;

    @OneToMany(mappedBy = "categorieVehicule")
    @JsonIgnore
    private Set<Ligne> lignes;

    @OneToMany(mappedBy = "categorieVehicule")
    @JsonIgnore
    private Set<Lexique> lexique;

    @OneToMany(mappedBy = "categorieVehicule")
    @JsonIgnore
    private Set<Produit> produits;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategorieVehicule)) return false;
        CategorieVehicule that = (CategorieVehicule) o;
        return Objects.equals(getId(), that.getId());
    }

}