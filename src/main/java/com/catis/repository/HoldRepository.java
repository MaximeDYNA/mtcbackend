package com.catis.repository;

import java.util.List;
import java.util.UUID;

import com.catis.model.entity.SessionCaisse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Hold;

public interface HoldRepository extends CrudRepository<Hold, UUID> {

    void deleteByNumberAndSessionCaisse_SessionCaisseId(Long number, UUID sessionCaisseId);

    @Query(value = "SELECT max(number) FROM Hold h where h.sessionCaisse = ?1")
    Long max(SessionCaisse sessionCaisse);

    void deleteBySessionCaisse_SessionCaisseId(UUID sessionCaisseId);

    List<Hold> findBySessionCaisse_SessionCaisseId(UUID sessionCaisseId);

    Hold findByNumberAndSessionCaisse_SessionCaisseId(Long number, UUID sessionCaisseId);
}
