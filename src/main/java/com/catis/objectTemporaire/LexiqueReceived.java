package com.catis.objectTemporaire;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LexiqueReceived {

    private UUID id;
    private String nom;
    private String version;
    private List<LexiquePOJO> rows;

    public LexiqueReceived() {
        super();
        // TODO Auto-generated constructor stub
    }

    public LexiqueReceived(String nom, List<LexiquePOJO> rows) {
        super();
        this.nom = nom;
        this.rows = rows;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<LexiquePOJO> getRows() {
        return rows;
    }

    public void setRows(List<LexiquePOJO> rows) {
        this.rows = rows;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
