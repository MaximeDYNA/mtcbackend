package com.catis.control.dao;

import com.catis.control.entities.CategorieTestProduit;
import com.catis.control.entities.Formule;
import com.catis.control.entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CategorieTestProduitDao extends JpaRepository<CategorieTestProduit, UUID> {

    @Query(value = "select c from CategorieTestProduit c "+
            "join c.produit p join c.categorieTest ct where p.produitId = ?1 and ct.idCategorieTest = ?2"
    )
    List<CategorieTestProduit> getCatTestProduitsByIdProducts(UUID id, UUID pkid0);
}
