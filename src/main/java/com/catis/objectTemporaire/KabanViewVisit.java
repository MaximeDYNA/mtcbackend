package com.catis.objectTemporaire;

import java.util.List;

import com.catis.model.entity.Visite;

public class KabanViewVisit {

    private String columnname;
    private List<KanBanSimpleData> visites;
    private int number;

    public KabanViewVisit() {
        super();
    }


    public KabanViewVisit(String columnname, List<KanBanSimpleData> visites, int number) {
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

    public List<KanBanSimpleData> getVisites() {
        return visites;
    }

    public void setVisites(List<KanBanSimpleData> visites) {
        this.visites = visites;
    }


    public int getNumber() {
        return number;
    }


    public void setNumber(int number) {
        this.number = number;
    }


}
