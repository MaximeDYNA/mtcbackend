package com.catis.model;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

@Entity
@Table(name = "t_operationdecaisse")
@EntityListeners(AuditingEntityListener.class)
@Audited
public class OperationCaisse extends JournalData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long operationDeCaisseId;
    private int type;
    private double montant;

    @ManyToOne
    private Vente vente;

    @ManyToOne
    private Taxe taxe;

    @ManyToOne
    private SessionCaisse sessionCaisse;

    private String numeroTicket;

    public OperationCaisse() {

    }


    public OperationCaisse(long operationDeCaisseId, int type, double montant, Caissier caissier, Vente vente,
                           Taxe taxe, SessionCaisse sessionCaisse, String numeroTicket) {
        super();
        this.operationDeCaisseId = operationDeCaisseId;
        this.type = type;
        this.montant = montant;

        this.vente = vente;
        this.taxe = taxe;
        this.sessionCaisse = sessionCaisse;
        this.numeroTicket = numeroTicket;
    }


    public long getOperationDeCaisseId() {
        return operationDeCaisseId;
    }

    public void setOperationDeCaisseId(long operationDeCaisseId) {
        this.operationDeCaisseId = operationDeCaisseId;
    }

    public int isType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public int getType() {
        return type;
    }


    public Vente getVente() {
        return vente;
    }

    public void setVente(Vente vente) {
        this.vente = vente;
    }

    public Taxe getTaxe() {
        return taxe;
    }

    public void setTaxe(Taxe taxe) {
        this.taxe = taxe;
    }

    public String getNumeroTicket() {
        return numeroTicket;
    }

    public void setNumeroTicket(String numeroTicket) {
        this.numeroTicket = numeroTicket;
    }

    public SessionCaisse getSessionCaisse() {
        return sessionCaisse;
    }

    public void setSessionCaisse(SessionCaisse sessionCaisse) {
        this.sessionCaisse = sessionCaisse;
    }

    public String getLibelle() {
        if (this.type == 0) {
            return "Mise en compte";
        }
        if (this.type == 1) {
            return "Encaissement";
        }
        if (this.type == 2) {
            return "Décaissement";
        }
        return "Erreur";
    }

}
