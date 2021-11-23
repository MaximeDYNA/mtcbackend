package com.catis.objectTemporaire;

import java.util.UUID;

public class CategorieproduitProduitPOJO {
    private UUID categorieProduitId;
    private String libelle;
    private String description;
    private UUID organisation;

    public CategorieproduitProduitPOJO() {
    }

    public CategorieproduitProduitPOJO(UUID categorieProduitId, String libelle, String description, UUID organisation) {
        this.categorieProduitId = categorieProduitId;
        this.libelle = libelle;
        this.description = description;
        this.organisation = organisation;
    }

    public UUID getCategorieProduitId() {
        return categorieProduitId;
    }

    public void setCategorieProduitId(UUID categorieProduitId) {
        this.categorieProduitId = categorieProduitId;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getOrganisation() {
        return organisation;
    }

    public void setOrganisation(UUID organisation) {
        this.organisation = organisation;
    }
}
