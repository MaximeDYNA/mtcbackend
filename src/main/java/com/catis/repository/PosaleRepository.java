package com.catis.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Posales;

public interface PosaleRepository extends CrudRepository<Posales, UUID> {

    List<Posales> findByHold_NumberAndSessionCaisse_SessionCaisseId(Long number, UUID sessionCaisseId);

    List<Posales> findByHold_Number(Long number);

    List<Posales> findByStatusTrue();
    List<Posales> findByStatusTrueAndSessionCaisse_SessionCaisseId(UUID sessionCaisseId);

    void deleteBySessionCaisse_SessionCaisseId(UUID sessionCaisseId);

    List<Posales> findBySessionCaisse_SessionCaisseId(UUID sessionCaisseId);

    void deleteByReferenceAndSessionCaisse_SessionCaisseId(String reference, UUID sessionCaisseId);

    List<Posales> findByReferenceAndSessionCaisse_SessionCaisseId(String reference, UUID sessionCaisseId);

    List<Posales> findByHold_HoldIdAndSessionCaisse_SessionCaisseIdAndProduit_ProduitId(UUID holdId, UUID sessionCaisseId, UUID produitId);
}
