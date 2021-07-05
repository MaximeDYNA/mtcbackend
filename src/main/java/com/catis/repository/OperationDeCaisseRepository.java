package com.catis.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.OperationCaisse;

public interface OperationDeCaisseRepository extends CrudRepository<OperationCaisse, String> {

    OperationCaisse findByNumeroTicket(String ticket);

    List<OperationCaisse> findByType(int type);

    List<OperationCaisse> findByVente_IdVente(Long idVente);

    List<OperationCaisse> findByTypeTrueAndVente_IdVente(Long idVente);

    List<OperationCaisse> findBySessionCaisse_SessionCaisseId(Long sessionCaisseId);

}
