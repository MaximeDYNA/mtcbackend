package com.catis.objectTemporaire;

import java.util.List;

import com.catis.model.entity.DetailVente;
import com.catis.model.entity.OperationCaisse;

import pl.allegro.finance.tradukisto.ValueConverters;

public class EncaissementResponse {

    private OperationCaisse operationCaisse;
    private List<DetailVente> detailVentes;

    private String lang;
    private String moneyAsWords;

    public EncaissementResponse(OperationCaisse operationCaisse, List<DetailVente> detailVentes, String lang) {
        super();
        ValueConverters converter;
        if (lang.equalsIgnoreCase("fr")) {
            converter = ValueConverters.FRENCH_INTEGER;
        } else
            converter = ValueConverters.ENGLISH_INTEGER;

        this.operationCaisse = operationCaisse;

        this.detailVentes = detailVentes;
        this.moneyAsWords = converter.asWords((int) operationCaisse.getMontant());
    }

    public EncaissementResponse() {
        super();
        // TODO Auto-generated constructor stub
    }

    public OperationCaisse getOperationCaisse() {
        return operationCaisse;
    }

    public void setOperationCaisse(OperationCaisse operationCaisse) {
        this.operationCaisse = operationCaisse;
    }

    public List<DetailVente> getDetailVentes() {
        return detailVentes;
    }

    public void setDetailVentes(List<DetailVente> detailVentes) {
        this.detailVentes = detailVentes;
    }

    public String getMoneyAsWords() {
        return moneyAsWords;
    }

    public void setMoneyAsWords(String moneyAsWords) {
        this.moneyAsWords = moneyAsWords;
    }


}
