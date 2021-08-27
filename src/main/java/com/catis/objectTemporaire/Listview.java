package com.catis.objectTemporaire;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.catis.model.control.Control;
import com.catis.model.control.GieglanFile;
import com.catis.model.entity.*;
import com.catis.service.CategorieTestVehiculeService;
import com.catis.service.GieglanFileService;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.catis.service.VisiteService;
import org.springframework.stereotype.Service;


public class Listview {

    private Long id;
    private Produit categorie;
    private String type;
    private String reference;
    private String chassis;
    private String client;
    private String date;
    private String statut;
    private Organisation organisation;
    private int statutVisite;
    private Long idVisite;
    private boolean contreVisite;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private boolean encours = true;
    private Caissier caissier;
    private Long inspection;
    private VerbalProcess process;
    private CarteGrise carteGrise;
    @JsonIgnore
    private Visite vis;
    //private Control control;
    private int isConform;
    private List<GieglanFileIcon> measures = new ArrayList<>();
    private List<CategorieTest> testAttendus = new ArrayList<>();

    @JsonIgnore
    private VisiteService visiteService;
    @JsonIgnore
    private GieglanFileService gieglanFileService;
    @JsonIgnore
    private CategorieTestVehiculeService catSer;

    public Listview() {


    }
    public Listview(Visite v, VisiteService visiteService, GieglanFileService gieglanFileService, CategorieTestVehiculeService catSer) {
        super();
        this.id = v.getIdVisite();
        this.vis=v;
        this.statut="";
        this.statutVisite = v.getStatut();
        this.chassis = (v.getCarteGrise().getVehicule()==null
                ? "": (v.getCarteGrise().getVehicule().getChassis()==null
                ? "" : v.getCarteGrise().getVehicule().getChassis()));
        this.visiteService = visiteService;
        this.gieglanFileService = gieglanFileService;
        this.catSer = catSer;
        this.measures = new ArrayList<>();
        this.organisation = v.getOrganisation();
        this.idVisite = id;
        this.contreVisite = v.isContreVisite();
        this.dateDebut = v.getDateDebut();
        this.dateFin = v.getDateFin();
        this.encours = v.isEncours();
        this.caissier = v.getCaissier();
        this.inspection = v.getInspection() == null ? null : v.getInspection().getIdInspection();
        this.process =v.getProcess();
        this.carteGrise=v.getCarteGrise();
        //this.control =v.getControl();
        this.isConform = v.getIsConform();


        manageColor();
    }


    public void manageColor() {


        if (this.vis.isContreVisite()) {

            Visite visiteWithMissedTests = visiteService.visiteWithLastMissedTests(this.vis);
            System.out.println("Missed tests size :"+visiteWithMissedTests.getInspection().getGieglanFiles().size());
            for(GieglanFile g: visiteWithMissedTests
                    .getInspection().getGieglanFiles()){

                GieglanFileIcon gfi = new GieglanFileIcon();
                    if(g.getType()!=GieglanFile.FileType.CARD_REGISTRATION){
                        gfi.setExtension(g.getCategorieTest().getLibelle());
                        gfi.setIcon(g.getCategorieTest().getIcon());
                        this.testAttendus.add(g.getCategorieTest());
                        this.measures.add(replaceIconIfNecessary(gfi, this.id));
                    }
                }
        } else {
            List<GieglanFileIcon> categorieTests = new ArrayList<>();


            Set<CategorieTestVehicule> integersSet = new LinkedHashSet<CategorieTestVehicule>(this.vis
                    .getCarteGrise()
                    .getProduit()
                    .getCategorieVehicule()
                    .getCategorieTestVehicules());
            List<CategorieTestVehicule> list = new ArrayList<CategorieTestVehicule>(integersSet);
            list.sort((CategorieTestVehicule s1, CategorieTestVehicule s2)->s1.getId().compareTo(s2.getId()));

            for(CategorieTestVehicule c : list ){
                GieglanFileIcon gfi = new GieglanFileIcon();
                gfi.setExtension(c.getCategorieTest().getLibelle());
                gfi.setIcon(c.getCategorieTest().getIcon());
                this.testAttendus.add(c.getCategorieTest());
                categorieTests.add(gfi);
            }
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

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
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

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public boolean isEncours() {
        return encours;
    }

    public void setEncours(boolean encours) {
        this.encours = encours;
    }

    public Caissier getCaissier() {
        return caissier;
    }

    public void setCaissier(Caissier caissier) {
        this.caissier = caissier;
    }

    public Long getInspection() {
        return inspection;
    }

    public void setInspection(Long inspection) {
        this.inspection = inspection;
    }

    public VerbalProcess getProcess() {
        return process;
    }

    public void setProcess(VerbalProcess process) {
        this.process = process;
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

    public List<CategorieTest> getTestAttendus() {
        return testAttendus;
    }

    public void setTestAttendus(List<CategorieTest> testAttendus) {
        this.testAttendus = testAttendus;
    }
}
