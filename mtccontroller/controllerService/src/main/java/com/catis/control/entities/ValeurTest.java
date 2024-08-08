package com.catis.control.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.control.entities.GieglanFile.StatusType;
import com.catis.control.entities.inherited.JournalData;

import lombok.*;

import java.util.UUID;

@Entity @Table(name = "t_valeurtest")
@Getter @Setter @Audited
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ValeurTest extends JournalData {

	@Id
	@GeneratedValue(generator = "UUID")
	@Type(type="uuid-char")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID idValeurTest;

	private String code;

	private String valeur;

	private Integer crc;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(255) default 'INITIALIZED'")
	private StatusType status;

	private String description;

	@ManyToOne
	private Mesure mesure;

	@ManyToOne
	private GieglanFile gieglanFile;
}