package com.catis.objectTemporaire;

import java.util.List;

public class OpCaisseDTO {

    private String type;
    private double prixT;
    private String client;
    private String date;
    private List<DetailDTO> details;

    public OpCaisseDTO() {
        super();
        // TODO Auto-generated constructor stub
    }

    public OpCaisseDTO(String type, double prixT, String client, String date, List<DetailDTO> details) {
        super();
        this.type = type;
        this.prixT = prixT;
        this.client = client;
        this.date = date;
        this.details = details;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrixT() {
        return prixT;
    }

    public void setPrixT(double prixT) {
        this.prixT = prixT;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<DetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List<DetailDTO> details) {
        this.details = details;
    }


}
