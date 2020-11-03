package com.catis.model;



import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "t_divisionpays")
public class DivisionPays {
	@Id
	private String idDivisionPays;
	
	@ManyToOne
	@JoinColumn(name="idPays")
	private Pays Pays;
	private String libelle;
	private String description;
	
	
	private String idDivisionPaysParent;

	public DivisionPays() {
		// TODO Auto-generated constructor stub
	}



	public DivisionPays(String idDivisionPays, com.catis.model.Pays pays, String libelle, String description,
			String idDivisionPaysParent) {
		super();
		this.idDivisionPays = idDivisionPays;
		Pays = pays;
		this.libelle = libelle;
		this.description = description;
		this.idDivisionPaysParent = idDivisionPaysParent;
	}





	public String getIdDivisionPays() {
		return idDivisionPays;
	}

	public void setIdDivisionPays(String idDivisionPays) {
		this.idDivisionPays = idDivisionPays;
	}


	public Pays getPays() {
		return Pays;
	}


	public void setPays(Pays pays) {
		Pays = pays;
	}




	public String getIdDivisionPaysParent() {
		return idDivisionPaysParent;
	}



	public void setIdDivisionPaysParent(String idDivisionPaysParent) {
		this.idDivisionPaysParent = idDivisionPaysParent;
	}



	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


}
