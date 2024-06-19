package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Encaissement {

    private String clientId;
    private String vendeurId;
    private String contactId;
    private int type;
    private String document;
    private double montantTotal;
    private double montantEncaisse;
    private double montantHT;
    private String numeroTicket;
    private String nomclient;
    private String numeroclient;
    private String nomcontacts;
    private String numerocontacts;
    private UUID sessionCaisseId;
    private String lang;
    private List<ProduitVue> produitVue;
    private String certidocsId;


    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }

}
