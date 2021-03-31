package com.catis.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.CarteGrise;
import org.springframework.data.repository.query.Param;

public interface CarteGriseRepository extends CrudRepository<CarteGrise, Long> {

    List<CarteGrise> findByNumImmatriculationIgnoreCaseOrVehicule_ChassisIgnoreCase(String immatriculation, String Chassis);

    @Query("select c from CarteGrise c " +
            "join fetch c.proprietaireVehicule p " +
            "join fetch p.partenaire part " +
            "where lower(c.numImmatriculation) LIKE lower(CONCAT('%',:imma,'%')) " +
            "and c.activeStatus = true")
    Optional<List<CarteGrise>> findCartegriseWithPartOfImma(@Param("imma") String imma);

    List<CarteGrise> findByVehicule_ChassisStartsWithIgnoreCase(String chassis);

    List<CarteGrise> findByVehicule_ChassisStartsWithIgnoreCaseOrderByCreatedDateDesc(String chassis, Pageable pageable);
}
