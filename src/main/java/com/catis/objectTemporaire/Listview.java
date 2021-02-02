package com.catis.objectTemporaire;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.catis.model.CategorieTest;
import com.catis.model.GieglanFile;
import com.catis.service.CategorieTestVehiculeService;
import com.catis.service.GieglanFileService;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private List<CategorieTest> measures;
    @JsonIgnore
    private VisiteService visiteService;
    @JsonIgnore
    private GieglanFileService gieglanFileService;
    @JsonIgnore
    private CategorieTestVehiculeService catSer;

    public Listview(Long id, VisiteService visiteService, GieglanFileService gieglanFileService, CategorieTestVehiculeService catSer) {
        this.id = id;
        this.visiteService = visiteService;
        this.gieglanFileService = gieglanFileService;
        this.catSer = catSer;
        measures = new ArrayList<>();
        manageColor();
    }
    public Listview() {


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
        System.out.println("----------"+ this.id);
        Visite visite = visiteService.findById(this.id);

        if (visite.isContreVisite()) {
            Visite visiteWithMissedTests = visiteService.visiteWithLastMissedTests(visite);
            visiteWithMissedTests
                .getInspection().getGieglanFiles()
                .forEach( g -> {
                    replaceIconIfNecessary(g.getCategorieTest(), this.id);
                    this.measures.add(g.getCategorieTest());
                });
        } else {
            visite.getCarteGrise().getProduit().getCategorieVehicule().getCategorieTestVehicules().forEach(
                    categorieTestVehicule -> {
                        System.out.println("*********"+categorieTestVehicule.getCategorieTest().getLibelle());
                        replaceIconIfNecessary(categorieTestVehicule.getCategorieTest(), this.id);
                        this.measures.add(categorieTestVehicule.getCategorieTest());
                    }
            );
        }
    }

    public void replaceIconIfNecessary(CategorieTest categorieTest, Long idVisite){
        gieglanFileService.findByExtensionAndVisite(categorieTest.getLibelle(), idVisite)
                .forEach(g -> {
                    if(g.getStatus().equals(GieglanFile.StatusType.VALIDATED)){
                        switch (categorieTest.getLibelle()){
                            case "F":
                                categorieTest.setIcon("<span class=\"badge badge-success\"><i class=\"i-Pause\"></i></span>&nbsp");
                                break;
                            case "R":
                                 categorieTest.setIcon("<span class=\"badge badge-success\"><i class=\"i-Car-2\"></i></span>&nbsp");
                                 break;
                            case "S":
                                categorieTest.setIcon("<span class=\"badge badge-success\"><i class=\"i-Jeep-2\"></i></span>&nbsp");
                                break;
                            case "P":
                                categorieTest.setIcon("<span class=\"badge badge-success\"><i class=\"i-Flash\"></i></span>&nbsp");
                                break;
                            case "O":
                                categorieTest.setIcon("<span class=\"badge badge-success\"><i class=\"i-Cloud1\"></i></span>&nbsp");
                                break;
                            case "G":
                                categorieTest.setIcon("<span class=\"badge badge-success\"><i class=\"i-Eye\"></i></span>&nbsp");
                                break;
                        }
                    }
                    else{
                        switch (categorieTest.getLibelle()){
                            case "F":
                                categorieTest.setIcon("<span class=\"badge badge-danger\"><i class=\"i-Pause\"></i></span>&nbsp");
                                break;
                            case "R":
                                categorieTest.setIcon("<span class=\"badge badge-danger\"><i class=\"i-Car-2\"></i></span>&nbsp");
                                break;
                            case "S":
                                categorieTest.setIcon("<span class=\"badge badge-danger\"><i class=\"i-Jeep-2\"></i></span>&nbsp");
                                break;
                            case "P":
                                categorieTest.setIcon("<span class=\"badge badge-danger\"><i class=\"i-Flash\"></i></span>&nbsp");
                                break;
                            case "O":
                                categorieTest.setIcon("<span class=\"badge badge-danger\"><i class=\"i-Cloud1\"></i></span>&nbsp");
                                break;
                            case "G":
                                categorieTest.setIcon("<span class=\"badge badge-danger\"><i class=\"i-Eye\"></i></span>&nbsp");
                                break;
                        }
                    }

                });
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
                for (CategorieTest cat : measures) {
                    b++;
                    this.statut += cat.getIcon();
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

    public List<CategorieTest> getMeasures() {
        return measures;
    }

    public void setMeasures(List<CategorieTest> measures) {
        this.measures = measures;
    }

    public VisiteService getVisiteService() {
        return visiteService;
    }

    public void setVisiteService(VisiteService visiteService) {
        this.visiteService = visiteService;
    }

    public GieglanFileService getGieglanFileService() {
        return gieglanFileService;
    }

    public void setGieglanFileService(GieglanFileService gieglanFileService) {
        this.gieglanFileService = gieglanFileService;
    }

    public CategorieTestVehiculeService getCatSer() {
        return catSer;
    }

    public void setCatSer(CategorieTestVehiculeService catSer) {
        this.catSer = catSer;
    }
}
