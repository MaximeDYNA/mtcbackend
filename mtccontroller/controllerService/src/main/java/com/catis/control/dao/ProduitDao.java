package com.catis.control.dao;

import com.catis.control.entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProduitDao extends JpaRepository<Produit, UUID> {
}
