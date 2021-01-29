package com.catis.objectTemporaire;

import java.util.List;

public class CategorieTests {

    public String libelle;
    public List<TestList> testlist;


    public CategorieTests(String libelle, List<TestList> testlist) {
        super();
        this.libelle = libelle;
        this.testlist = testlist;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public List<TestList> getTestlist() {
        return testlist;
    }

    public void setTestlist(List<TestList> testlist) {
        this.testlist = testlist;
    }

}
