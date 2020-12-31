package com.catis.model;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_caissiercaisse")
public class CaissierCaisse extends JournalData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long caissier_caisse_id;

	@ManyToOne
	private Organisation organisation;

	@ManyToOne(fetch = FetchType.LAZY)
	private Caisse caisse;

	@ManyToOne
	private Caissier caissier;

	public CaissierCaisse() {

	}

	public CaissierCaisse(Long caissier_caisse_id, Organisation organisation, Caisse caisse, Caissier caissier) {

		this.caissier_caisse_id = caissier_caisse_id;
		this.organisation = organisation;
		this.caisse = caisse;
		this.caissier = caissier;
	}

	public Long getCaissier_caisse_id() {
		return caissier_caisse_id;
	}

	public void setCaissier_caisse_id(Long caissier_caisse_id) {
		this.caissier_caisse_id = caissier_caisse_id;
	}

	public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	public Caisse getCaisse() {
		return caisse;
	}

	public void setCaisse(Caisse caisse) {
		this.caisse = caisse;
	}

	public Caissier getCaissier() {
		return caissier;
	}

	public void setCaissier(Caissier caissier) {
		this.caissier = caissier;
	}

}
