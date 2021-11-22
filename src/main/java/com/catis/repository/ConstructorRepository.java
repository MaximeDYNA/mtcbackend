package com.catis.repository;

import com.catis.model.entity.Constructor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ConstructorRepository extends CrudRepository<Constructor, UUID> {

    List<Constructor> findByActiveStatusTrue();
}
