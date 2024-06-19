package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaxePOJO {

    private UUID taxeId;
    private String nom;
    private String description;
    private double valeur;
    private List<ObjectForSelect> produits;
    private boolean incluse;
    private UUID organisationId;

}
