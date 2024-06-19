package com.catis.model.entity;

import com.catis.model.configuration.JournalData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Audited
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE constructor_model SET active_status=false WHERE id=?")
public class ConstructorModel extends JournalData {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Constructor constructor;

    @OneToMany(mappedBy = "constructorModel",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Machine> machines;
}
