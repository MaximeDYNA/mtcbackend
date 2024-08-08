package com.catis.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.catis.model.entity.CarteGrise;
import com.catis.repository.nativeQueries.CarteGriseCustomRepository;

import org.springframework.data.repository.query.Param;

public interface CarteGriseRepository extends CrudRepository<CarteGrise, UUID>, CarteGriseCustomRepository {

    List<CarteGrise> findByNumImmatriculationIgnoreCaseOrVehicule_ChassisIgnoreCase(String immatriculation, String Chassis);

    @Query("select c from CarteGrise c " +
            "join fetch c.proprietaireVehicule p " +
            "join fetch p.partenaire part " +
            "where lower(c.numImmatriculation) LIKE lower(CONCAT('%',:imma,'%')) " +
            "and c.activeStatus = true")
    Optional<List<CarteGrise>> findCartegriseWithPartOfImma(@Param("imma") String imma);

    List<CarteGrise> findByVehicule_ChassisStartsWithIgnoreCase(String chassis);

    CarteGrise findByNumImmatriculationIgnoreCase(String imma);

    List<CarteGrise> findByVehicule_ChassisStartsWithIgnoreCaseOrderByCreatedDateDesc(String chassis, Pageable pageable);

    @Query("select c from CarteGrise c " +
            "join fetch c.controls con " +
            "where con.activeStatus = true " +
            "and con.status = 'VALIDATED' " +
            "and con.validityAt >= current_date " +
            "and c.numImmatriculation = ?1 " +
            "order by con.createdDate desc")
    Optional<CarteGrise> findCGWithOrderedValidControl(String immatriculation);

    List<CarteGrise> findByActiveStatusTrue();

    List<CarteGrise> findByActiveStatusTrue(Pageable pageable);
}
