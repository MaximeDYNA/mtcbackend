package com.catis.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.catis.objectTemporaire.GraphView;

@Entity
@Table(name="t_visite")
@EntityListeners(AuditingEntityListener.class)
public class Visite extends JournalData {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idVisite;
	private boolean contreVisite;
	private LocalDateTime dateDebut;
	private LocalDateTime dateFin;
	private int statut;
	private String idOrganisation;

	@Column(columnDefinition = "bit default 1")
	private boolean encours;
	
	@ManyToOne
	@JoinColumn(name="idCaissier")
	private Caissier caissier;
	
	@OneToOne(mappedBy = "visite", cascade = CascadeType.ALL)
	private Inspection inspection;
	
	@OneToOne(mappedBy = "visite")
	private VerbalProcess process;
	
	@ManyToOne
	private CarteGrise carteGrise;
	
	public List<RapportDeVisite> getRapportDeVisites() {
		return rapportDeVisites;
	}

	public void setRapportDeVisites(List<RapportDeVisite> rapportDeVisites) {
		this.rapportDeVisites = rapportDeVisites;
	}
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "visite")
	private List<RapportDeVisite> rapportDeVisites;

	public Visite() {
	
	}

	public Visite(Long idVisite, boolean contreVisite, LocalDateTime dateDebut, LocalDateTime dateFin, int statut,
			String idOrganisation, Caissier caissier, CarteGrise carteGrise) {
		super();
		this.idVisite = idVisite;
		this.contreVisite = contreVisite;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.statut = statut;
		this.idOrganisation = idOrganisation;
		this.caissier = caissier;
		this.carteGrise = carteGrise;
	}



	public boolean isContreVisite() {
		return contreVisite;
	}









	public void setContreVisite(boolean contreVisite) {
		this.contreVisite = contreVisite;
	}









	public Long getIdVisite() {
		return idVisite;
	}



	public void setIdVisite(Long idVisite) {
		this.idVisite = idVisite;
	}



	public LocalDateTime getDateDebut() {
		return dateDebut;
	}


	public void setDateDebut(LocalDateTime dateDebut) {
		this.dateDebut = dateDebut;
	}


	public LocalDateTime getDateFin() {
		return dateFin;
	}


	public void setDateFin(LocalDateTime dateFin) {
		this.dateFin = dateFin;
	}


	public int getStatut() {
		return statut;
	}

	public void setStatut(int statut) {
		this.statut = statut;
	}

	public String getIdOrganisation() {
		return idOrganisation;
	}

	public void setIdOrganisation(String idOrganisation) {
		this.idOrganisation = idOrganisation;
	}

	public Caissier getCaissier() {
		return caissier;
	}

	public void setCaissier(Caissier caissier) {
		this.caissier = caissier;
	}

	public CarteGrise getCarteGrise() {
		return carteGrise;
	}

	public void setCarteGrise(CarteGrise carteGrise) {
		this.carteGrise = carteGrise;
	}


	public boolean isEncours() {
		return encours;
	}


	public void setEncours(boolean encours) {
		this.encours = encours;
	}
	
	public String statutRender(int code) {
		if(code==0) {
			return "maj";
		}
		else if(code==1) {
			return "A inspecter";
		}
		else if(code==2) {
			return "En cours test";
		}
		else if(code==3) {
			return "A signer";
		}
		else if(code==4) {
			return "A imprimer";
		}
		else if(code==5) {
			return "A enregister";
		}
		else if(code==6) {
			return "A certifier";
		}
		else if(code==7) {
			return "Accepté";
		}
		else if(code==8) {
			return "Refusé";
		}
		else if(code==9) {
			return "A approuver";
		}
		else {
			return "erreur";
		}
		
	}
	public String typeRender() {
		if(this.contreVisite) {
			return "CV";
		}
		else
			return "VTP";
	}
}
