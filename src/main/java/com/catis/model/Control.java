package com.catis.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Control extends JournalData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Enumerated(EnumType.STRING)
	private StatusType status;

	private LocalDateTime contreVDelayAt;

	private LocalDateTime validityAt;

	@ManyToOne
	private CarteGrise carteGrise;

	@OneToMany(mappedBy = "control")
	private List<Visite> visites = new ArrayList<>();
	
	public enum StatusType {
	    INITIALIZED, REJECTED, VALIDATED
	}

	public Control(long id, StatusType status, LocalDateTime contreVDelayAt, LocalDateTime validityAt,
			CarteGrise carteGrise, List<Visite> visites) {
		super();
		this.id = id;
		this.status = status;
		this.contreVDelayAt = contreVDelayAt;
		this.validityAt = validityAt;
		this.carteGrise = carteGrise;
		this.visites = visites;
	}

	public Control() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public StatusType getStatus() {
		return status;
	}

	public void setStatus(StatusType status) {
		this.status = status;
	}

	public LocalDateTime getContreVDelayAt() {
		return contreVDelayAt;
	}

	public void setContreVDelayAt(LocalDateTime contreVDelayAt) {
		this.contreVDelayAt = contreVDelayAt;
	}

	public LocalDateTime getValidityAt() {
		return validityAt;
	}

	public void setValidityAt(LocalDateTime validityAt) {
		this.validityAt = validityAt;
	}

	public CarteGrise getCarteGrise() {
		return carteGrise;
	}

	public void setCarteGrise(CarteGrise carteGrise) {
		this.carteGrise = carteGrise;
	}

	public List<Visite> getVisites() {
		return visites;
	}

	public void setVisites(List<Visite> visites) {
		this.visites = visites;
	}
	
	
}