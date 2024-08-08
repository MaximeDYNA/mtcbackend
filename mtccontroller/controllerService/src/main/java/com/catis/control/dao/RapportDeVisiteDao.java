package com.catis.control.dao;

import com.catis.control.dao.custom.RapportDeVisiteRepositoryCustom;
import com.catis.control.dto.ResultDto;
import com.catis.control.dto.TestDto;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.catis.control.entities.RapportDeVisite;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public interface RapportDeVisiteDao extends JpaRepository<RapportDeVisite, UUID> {

    @Query(value = "SELECT r.result, m.code FROM rapport_de_visite r " +
        "JOIN seuil s ON s.id = r.seuil_id JOIN formule f " +
        "ON f.id = s.formule_id JOIN t_mesure m ON m.formule_id = f.id " +
        "WHERE r.visite_id_visite = :id", nativeQuery = true)
    List<ResultDto> getResultWithCode(@Param("id") UUID id);

    /*@Query("SELECT r.result, m.code FROM RapportDeVisite r " +
        "join fetch r.seuil s join fetch s.formule f join fetch " +
        "f.mesures m join r.visite v where v.idVisite = ?1"
    )
    Map<String, String> getResultVisiteId(Long id);*/

    default Map<String, String> getResultVisiteIdMap(UUID id) {
        return getResultWithCode(id).stream()
            .collect(Collectors.toMap(
                test -> test.getCode(),
                test -> test.getResult()
            )
        );
    }
}