package com.catis.model.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

import java.util.UUID;

@Entity
@Table(name = "t_lignemachine")
@EntityListeners(AuditingEntityListener.class)
@Audited
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class LigneMachine extends JournalData {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID ligneMachine;
    @ManyToOne
    private Machine machine;

    @ManyToOne
    private Ligne ligne;


}
