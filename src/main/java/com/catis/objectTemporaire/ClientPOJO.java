package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientPOJO {

    private UUID clientId;
    private String nom;
    private String description;
    private String prenom;
    private Date dateNaiss;
    private String lieuDeNaiss;
    private String passport;
    private String permiDeConduire;
    private String cni;
    private String telephone;
    private String email;
    private UUID organisationId;
}
