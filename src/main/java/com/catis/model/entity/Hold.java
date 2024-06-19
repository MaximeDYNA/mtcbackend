package com.catis.model.entity;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "t_hold")
@EntityListeners(AuditingEntityListener.class)
@Audited
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Hold extends JournalData {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID holdId;

    private Long number;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private SessionCaisse sessionCaisse;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hold")
    private Set<Posales> posales;



    @JsonIgnore
    public SessionCaisse getSessionCaisse() {
        return sessionCaisse;
    }


    @JsonIgnore
    public Set<Posales> getPosales() {
        return posales;
    }


}
