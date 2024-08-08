package com.catis.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.catis.dtoprojections.VehiculeDTO;
import com.catis.model.entity.Vehicule;
import com.catis.repository.nativeQueries.VehiculeCustomRepository;

public interface VehiculeRepository extends CrudRepository<Vehicule, UUID>,VehiculeCustomRepository {

    List<Vehicule> findByChassisStartsWithIgnoreCase(String chassis);
    List<Vehicule> findByActiveStatusTrue();
    List<Vehicule> findByActiveStatusTrue(Pageable pageable);

    // flemming implimented
    @Query("SELECT v FROM Vehicule v LEFT JOIN FETCH v.organisation WHERE v.activeStatus = true AND v.chassis LIKE ?1%")
    List<Vehicule> findByActiveStatusTrueAndChassisStartsWithIgnoreCase(String chassis, Pageable pageable);
  
    // flemming implimented 
    @Query("SELECT v.vehiculeId AS vehiculeId, v.chassis AS chassis, COALESCE(o.nom, 'Toutes') AS organisationNom " +
       "FROM Vehicule v LEFT JOIN v.organisation o " +
       "WHERE v.activeStatus = true AND v.chassis LIKE ?1%")
    List<VehiculeDTO> findByActiveStatusTrueAndChassisStartsWithIgnoreCaseDTO(String chassis, Pageable pageable);

}
