package com.catis.control.dao;

import com.catis.control.entities.CategorieTestProduit;
import com.catis.control.entities.Mesure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MesureDao extends JpaRepository<Mesure, UUID> {

    @Query("select distinct m from Mesure m "+
            "join fetch m.categorieTestProduits p " +
            "where p = ?1")
    public List<Mesure> findMesureByCategorieTestProduitsId(CategorieTestProduit id);
}
