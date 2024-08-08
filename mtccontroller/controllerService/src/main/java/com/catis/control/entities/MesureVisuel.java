package com.catis.control.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.catis.control.entities.inherited.JournalData;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity @Table(name = "t_mesurevisuel")
@Getter @Setter @Audited
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class MesureVisuel extends JournalData {

	@Id
	@GeneratedValue(generator = "UUID")
	@Type(type="uuid-char")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID idMesureVisuel;
	
	private String heureDebut;
	private String heureFin;
	
	private String dateControl;
	
	private String plateNumber;
	
	@Column(columnDefinition = "LONGTEXT")
	private String image1;

	private String signature1;

	private UUID gieglanFileDeleted;

	@Column(columnDefinition = "LONGTEXT")
	private String image2;

	private String signature2;

	private String gps;

	@OneToOne
	private GieglanFile gieglanFile;
}
