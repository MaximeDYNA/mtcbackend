package com.catis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catis.model.Control;

public interface ControlRepository extends JpaRepository<Control, Long> {

}