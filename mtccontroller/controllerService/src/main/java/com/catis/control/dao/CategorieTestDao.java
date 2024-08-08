package com.catis.control.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catis.control.entities.CategorieTest;

public interface CategorieTestDao extends JpaRepository<CategorieTest, UUID>{

	Optional<CategorieTest> findByLibelleIgnoreCase(String extension);
}
