package com.catis.model;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class VerbalProcess extends JournalData {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String reference;

	private String signature; //qrc

	@OneToOne
	private Visite visite;
}