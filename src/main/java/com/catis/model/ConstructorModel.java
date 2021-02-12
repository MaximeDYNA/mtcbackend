package com.catis.model;

import com.catis.model.configuration.JournalData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConstructorModel extends JournalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    private Constructor constructor;

    @OneToMany(mappedBy = "constructorModel")
    private Set<Machine> machines;
}
