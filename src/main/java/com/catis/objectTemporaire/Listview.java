package com.catis.objectTemporaire;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.catis.model.control.GieglanFile;
import com.catis.model.entity.*;
import com.catis.service.CategorieTestVehiculeService;
import com.catis.service.GieglanFileService;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.catis.service.VisiteService;


public class Listview {

    private UUID id;
    private Produit categorie;
    private String type;
    private String reference;
    private String chassis;
    private String client;
    private String createdAt;
    private LocalDateTime createdDate;
    private String statut;
    private int statutVisite;
    private UUID idVisite;
    private boolean contreVisite;
    private boolean encours = true;
    private UUID inspection;
    private CarteGrise carteGrise;
    private boolean conformityTest;
    private int isConform;
    private String organisation;
    private String bestPlate;
    private double accurance;
    private String date;

    @JsonIgnore
    private Visite vis;
    @JsonIgnore
    private List<GieglanFileIcon> measures;
    @JsonIgnore
    private VisiteService visiteService;
    @JsonIgnore
    private GieglanFileService gieglanFileService;
    @JsonIgnore
    private CategorieTestVehiculeService catSer;


    public Listview(Visite v, VisiteService visiteService, GieglanFileService gieglanFileService, CategorieTestVehiculeService catSer) {
        super();
        this.id = v.getIdVisite();
        this.organisation = v.getOrganisation().getNom();
        this.organisation = v.getOrganisation().getNom();
        this.vis=v;

        this.statut="";
        this.statutVisite = v.getStatut();
        this.setCreatedAt(v.getCreatedDate());
        this.createdDate = v.getCreatedDate();
        this.chassis = (v.getCarteGrise().getVehicule()==null
                ? "": (v.getCarteGrise().getVehicule().getChassis()==null
                ? "" : v.getCarteGrise().getVehicule().getChassis()));
        this.visiteService = visiteService;
        this.gieglanFileService = gieglanFileService;
        this.catSer = catSer;
        this.measures = new ArrayList<>();
        this.idVisite = id;
        this.contreVisite = v.isContreVisite();
        this.encours = v.isEncours();
        this.inspection = v.getInspection() == null ? null : v.getInspection().getIdInspection();
        this.carteGrise=v.getCarteGrise();
        this.isConform = v.getIsConform();
        this.conformityTest = v.getOrganisation().isConformity();
        if(v.getStatut() > 6){
            this.bestPlate = v.getInspection().getBestPlate();
            this.accurance = v.getInspection().getDistancePercentage();
        }


        manageColor();
    }


    public void manageColor() {
        if(this.vis.getStatut()==2){
            if (this.vis.isContreVisite()) {
                List<GieglanFile> files = gieglanFileService.getGieglanFileFailed(vis);

                for(GieglanFile g: files){

                    GieglanFileIcon gfi = new GieglanFileIcon();
                    if(g.getType()!=GieglanFile.FileType.CARD_REGISTRATION){
                        gfi.setExtension(g.getCategorieTest().getLibelle());
                        gfi.setIcon(g.getCategorieTest().getIcon());
                        this.measures.add(replaceIconIfNecessary(gfi, this.id));
                    }
                }
            } else {
                List<GieglanFileIcon> categorieTests = new ArrayList<>();

                Set<CategorieTestProduit> integersSet = new LinkedHashSet<CategorieTestProduit>(this.vis
                        .getCarteGrise()
                        .getProduit()
                        .getCategorieTestProduits());
                List<CategorieTestProduit> list = new ArrayList<CategorieTestProduit>(integersSet);
                list.sort((CategorieTestProduit s1, CategorieTestProduit s2)->s1.getId().compareTo(s2.getId()));

                for(CategorieTestProduit c : list ){
                    GieglanFileIcon gfi = new GieglanFileIcon();
                    gfi.setExtension(c.getCategorieTest().getLibelle());
                    gfi.setIcon(c.getCategorieTest().getIcon());
                    categorieTests.add(gfi);
                }
                categorieTests.forEach(categorieTest -> {
                    this.measures.add(replaceIconIfNecessary(categorieTest, this.id));
                });

            }
        }
    }

    public GieglanFileIcon replaceIconIfNecessary(GieglanFileIcon categorieTest, UUID idVisite){
        List<GieglanFile> gieglanFiles = gieglanFileService.findByExtensionAndVisite(categorieTest.getExtension(), idVisite);
        gieglanFiles.forEach(g -> {
                    if(g.getStatus().equals(GieglanFile.StatusType.VALIDATED)){
                        switch (categorieTest.getExtension()){
                            case "F":
                                categorieTest.setIcon("<span class=\"badge badge-success\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp");
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
                    else if(g.getStatus().equals(GieglanFile.StatusType.INITIALIZED)){
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
                    else{
                        switch (categorieTest.getExtension()){
                            case "F":
                                categorieTest.setIcon("<span class=\"badge badge-danger\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Freinage\"><i class=\"i-Pause\"></i></span>&nbsp");
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

    public void setId(UUID id) {
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

    public void setDate(String date) {
        this.date = date;
    }

    public String getCreatedAt() {

        return createdAt;
    }

    public void setCreatedAt(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.createdAt = date.format(formatter);
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getBestPlate() {
        return bestPlate;
    }

    public void setBestPlate(String bestPlate) {
        this.bestPlate = bestPlate;
    }

    public double getAccurance() {
        return accurance;
    }

    public void setAccurance(double accurance) {
        this.accurance = accurance;
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
                }
                break;
            case "A signer":
                if(this.vis.getProcess().isStatus())
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

    public boolean isConformityTest() {
        return conformityTest;
    }

    public void setConformityTest(boolean conformityTest) {
        this.conformityTest = conformityTest;
    }

    public Visite getVis() {
        return vis;
    }

    public void setVis(Visite vis) {
        this.vis = vis;
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
                Objects.equals(createdAt, listview.createdAt) &&
                Objects.equals(statut, listview.statut) &&
                Objects.equals(measures, listview.measures) &&
                Objects.equals(visiteService, listview.visiteService) &&
                Objects.equals(gieglanFileService, listview.gieglanFileService) &&
                Objects.equals(catSer, listview.catSer);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, categorie, type, reference, chassis, client, createdAt, statut, statutVisite, measures, visiteService, gieglanFileService, catSer);
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Long getIdVisite() {
        return idVisite;
    }

    public void setIdVisite(Long idVisite) {
        this.idVisite = idVisite;
    }

    public boolean isContreVisite() {
        return contreVisite;
    }

    public void setContreVisite(boolean contreVisite) {
        this.contreVisite = contreVisite;
    }



    public boolean isEncours() {
        return encours;
    }

    public void setEncours(boolean encours) {
        this.encours = encours;
    }


    public Long getInspection() {
        return inspection;
    }

    public void setInspection(Long inspection) {
        this.inspection = inspection;
    }



    public CarteGrise getCarteGrise() {
        return carteGrise;
    }

    public void setCarteGrise(CarteGrise carteGrise) {
        this.carteGrise = carteGrise;
    }

    public int getIsConform() {
        return isConform;
    }

    public void setIsConform(int isConform) {
        this.isConform = isConform;
    }

}
