package com.catis.objectTemporaire;

import com.catis.model.Organisation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProprietaireDTO {
    // object de transfert proprietaire et controleur
    private Long proprietaireVehiculeId;
    private Long idControleur;
    private String nom;
    private String prenom;
    private Date dateNaiss;
    private String lieuDeNaiss;
    private String passport;
    private String permiDeConduire;
    private String cni;
    private String telephone;
    private String email;
    private String description;
    private Organisation organisation;
    private Long vehicule;
    private LocalDateTime createdDate;
    private Long partenaireId;
    private String agremment;
}