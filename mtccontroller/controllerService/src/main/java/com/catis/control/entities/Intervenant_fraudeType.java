package com.catis.control.entities;

import com.catis.control.entities.inherited.JournalData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_intervenant_fraudetype")
@Audited

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE t_intervenant_fraudetype SET active_status=false WHERE id=?")
public class Intervenant_fraudeType extends JournalData {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private double appreciation = 0.0;

    private double depreciation = 0.0;

    @ManyToOne(cascade = CascadeType.ALL)
    private FraudeType fraudeType;

    @ManyToOne(cascade = CascadeType.ALL)
    private IntervenantFraude intervenantFraude;

}
