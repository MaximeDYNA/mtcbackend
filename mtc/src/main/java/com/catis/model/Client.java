package com.catis.model;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "t_client")
public class Client {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long clientId;
	private String description;

	@ManyToOne
	private Partenaire partenaire;
	
	@OneToMany(mappedBy="client")
	@JsonIgnore
	Set<Vente> ventes;
	
	public Client() {
	}	

	public Client(long clientId, String description, Partenaire partenaire) {
		super();
		this.clientId = clientId;
		this.description = description;
		this.partenaire = partenaire;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
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
