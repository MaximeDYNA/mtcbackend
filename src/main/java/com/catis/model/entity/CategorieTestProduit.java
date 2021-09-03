package com.catis.model.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.catis.model.control.GieglanFile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategorieTestProduit extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;

    @ManyToOne
    private CategorieTest categorieTest;

    @ManyToOne
    @JsonIgnore
    private Produit produit;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Mesure> mesures;



}