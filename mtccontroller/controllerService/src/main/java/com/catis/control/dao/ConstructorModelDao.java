package com.catis.control.dao;

import com.catis.control.entities.CategorieTest;
import com.catis.control.entities.ConstructorModel;
import com.catis.control.entities.Ligne;
import com.catis.control.entities.Visite;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConstructorModelDao extends JpaRepository<ConstructorModel, UUID> {

    @Query(value = "select cm from ConstructorModel cm join cm.machines m "
        + "join m.ligneMachines lm join lm.ligne l "
        + "join m.categorieTestMachine ctm join ctm.categorieTest ct "
        + "where l = ?1 and ct.idCategorieTest = ?2"
    )
    Optional<ConstructorModel> getModelWithLigneAndIdCat(
        Ligne ligne,
        UUID catTestId
    );


}
