package com.catis.objectTemporaire;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import com.catis.model.*;
import com.catis.service.CategorieTestVehiculeService;
import com.catis.service.GieglanFileService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import com.catis.service.VisiteService;
import org.springframework.stereotype.Service;

@Service
public class Listview {

    private Long id;
    private Produit categorie;
    private String type;
    private String reference;
    private String chassis;
    private String client;
    private String date;
    private String statut;
    private int statutVisite;
    private List<GieglanFileIcon> measures = new ArrayList<>();

    @JsonIgnore
    private VisiteService visiteService;
    @JsonIgnore
    private GieglanFileService gieglanFileService;
    @JsonIgnore
    private CategorieTestVehiculeService catSer;

    public Listview() {


    }
    public Listview(Long id, VisiteService visiteService, GieglanFileService gieglanFileService, CategorieTestVehiculeService catSer) {
        super();
        this.id = id;
        Visite v = visiteService.findById(id);
        this.statut="";
        this.statutVisite = v.getStatut();
        this.chassis = (v.getCarteGrise().getVehicule()==null
                ? "": (v.getCarteGrise().getVehicule().getChassis()==null
                ? "" : v.getCarteGrise().getVehicule().getChassis()));
        this.visiteService = visiteService;
        this.gieglanFileService = gieglanFileService;
        this.catSer = catSer;
        this.measures = new ArrayList<>();
        manageColor();
    }


    public void manageColor() {
        Visite visite = visiteService.findById(this.id);

        if (visite.isContreVisite()) {

            Visite visiteWithMissedTests = visiteService.visiteWithLastMissedTests(visite);

            GieglanFileIcon gfi;
            for(GieglanFile g: visiteWithMissedTests
                    .getInspection().getGieglanFiles()){

                    gfi = new GieglanFileIcon();
                    gfi.setExtension(g.getCategorieTest().getLibelle());
                    gfi.setIcon(g.getCategorieTest().getIcon());
                   this.measures.add(replaceIconIfNecessary(gfi, this.id));
                }
        } else {
            List<GieglanFileIcon> categorieTests = new ArrayList<>();
            GieglanFileIcon gfi;

            Set<CategorieTestVehicule> integersSet = new LinkedHashSet<CategorieTestVehicule>(visite
                    .getCarteGrise()
                    .getProduit()
                    .getCategorieVehicule()
                    .getCategorieTestVehicules());
            List<CategorieTestVehicule> list = new ArrayList<CategorieTestVehicule>(integersSet);
            list.sort((CategorieTestVehicule s1, CategorieTestVehicule s2)->s1.getId().compareTo(s2.getId()));

            for(CategorieTestVehicule c : list ){
                gfi = new GieglanFileIcon();
                gfi.setExtension(c.getCategorieTest().getLibelle());
                gfi.setIcon(c.getCategorieTest().getIcon());
                categorieTests.add(gfi);
            }
            /*visite
                    .getCarteGrise()
                    .getProduit()
                    .getCategorieVehicule()
                    .getCategorieTestVehicules()
                    .forEach(
                    categorieTestVehicule -> {
                        categorieTests.se;
                    }
            );*/
            categorieTests.forEach(categorieTest -> {
                this.measures.add(replaceIconIfNecessary(categorieTest, this.id));
            });

        }
    }

