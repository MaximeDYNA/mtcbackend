package com.catis.model.entity;

import com.catis.model.configuration.JournalData;
import com.catis.model.entity.FraudeType;
import com.catis.model.entity.IntervenantFraude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double appreciation = 0.0;

    private double depreciation = 0.0;

    @ManyToOne(cascade = CascadeType.ALL)
    private FraudeType fraudeType;

    @ManyToOne(cascade = CascadeType.ALL)
    private IntervenantFraude intervenantFraude;

}
