package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaxePOJO {

    private Long taxeId;
    private String nom;
    private String description;
    private double valeur;
    private List<ObjectForSelect> produits;
    private boolean incluse;
    private Long organisationId;

}
