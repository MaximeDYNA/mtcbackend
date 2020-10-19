package com.catis.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_client")
public class Client {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name="id_client")
	private String idClient;
	private String description;
	
	private String idorganisation;
	
	@ManyToOne
	@JoinColumn(name="idPartenaire")
	private Partenaire partenaire;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="client")
	@JsonIgnore
	private Set<Vente> ventes;
	
	
	public Client() {
	}	

	

	



	public Client(String idClient, String description, String idorganisation, Partenaire partenaire,
			Set<Vente> ventes) {
		super();
		this.idClient = idClient;
		this.description = description;
		this.idorganisation = idorganisation;
		this.partenaire = partenaire;
		this.ventes = ventes;
	}







	public String getIdClient() {
		return idClient;
	}

	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

	


	public String getIdorganisation() {
		return idorganisation;
	}







	public void setIdorganisation(String idorganisation) {
		this.idorganisation = idorganisation;
	}







	public Partenaire getPartenaire() {
		return partenaire;
	}

	public void setPartenaire(Partenaire partenaire) {
		this.partenaire = partenaire;
	}

	public Set<Vente> getVentes() {
		return ventes;
	}

	public void setVentes(Set<Vente> ventes) {
		this.ventes = ventes;
	}
	
}
