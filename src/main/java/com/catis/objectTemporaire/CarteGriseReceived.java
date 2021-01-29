package com.catis.objectTemporaire;

import java.util.Date;


public class CarteGriseReceived {
    private Long carteGriseId;
    private Long visiteId;
    private String numImmatriculation;
    private String preImmatriculation;// immatriculation précédente
    private Date dateDebutValid; //debut de validité
    private Date dateFinValid;// fin de validité
    private String ssdt_id;
    private String commune;
    private double montantPaye;
    private boolean vehiculeGage; // véhicule gagé
    private String genreVehicule;
    private Long marqueVehiculeId;
    private Long produitId;
    private Long proprietaireId;
    private Long vehiculeId;
    private String typeVehicule;
    private String carrosserie;
    private String enregistrement;
    private String chassis;
    private Date premiereMiseEnCirculation;
    private Long energieId;
    private int cylindre; //cm3
    private int puissAdmin; // Puissance Administrative
    private int poidsTotalCha; // poids total en charge
    private int poidsVide;
    private int chargeUtile; // charge utile
    private Date dateDelivrance;
    private String lieuDedelivrance;// lieu de délivrance
    private String centre_ssdt;
    private int places;

    public CarteGriseReceived() {
        super();
        // TODO Auto-generated constructor stub
    }

    public CarteGriseReceived(Long carteGriseId, String numImmatriculation, String preImmatriculation,
                              Date dateDebutValid, Date dateFinValid, String ssdt_id, String commune, double montantPaye,
                              boolean vehiculeGage, String genreVehicule, Long marqueVehiculeId, String typeVehicule, String carrosserie,
                              String enregistrement, String chassis, Date premiereMiseEnCirculation, Long energieId, int cylindre,
                              int puissAdmin, int poidsTotalCha, int poidsVide, int chargeUtile, Date dateDelivrance,
                              String lieuDedelivrance, String centre_ssdt) {
        super();
        this.carteGriseId = carteGriseId;
        this.numImmatriculation = numImmatriculation;
        this.preImmatriculation = preImmatriculation;
        this.dateDebutValid = dateDebutValid;
        this.dateFinValid = dateFinValid;
        this.ssdt_id = ssdt_id;
        this.commune = commune;
        this.montantPaye = montantPaye;
        this.vehiculeGage = vehiculeGage;
        this.genreVehicule = genreVehicule;
        this.marqueVehiculeId = marqueVehiculeId;
        this.typeVehicule = typeVehicule;
        this.carrosserie = carrosserie;
        this.enregistrement = enregistrement;
        this.chassis = chassis;
        this.premiereMiseEnCirculation = premiereMiseEnCirculation;
        this.energieId = energieId;
        this.cylindre = cylindre;
        this.puissAdmin = puissAdmin;
        this.poidsTotalCha = poidsTotalCha;
        this.poidsVide = poidsVide;
        this.chargeUtile = chargeUtile;
        this.dateDelivrance = dateDelivrance;
        this.lieuDedelivrance = lieuDedelivrance;
        this.centre_ssdt = centre_ssdt;
    }

    public Long getCarteGriseId() {
        return carteGriseId;
    }

    public void setCarteGriseId(Long carteGriseId) {
        this.carteGriseId = carteGriseId;
    }

    public String getNumImmatriculation() {
        return numImmatriculation;
    }

    public void setNumImmatriculation(String numImmatriculation) {
        this.numImmatriculation = numImmatriculation;
    }

    public String getPreImmatriculation() {
        return preImmatriculation;
    }

    public void setPreImmatriculation(String preImmatriculation) {
        this.preImmatriculation = preImmatriculation;
    }

    public Date getDateDebutValid() {
        return dateDebutValid;
    }

    public void setDateDebutValid(Date dateDebutValid) {
        this.dateDebutValid = dateDebutValid;
    }

    public Date getDateFinValid() {
        return dateFinValid;
    }

    public void setDateFinValid(Date dateFinValid) {
        this.dateFinValid = dateFinValid;
    }

    public String getSsdt_id() {
        return ssdt_id;
    }

