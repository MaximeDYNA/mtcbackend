package com.catis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catis.model.control.Control;

import java.util.UUID;

public interface ControlRepository extends JpaRepository<Control, UUID> {

}
