package com.catis.objectTemporaire;

import java.util.List;

import com.catis.model.Produit;
import com.catis.model.Taxe;

public class Card {

    private String reference;
    private Produit produit;
    private List<Taxe> taxes;

    public Card(String reference, Produit produit, List<Taxe> taxes) {
        super();
        this.reference = reference;
        this.produit = produit;
        this.taxes = taxes;
    }

    public Card() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public List<Taxe> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<Taxe> taxes) {
        this.taxes = taxes;
    }


}
