package com.catis.objectTemporaire;

import org.springframework.web.multipart.MultipartFile;

import com.catis.model.Produit;

public class ProduitView {

    private Long produitId;
    private String libelle;
    private String description;
    private double prix;
    private int delaiValidite;
    private MultipartFile file;
    private Long categorieProduitId;

    public ProduitView() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ProduitView(Long produitId, String libelle, String description, double prix, int delaiValidite,
                       MultipartFile file, Long categorieProduitId) {
        super();
        this.produitId = produitId;
        this.libelle = libelle;
        this.description = description;
        this.prix = prix;
        this.delaiValidite = delaiValidite;
        this.file = file;
        this.categorieProduitId = categorieProduitId;
    }


    public Long getProduitId() {
        return produitId;
    }

    public void setProduitId(Long produitId) {
        this.produitId = produitId;
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

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getDelaiValidite() {
        return delaiValidite;
    }

    public void setDelaiValidite(int delaiValidite) {
        this.delaiValidite = delaiValidite;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public Long getCategorieProduitId() {
        return categorieProduitId;
    }

    public void setCategorieProduitId(Long categorieProduitId) {
        this.categorieProduitId = categorieProduitId;
    }

}
