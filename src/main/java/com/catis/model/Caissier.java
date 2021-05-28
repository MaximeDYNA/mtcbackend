package com.catis.model;

import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_caissier")
@Audited
@SQLDelete(sql = "UPDATE t_caissier SET active_status=false WHERE caissier_id=?")
public class Caissier extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long caissierId;

    private String codeCaissier;


    @ManyToOne(cascade = CascadeType.PERSIST)
    private Partenaire partenaire;

    @OneToOne (cascade = CascadeType.PERSIST)
    private Caisse caisse;

    @ManyToOne(optional = true,  cascade = CascadeType.PERSIST) // id utilisateur optionel
    private Utilisateur user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "caissier")
    @JsonIgnore
    private Set<SessionCaisse> sessionCaisses;

    public Caissier() {
    }


    public Caissier(Long caissierId, String codeCaissier, Partenaire partenaire,
                    Caisse caisse, Utilisateur user, Set<SessionCaisse> sessionCaisses) {
        super();
        this.caissierId = caissierId;
        this.codeCaissier = codeCaissier;

        this.partenaire = partenaire;
        this.caisse = caisse;
        this.user = user;
        this.sessionCaisses = sessionCaisses;
    }


    public Long getCaissierId() {
        return caissierId;
    }


    public void setCaissierId(Long caissierId) {
        this.caissierId = caissierId;
    }


    public String getCodeCaissier() {
        return codeCaissier;
    }

    public void setCodeCaissier(String codeCaissier) {
        this.codeCaissier = codeCaissier;
    }

    public Partenaire getPartenaire() {
        return partenaire;
    }

    public void setPartenaire(Partenaire partenaire) {
        this.partenaire = partenaire;
    }

    public Utilisateur getUser() {
        return user;
    }

    public void setUser(Utilisateur user) {
        this.user = user;
    }


    public Caisse getCaisse() {
        return caisse;
    }


    public void setCaisse(Caisse caisse) {
        this.caisse = caisse;
    }


    public Set<SessionCaisse> getSessionCaisses() {
        return sessionCaisses;
    }


    public void setSessionCaisses(Set<SessionCaisse> sessionCaisses) {
        this.sessionCaisses = sessionCaisses;
    }


}
