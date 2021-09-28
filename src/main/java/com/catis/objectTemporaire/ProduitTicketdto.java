package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor @NoArgsConstructor
public class ProduitTicketdto {
    private String immatriculation;
    private String cat;
    private double prixTTC;
    private double prix;
    private String description;
    private List<TaxeTicketdto> taxeTicketdtos;
}
