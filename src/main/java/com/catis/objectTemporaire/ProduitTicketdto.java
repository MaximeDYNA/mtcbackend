package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
@Data
@AllArgsConstructor @NoArgsConstructor
public class ProduitTicketdto implements Serializable {
    private String immatriculation;
    private String cat;
    private double prixTTC;
    private double prix;
    private String description;
    private List<TaxeTicketdto> taxeTicketdtos;
}
