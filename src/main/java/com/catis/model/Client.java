package com.catis.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_client")
@EntityListeners(AuditingEntityListener.class)
public class Client extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;
    private String description;

    @ManyToMany
    private Set<Lexique> lexiques;

    @ManyToOne
    private Partenaire partenaire;

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    Set<Vente> ventes;

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    Set<Contact> contact;

    public Set<Contact> getContact() {
        return contact;
    }

    public void setContact(Set<Contact> contact) {
        this.contact = contact;
    }

    public Client() {
    }

    public Client(long clientId, String description, Partenaire partenaire) {

        this.clientId = clientId;
        this.description = description;
        this.partenaire = partenaire;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
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

    public Set<Lexique> getLexiques() {
        return lexiques;
    }

    public void setLexiques(Set<Lexique> lexiques) {
        this.lexiques = lexiques;
    }

}
