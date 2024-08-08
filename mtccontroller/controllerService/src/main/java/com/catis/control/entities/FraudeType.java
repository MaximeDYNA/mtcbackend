package com.catis.control.entities;

import com.catis.control.entities.inherited.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.Set;
import java.util.UUID;

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
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String code;
    private String fraude;
    private String description;

    @OneToMany(mappedBy = "fraudeType")
    @JsonIgnore
    private Set<Intervenant_fraudeType> intervenant_fraudeTypes;
}
