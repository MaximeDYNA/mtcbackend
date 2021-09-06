package com.catis.model.entity;

import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_client")
@EntityListeners(AuditingEntityListener.class)
@Audited
@SQLDelete(sql = "UPDATE t_client SET active_status=false WHERE client_id=?")
public class Client extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;
    private String description;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    private Partenaire partenaire;
    @ManyToMany
    private Set<Lexique> lexiques;

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    Set<Vente> ventes;

    public Client() {
    }

    public Client(String description, Partenaire partenaire, Set<Lexique> lexiques, Set<Vente> ventes) {
        this.description = description;
        this.partenaire = partenaire;
        this.lexiques = lexiques;
        this.ventes = ventes;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return Objects.equals(getClientId(), client.getClientId());
    }


}
