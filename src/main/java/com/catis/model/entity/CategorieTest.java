package com.catis.model.entity;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import com.catis.model.control.GieglanFile;
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
@Table(name = "t_categorietest")
@Audited
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class CategorieTest extends JournalData {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID idCategorieTest;

    private String libelle;

    private String description;

    private String icon;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "categorieTest")
    @JsonIgnore
    private Set<CategorieTestMachine> categorieTestMachines;

    @OneToMany(mappedBy = "categorieTest")
    @JsonIgnore
    private Set<CategorieTestProduit> categorieTestVehicules;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "categorieTest")
    @JsonIgnore
    private Set<GieglanFile> gieglanFiles;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategorieTest)) return false;
        CategorieTest that = (CategorieTest) o;
        return Objects.equals(getIdCategorieTest(), that.getIdCategorieTest());
    }

}
