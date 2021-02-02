package com.catis.repository;

import java.util.List;

import com.catis.model.SessionCaisse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.Hold;

public interface HoldRepository extends CrudRepository<Hold, Long> {

    void deleteByNumberAndSessionCaisse_SessionCaisseId(Long number, Long sessionCaisseId);

    @Query(value = "SELECT max(number) FROM Hold h where h.sessionCaisse = ?1")
    public Long max(SessionCaisse sessionCaisse);

    void deleteBySessionCaisse_SessionCaisseId(Long sessionCaisseId);

    List<Hold> findBySessionCaisse_SessionCaisseId(Long sessionCaisseId);

    Hold findByNumberAndSessionCaisse_SessionCaisseId(Long number, Long sessionCaisseId);
}
