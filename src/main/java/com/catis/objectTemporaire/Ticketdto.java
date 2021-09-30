package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class Ticketdto {
    private String numTicket;
    private String nomClient;
    private String numClient;
    private String dateVente;
    private List<ProduitTicketdto> produits;
    private double total;
    private String lettre;
    private double totalHT;
}
