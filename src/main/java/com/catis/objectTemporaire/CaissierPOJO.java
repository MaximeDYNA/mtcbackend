package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CaissierPOJO {

    private UUID caissierId;
    private String nom;
    private String prenom;
    private Long dateNaiss;
    private String lieuDeNaiss;
    private String passport;
    private String permiDeConduire;
    private String cni;
    private String telephone;
    private String email;
    private UUID organisationId;
    private UUID caisse;
    private UUID user;

}