    public GieglanFileIcon replaceIconIfNecessary(GieglanFileIcon categorieTest, Long idVisite){
        List<GieglanFile> gieglanFiles = gieglanFileService.findByExtensionAndVisite(categorieTest.getExtension(), idVisite);
        gieglanFiles.forEach(g -> {
                    if(g.getStatus().equals(GieglanFile.StatusType.VALIDATED)){
                        switch (categorieTest.getExtension()){
                            case "F":
                                categorieTest.setIcon("<span class=\"badge badge-success\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp");
                                //categorieTest.setIcon("<span class=\"badge badge-success\"><i class=\"i-Pause\"></i></span>&nbsp");
                                break;
                            case "R":
                                categorieTest.setIcon("<span class=\"badge badge-success\"><i class=\"i-Car-2\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Ripage\"></i></span>&nbsp");
                                break;
                            case "S":
                                categorieTest.setIcon("<span class=\"badge badge-success\"><i class=\"i-Jeep-2\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"Suspension\"></i></span>&nbsp");
                                break;
                            case "P":
                                categorieTest.setIcon("<span class=\"badge badge-success\"><i class=\"i-Flash\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Réglophare\"></i></span>&nbsp");
                                break;
                            case "JSON":
                                categorieTest.setIcon("<span class=\"badge badge-success\"><i class=\"i-Eye\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures visuelles\"></i></span>&nbsp");
                                break;
                            case "G":
                                categorieTest.setIcon("<span class=\"badge badge-success\"><i class=\"i-Cloud1\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Analyseur de gaz\"></i></span>&nbsp");
                                break;
                        }
                    }
                    if(g.getStatus().equals(GieglanFile.StatusType.INITIALIZED)){
                        switch (categorieTest.getExtension()){
                            case "F":
                                categorieTest.setIcon("<span class=\"badge badge-light\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp");
                                //categorieTest.setIcon("<span class=\"badge badge-success\"><i class=\"i-Pause\"></i></span>&nbsp");
                                break;
                            case "R":
                                categorieTest.setIcon("<span class=\"badge badge-light\"><i class=\"i-Car-2\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Ripage\"></i></span>&nbsp");
                                break;
                            case "S":
                                categorieTest.setIcon("<span class=\"badge badge-light\"><i class=\"i-Jeep-2\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"Suspension\"></i></span>&nbsp");
                                break;
                            case "P":
                                categorieTest.setIcon("<span class=\"badge badge-light\"><i class=\"i-Flash\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Réglophare\"></i></span>&nbsp");
                                break;
                            case "JSON":
                                categorieTest.setIcon("<span class=\"badge badge-light\"><i class=\"i-Eye\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures visuelles\"></i></span>&nbsp");
                                break;
                            case "G":
                                categorieTest.setIcon("<span class=\"badge badge-light\"><i class=\"i-Cloud1\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Analyseur de gaz\"></i></span>&nbsp");
                                break;
                        }

                    }
                    if(g.getStatus().equals(GieglanFile.StatusType.REJECTED)){
                        switch (categorieTest.getExtension()){
                            case "F":
                                categorieTest.setIcon("<span class=\"badge badge-danger\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp");
                                //categorieTest.setIcon("<span class=\"badge badge-success\"><i class=\"i-Pause\"></i></span>&nbsp");
                                break;
                            case "R":
                                categorieTest.setIcon("<span class=\"badge badge-danger\"><i class=\"i-Car-2\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Ripage\"></i></span>&nbsp");
                                break;
                            case "S":
                                categorieTest.setIcon("<span class=\"badge badge-danger\"><i class=\"i-Jeep-2\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"Suspension\"></i></span>&nbsp");
                                break;
                            case "P":
                                categorieTest.setIcon("<span class=\"badge badge-danger\"><i class=\"i-Flash\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Réglophare\"></i></span>&nbsp");
                                break;
                            case "JSON":
                                categorieTest.setIcon("<span class=\"badge badge-danger\"><i class=\"i-Eye\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Mesures visuelles\"></i></span>&nbsp");
                                break;
                            case "G":
                                categorieTest.setIcon("<span class=\"badge badge-danger\"><i class=\"i-Cloud1\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Analyseur de gaz\"></i></span>&nbsp");
                                break;
                        }
                    }

                });
        return categorieTest;

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
                for (GieglanFileIcon cat : measures) {
                    b++;
                    this.statut += cat.getIcon();
//				  if(b%3==0) {
//					  this.statut+="<br/>";
//				  }
                }
                break;
            case "A signer":
                Visite visite = visiteService.findById(this.id);
                if(visite.getProcess().isStatus())
                    this.statut = "<span class=\"badge badge-info\"> ACCEPTE " + statut + "</span>";
                else
                    this.statut = "<span class=\"badge badge-info\"> REFUSE " + statut + "</span>";
                break;
            case "A imprimer":
                this.statut = "<span class=\"badge badge-success\">" + statut + "</span>";
                break;
            case "A certifier":
                this.statut = "<span class=\"badge badge-primary\">" + statut + "</span>";
                break;
            case "Accepté":
                this.statut = "<span class=\"badge badge-success\">" + statut + "</span>";
                break;
            case "Refusé":
                this.statut = "<span class=\"badge badge-dark\">" + statut + "</span>";
                break;
            default:
                this.statut = "<span class=\"badge badge-warning\">" + statut + "</span>";
        }
    }


    public void setDate(String date) {
        this.date = date;
    }

    public List<GieglanFileIcon> getMeasures() {
        return measures;
    }

    public void setMeasures(List<GieglanFileIcon> measures) {
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

    public String getChassis() {
        return chassis;
    }

    public void setChassis(String chassis) {
        this.chassis = chassis;
    }

    public int getStatutVisite() {
        return statutVisite;
    }

    public void setStatutVisite(int statutVisite) {
        this.statutVisite = statutVisite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Listview listview = (Listview) o;
        return statutVisite == listview.statutVisite &&
                Objects.equals(id, listview.id) &&
                Objects.equals(categorie, listview.categorie) &&
                Objects.equals(type, listview.type) &&
                Objects.equals(reference, listview.reference) &&
                Objects.equals(chassis, listview.chassis) &&
                Objects.equals(client, listview.client) &&
                Objects.equals(date, listview.date) &&
                Objects.equals(statut, listview.statut) &&
                Objects.equals(measures, listview.measures) &&
                Objects.equals(visiteService, listview.visiteService) &&
                Objects.equals(gieglanFileService, listview.gieglanFileService) &&
                Objects.equals(catSer, listview.catSer);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, categorie, type, reference, chassis, client, date, statut, statutVisite, measures, visiteService, gieglanFileService, catSer);
    }

}
