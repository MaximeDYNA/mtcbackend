package com.catis.control.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catis.control.entities.Machine;

public interface MachineDao extends JpaRepository<Machine, UUID> {
	
	Optional<Machine> findByNumSerie(String numSerie);
}
