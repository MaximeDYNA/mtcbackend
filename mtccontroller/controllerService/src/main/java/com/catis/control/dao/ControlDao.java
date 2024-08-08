package com.catis.control.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catis.control.entities.Control;

import java.util.UUID;

public interface ControlDao extends JpaRepository<Control, UUID> {

}
