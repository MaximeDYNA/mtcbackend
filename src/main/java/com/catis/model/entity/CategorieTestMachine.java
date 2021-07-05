package com.catis.model.entity;

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_categorietestmachine")
@Audited
public class CategorieTestMachine extends JournalData {
    // table pivot entre catégorietest et machine
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategorieTestMachine;


    @ManyToOne(fetch = FetchType.LAZY)
    private CategorieTest categorieTest;

    @ManyToOne(fetch = FetchType.LAZY)
    private Machine machine;

    @OneToMany(mappedBy = "categorieTestMachine")
    private Set<Pattern> patterns;

    @ManyToOne
    private RapportMachine rapportMachine;

    public CategorieTestMachine() {
        // TODO Auto-generated constructor stub
    }

    public CategorieTestMachine(Long idCategorieTestMachine,  CategorieTest categorieTest,
                                Machine machine) {
        this.idCategorieTestMachine = idCategorieTestMachine;

        this.categorieTest = categorieTest;
        this.machine = machine;
    }

    public Long getIdCategorieTestMachine() {
        return idCategorieTestMachine;
    }

    public void setIdCategorieTestMachine(Long idCategorieTestMachine) {
        this.idCategorieTestMachine = idCategorieTestMachine;
    }


    public CategorieTest getCategorieTest() {
        return categorieTest;
    }

    public void setCategorieTest(CategorieTest categorieTest) {
        this.categorieTest = categorieTest;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

}
