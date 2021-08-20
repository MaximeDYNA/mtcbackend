package com.catis.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.Posales;

public interface PosaleRepository extends CrudRepository<Posales, Long> {

    List<Posales> findByHold_NumberAndSessionCaisse_SessionCaisseId(Long number, Long sessionCaisseId);

    List<Posales> findByHold_Number(Long number);

    List<Posales> findByStatusTrue();
    List<Posales> findByStatusTrueAndSessionCaisse_SessionCaisseId(Long sessionCaisseId);

    void deleteBySessionCaisse_SessionCaisseId(Long sessionCaisseId);

    List<Posales> findBySessionCaisse_SessionCaisseId(Long sessionCaisseId);

    void deleteByReferenceAndSessionCaisse_SessionCaisseId(String reference, Long sessionCaisseId);

    List<Posales> findByReferenceAndSessionCaisse_SessionCaisseId(String reference, Long sessionCaisseId);

    List<Posales> findByHold_HoldIdAndSessionCaisse_SessionCaisseIdAndProduit_ProduitId(Long holdId, Long sessionCaisseId, Long produitId);
}
