package com.catis.model.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_categorietestmachine")
@Audited
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class CategorieTestMachine extends JournalData {
    // table pivot entre catégorietest et machine
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID idCategorieTestMachine;


    @ManyToOne(fetch = FetchType.LAZY)
    private CategorieTest categorieTest;

    @ManyToOne(fetch = FetchType.LAZY)
    private Machine machine;

    @OneToMany(mappedBy = "categorieTestMachine")
    private Set<Pattern> patterns;

    @ManyToOne
    private RapportMachine rapportMachine;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategorieTestMachine)) return false;
        CategorieTestMachine that = (CategorieTestMachine) o;
        return Objects.equals(getIdCategorieTestMachine(), that.getIdCategorieTestMachine());
    }

}