    public void setSsdt_id(String ssdt_id) {
        this.ssdt_id = ssdt_id;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public double getMontantPaye() {
        return montantPaye;
    }

    public void setMontantPaye(double montantPaye) {
        this.montantPaye = montantPaye;
    }

    public boolean isVehiculeGage() {
        return vehiculeGage;
    }

    public void setVehiculeGage(boolean vehiculeGage) {
        this.vehiculeGage = vehiculeGage;
    }

    public String getGenreVehicule() {
        return genreVehicule;
    }

    public void setGenreVehicule(String genreVehicule) {
        this.genreVehicule = genreVehicule;
    }

    public Long getMarqueVehiculeId() {
        return marqueVehiculeId;
    }

    public void setMarqueVehiculeId(Long marqueVehiculeId) {
        this.marqueVehiculeId = marqueVehiculeId;
    }

    public String getTypeVehicule() {
        return typeVehicule;
    }

    public void setTypeVehicule(String typeVehicule) {
        this.typeVehicule = typeVehicule;
    }

    public String getCarrosserie() {
        return carrosserie;
    }

    public void setCarrosserie(String carrosserie) {
        this.carrosserie = carrosserie;
    }

    public String getEnregistrement() {
        return enregistrement;
    }

    public void setEnregistrement(String enregistrement) {
        this.enregistrement = enregistrement;
    }

    public String getChassis() {
        return chassis;
    }

    public void setChassis(String chassis) {
        this.chassis = chassis;
    }

    public Date getPremiereMiseEnCirculation() {
        return premiereMiseEnCirculation;
    }

    public void setPremiereMiseEnCirculation(Date premiereMiseEnCirculation) {
        this.premiereMiseEnCirculation = premiereMiseEnCirculation;
    }

    public Long getEnergieId() {
        return energieId;
    }

    public void setEnergieId(Long energieId) {
        this.energieId = energieId;
    }

    public int getCylindre() {
        return cylindre;
    }

    public void setCylindre(int cylindre) {
        this.cylindre = cylindre;
    }

    public int getPuissAdmin() {
        return puissAdmin;
    }

    public void setPuissAdmin(int puissAdmin) {
        this.puissAdmin = puissAdmin;
    }

    public int getPoidsTotalCha() {
        return poidsTotalCha;
    }

    public void setPoidsTotalCha(int poidsTotalCha) {
        this.poidsTotalCha = poidsTotalCha;
    }

    public int getPoidsVide() {
        return poidsVide;
    }

    public void setPoidsVide(int poidsVide) {
        this.poidsVide = poidsVide;
    }

    public int getChargeUtile() {
        return chargeUtile;
    }

    public void setChargeUtile(int chargeUtile) {
        this.chargeUtile = chargeUtile;
    }

    public Date getDateDelivrance() {
        return dateDelivrance;
    }

    public void setDateDelivrance(Date dateDelivrance) {
        this.dateDelivrance = dateDelivrance;
    }

    public String getLieuDedelivrance() {
        return lieuDedelivrance;
    }

    public void setLieuDedelivrance(String lieuDedelivrance) {
        this.lieuDedelivrance = lieuDedelivrance;
    }

    public String getCentre_ssdt() {
        return centre_ssdt;
    }

    public void setCentre_ssdt(String centre_ssdt) {
        this.centre_ssdt = centre_ssdt;
    }

    public Long getProduitId() {
        return produitId;
    }

    public void setProduitId(Long produitId) {
        this.produitId = produitId;
    }

    public Long getProprietaireId() {
        return proprietaireId;
    }

    public void setProprietaireId(Long proprietaireId) {
        this.proprietaireId = proprietaireId;
    }

    public Long getVehiculeId() {
        return vehiculeId;
    }

    public void setVehiculeId(Long vehiculeId) {
        this.vehiculeId = vehiculeId;
    }

    public int getPlaces() {
        return places;
    }

    public void setPlace(int place) {
        this.places = place;
    }

    public Long getVisiteId() {
        return visiteId;
    }

    public void setVisiteId(Long visiteId) {
        this.visiteId = visiteId;
    }

    public void setPlaces(int places) {
        this.places = places;
    }


}
