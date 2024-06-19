package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProprietairePOJO {

    private UUID proprietaireVehiculeId;
    private UUID idControleur;
    private String nom;
    private String prenom;
    private Date dateNaiss;
    private String lieuDeNaiss;
    private String passport;
    private String permiDeConduire;
    private String cni;
    private String telephone;
    private String email;
    private ObjectForSelect organisationId;
    private ObjectForSelect user;
    private String description;
    private UUID partenaireId;

}
