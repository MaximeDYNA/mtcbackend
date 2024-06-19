package com.catis.objectTemporaire;

import com.catis.model.entity.Organisation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientDTO {
    private UUID clientId;
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
    private LocalDateTime createdDate;
}
