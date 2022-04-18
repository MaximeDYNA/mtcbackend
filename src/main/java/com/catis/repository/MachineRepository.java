package com.catis.repository;

import com.catis.model.entity.Machine;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface MachineRepository extends CrudRepository<Machine, UUID> {

    List<Machine> findByActiveStatusTrue();
}
