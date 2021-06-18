package com.catis.model;

import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_controleur")
@EntityListeners(AuditingEntityListener.class)
@Audited
@SQLDelete(sql = "UPDATE t_controleur SET active_status=false WHERE id_controleur=?")
public class Controleur extends JournalData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idControleur;
    private String agremment;
    private int score=0;

    @OneToOne(optional = true) // id utilisateur optionel
    private Utilisateur utilisateur;

    @OneToOne (cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    private Partenaire partenaire;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "controleur")
    @JsonIgnore
    private Set<Inspection> inspections;

    public Controleur() {

    }


    public Controleur(Long idControleur, String agremment, int score, Utilisateur utilisateur, Partenaire partenaire,
                       Set<Inspection> inspections) {
        super();
        this.idControleur = idControleur;
        this.agremment = agremment;
        this.score = score;
        this.utilisateur = utilisateur;
        this.partenaire = partenaire;

        this.inspections = inspections;
    }


    public Long getIdControleur() {
        return idControleur;
    }

    public void setIdControleur(Long idControleur) {
        this.idControleur = idControleur;
    }

    public String getAgremment() {
        return agremment;
    }

    public void setAgremment(String agremment) {
        this.agremment = agremment;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Set<Inspection> getInspections() {
        return inspections;
    }

    public void setInspections(Set<Inspection> inspections) {
        this.inspections = inspections;
    }

    public Partenaire getPartenaire() {
        return partenaire;
    }

    public void setPartenaire(Partenaire partenaire) {
        this.partenaire = partenaire;
    }




}
