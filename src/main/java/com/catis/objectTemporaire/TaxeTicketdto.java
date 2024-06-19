package com.catis.objectTemporaire;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxeTicketdto implements Serializable {
    private String libelle;
    private double valeur;
}
