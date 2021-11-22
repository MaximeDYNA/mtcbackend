package com.catis.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.OperationCaisse;

public interface OperationDeCaisseRepository extends CrudRepository<OperationCaisse, UUID> {

    OperationCaisse findByNumeroTicket(String ticket);

    List<OperationCaisse> findByType(int type);

    List<OperationCaisse> findByVente_IdVente(UUID idVente);

    List<OperationCaisse> findByTypeTrueAndVente_IdVente(UUID idVente);

    List<OperationCaisse> findBySessionCaisse_SessionCaisseId(UUID sessionCaisseId);

}
