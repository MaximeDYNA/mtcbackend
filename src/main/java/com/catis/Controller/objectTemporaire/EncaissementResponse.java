package com.catis.Controller.objectTemporaire;

import java.util.List;

import com.catis.model.DetailVente;
import com.catis.model.OperationCaisse;

public class EncaissementResponse {

	private OperationCaisse operationCaisse;
	private List<DetailVente> detailVentes;
	
	public EncaissementResponse(OperationCaisse operationCaisse, List<DetailVente> detailVentes) {
		super();
		this.operationCaisse = operationCaisse;
		this.detailVentes = detailVentes;
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
	
}
