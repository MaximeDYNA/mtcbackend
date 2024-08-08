package com.catis.control.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.catis.control.entities.Seuil;

import java.util.UUID;

@Repository
public interface SeuilDao extends JpaRepository<Seuil, String>{

}
