package com.catis.model.entity;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_fraude_type")
@Audited

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE t_fraude_type SET active_status=false WHERE id=?")
public class FraudeType extends JournalData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String fraude;
    private String description;

    @OneToMany(mappedBy = "fraudeType")
    @JsonIgnore
    private Set<Intervenant_fraudeType> intervenant_fraudeTypes;
}
