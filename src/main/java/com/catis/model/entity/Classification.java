package com.catis.model.entity;


import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

/**
 * @author AubryYvan
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Classification extends JournalData {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String code;

    @OneToMany(mappedBy = "classification",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Lexique> lexiques;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Classification)) return false;
        Classification that = (Classification) o;
        return Objects.equals(getId(), that.getId());
    }

}