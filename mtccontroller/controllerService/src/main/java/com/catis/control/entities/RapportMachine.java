package com.catis.control.entities;

import com.catis.control.entities.inherited.JournalData;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter @Setter @Audited
@AllArgsConstructor @NoArgsConstructor
public class RapportMachine extends JournalData {

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

    @OneToMany(mappedBy = "rapportMachine")
    private Set<CategorieTestMachine> categorieTestMachines;
}
