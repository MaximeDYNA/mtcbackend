package com.catis.objectTemporaire;


import java.util.List;

import com.catis.model.entity.Produit;
import com.catis.model.entity.Taxe;


public class ProduitEtTaxe{

    private Produit produit;

    private List<Taxe> taxe;

    public ProduitEtTaxe() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ProduitEtTaxe(Produit produit, List<Taxe> taxe) {
        super();
        this.produit = produit;
        this.taxe = taxe;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public List<Taxe> getTaxe() {
        return taxe;
    }

    public void setTaxe(List<Taxe> taxe) {
        this.taxe = taxe;
    }


}
