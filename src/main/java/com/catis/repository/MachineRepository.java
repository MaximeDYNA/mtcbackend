package com.catis.repository;

import com.catis.model.entity.Machine;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MachineRepository extends CrudRepository<Machine, Long> {

    List<Machine> findByActiveStatusTrue();
}
