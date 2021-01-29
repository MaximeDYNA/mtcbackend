package com.catis.objectTemporaire;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.catis.model.Produit;
import com.catis.model.Visite;
import com.catis.service.VisiteService;

public class Listview {

    private Long id;
    private Produit categorie;
    private String type;
    private String reference;
    private String client;
    private String date;
    private String statut;
    private List<String> measures;

    @Autowired
    private VisiteService visiteService;

    public Listview(Long id) {
        this.id = id;
        measures = new ArrayList<>();
        measures.add("<span class=\"badge badge-light\"><i class=\"i-Jeep-2\"></i></span>&nbsp");
        measures.add("<span class=\"badge badge-light\"><i class=\"i-Jeep\"></i></span>&nbsp");
        measures.add("<span class=\"badge badge-light\"><i class=\"i-Car-2\"></i></span>&nbsp");
        measures.add("<span class=\"badge badge-light\"><i class=\"i-Eye\"></i></span>&nbsp");
        measures.add("<span class=\"badge badge-light\"><i class=\"i-Cloud1\"></i></span>&nbsp");
        measures.add("<span class=\"badge badge-light\"><i class=\"i-Pause\"></i></span>&nbsp");
    }

    public Listview(Long id, Produit categorie, String type, String reference, String client, String date,
                    String statut) {
        super();
        this.id = id;
        this.categorie = categorie;
        this.type = type;
        this.reference = reference;
        this.client = client;
        this.date = date;
        this.statut = statut;
    }

    public void manageColor() {
        Visite visite = visiteService.findById(this.id);
        if (!visite.isContreVisite()) {

        }
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produit getCategorie() {
        return categorie;
    }

    public void setCategorie(Produit categorie) {
        this.categorie = categorie;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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

    public void setDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.date = date.format(formatter);
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        switch (statut) {
            case "maj":
                this.statut = "<span class=\"badge badge-primary\">" + statut + "</span>";
                break;
            case "A inspecter":
                this.statut = "<span class=\"badge badge-warning\">" + statut + "</span>";
                break;
            case "En cours test":

                this.statut = "";
                int b = 0;
                for (String i : measures) {
                    b++;
                    this.statut += i;
//				  if(b%3==0) {
//					  this.statut+="<br/>";
//				  }
                }
                break;
            case "A signer":
                this.statut = "<span class=\"badge badge-info\">" + statut + "</span>";
                break;
            case "A imprimer":
                this.statut = "<span class=\"badge badge-success\">" + statut + "</span>";
                break;
            case "A enregister":
                this.statut = "<span class=\"badge badge-dark\">" + statut + "</span>";
                break;
            case "A certifier":
                this.statut = "<span class=\"badge badge-primary\">" + statut + "</span>";
                break;
            case "Accepté":
                this.statut = "<span class=\"badge badge-success\">" + statut + "</span>";
                break;
            case "Refusé":
                this.statut = "<span class=\"badge badge-danger\">" + statut + "</span>";
                break;
            default:
                this.statut = "<span class=\"badge badge-warning\">" + statut + "</span>";
        }

    }


    public void setDate(String date) {
        this.date = date;
    }


}
