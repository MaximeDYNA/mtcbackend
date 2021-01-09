package com.catis.model;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "t_lexiqueinspection")
@EntityListeners(AuditingEntityListener.class)
public class lexique_inspection {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	private int lexique_id;
	private int inspection_id;
	
	public lexique_inspection(int lexique_id,int inspection_id) {
		
		this.lexique_id = lexique_id;
		this.inspection_id = inspection_id;
	}
	
	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public int getLexique_id() {
		return lexique_id;
	}
	public void setLexique_id(int lexique_id) {
		this.lexique_id = lexique_id;
	}
	public int getInspection_id() {
		return inspection_id;
	}
	public void setInspection_id(int inspection_id) {
		this.inspection_id = inspection_id;
	}
	
	
}
