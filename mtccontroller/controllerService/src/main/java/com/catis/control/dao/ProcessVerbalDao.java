package com.catis.control.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catis.control.entities.VerbalProcess;

import java.util.UUID;

public interface ProcessVerbalDao  extends JpaRepository<VerbalProcess, UUID>{

}
