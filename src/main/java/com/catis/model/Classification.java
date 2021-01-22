package com.catis.model;


import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

/**
 * @author AubryYvan
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Classification extends JournalData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String code;

	@OneToMany(mappedBy = "classification")

	private Set<Lexique> lexiques;


	public Classification() {
		// TODO Auto-generated constructor stub
	}

	public Classification(Long id, String code, Set<Lexique> lexiques) {
		this.id = id;
		this.code = code;
		this.lexiques = lexiques;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public Set<Lexique> getLexiques() {
		return lexiques;
	}

	public void setLexiques(Set<Lexique> lexiques) {
		this.lexiques = lexiques;
	}

}