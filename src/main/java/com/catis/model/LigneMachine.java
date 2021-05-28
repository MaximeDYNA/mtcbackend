package com.catis.model;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catis.model.configuration.JournalData;

@Entity
@Table(name = "t_lignemachine")
@EntityListeners(AuditingEntityListener.class)
@Audited
public class LigneMachine extends JournalData {
    /*
     * table pivot qui nous permet de savoir quels sont les machines par lignes et
     * les lignes par machine
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ligneMachine;
    @ManyToOne
    private Machine machine;

    @ManyToOne
    private Ligne ligne;



    public LigneMachine() {

    }

    public LigneMachine(Long ligneMachine, Machine machine, Ligne ligne) {

        this.ligneMachine = ligneMachine;
        this.machine = machine;
        this.ligne = ligne;
    }

    public Long getLigneMachine() {
        return ligneMachine;
    }

    public void setLigneMachine(Long ligneMachine) {
        this.ligneMachine = ligneMachine;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public Ligne getLigne() {
        return ligne;
    }

    public void setLigne(Ligne ligne) {
        this.ligne = ligne;
    }



}
