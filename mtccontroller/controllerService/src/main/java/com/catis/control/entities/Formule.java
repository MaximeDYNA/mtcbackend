package com.catis.control.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.control.entities.inherited.JournalData;

import lombok.*;

@Entity @Getter @Setter @Audited
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Formule extends JournalData {

	@Id
	@Column(name = "id", updatable = false, nullable = false)
	private String id;

	private String description;
	
	@OneToMany(mappedBy = "formule")
	private Set<Mesure> mesures = new HashSet<>();

	@OneToMany(mappedBy = "formule", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Seuil> seuils = new HashSet<>();
}
