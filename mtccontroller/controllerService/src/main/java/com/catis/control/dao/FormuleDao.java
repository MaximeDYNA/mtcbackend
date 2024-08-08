package com.catis.control.dao;

import java.util.List;
import java.util.UUID;

import com.catis.control.entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catis.control.entities.Formule;

public interface FormuleDao extends JpaRepository<Formule, String> {

    @Query(value = "select distinct f from Formule f "+
        "join f.seuils s join s.produits p where p = ?1"
    )
    List<Formule> getFormuleByProducts(Produit produit);
}
