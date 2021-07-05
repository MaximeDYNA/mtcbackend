package com.catis.model.entity;

import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_contact")
@EntityListeners(AuditingEntityListener.class)
@Audited
@SQLDelete(sql = "UPDATE t_contact SET active_status=false WHERE contact_id=?")
public class Contact extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactId;

    private String description;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    private Partenaire partenaire;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contact")
    @JsonIgnore
    private Set<Vente> ventes;

    public Contact() {

    }

    public Contact(Long contactId, String description, Partenaire partenaire, Set<Vente> ventes) {
        this.contactId = contactId;
        this.description = description;
        this.partenaire = partenaire;
        this.ventes = ventes;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Partenaire getPartenaire() {
        return partenaire;
    }

    public void setPartenaire(Partenaire partenaire) {
        this.partenaire = partenaire;
    }

    public Set<Vente> getVentes() {
        return ventes;
    }

    public void setVentes(Set<Vente> ventes) {
        this.ventes = ventes;
    }



}
