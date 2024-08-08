package com.catis.control.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catis.control.entities.GieglanFile;

import java.util.UUID;

public interface GieglanFileDao extends JpaRepository<GieglanFile, UUID> { }