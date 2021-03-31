package com.catis.objectTemporaire;

public class CategorieproduitProduitPOJO {
    private Long categorieProduitId;
    private String libelle;
    private String description;
    private Long organisation;

    public CategorieproduitProduitPOJO() {
    }

    public CategorieproduitProduitPOJO(Long categorieProduitId, String libelle, String description, Long organisation) {
        this.categorieProduitId = categorieProduitId;
        this.libelle = libelle;
        this.description = description;
        this.organisation = organisation;
    }

    public Long getCategorieProduitId() {
        return categorieProduitId;
    }

    public void setCategorieProduitId(Long categorieProduitId) {
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

    public Long getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Long organisation) {
        this.organisation = organisation;
    }
}
