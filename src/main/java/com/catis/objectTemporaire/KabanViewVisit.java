package com.catis.objectTemporaire;

import java.util.List;

import com.catis.model.Visite;

public class KabanViewVisit {

    private String columnname;
    private List<Visite> visites;
    private int number;

    public KabanViewVisit() {
        super();
    }


    public KabanViewVisit(String columnname, List<Visite> visites, int number) {
        super();
        this.columnname = columnname;
        this.visites = visites;
        this.number = number;
    }


    public String getColumnname() {
        return columnname;
    }

    public void setColumnname(String columnname) {
        this.columnname = columnname;
    }

    public List<Visite> getVisites() {
        return visites;
    }

    public void setVisites(List<Visite> visites) {
        this.visites = visites;
    }


    public int getNumber() {
        return number;
    }


    public void setNumber(int number) {
        this.number = number;
    }


}
