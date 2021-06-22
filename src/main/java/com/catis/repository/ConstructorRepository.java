package com.catis.repository;

import com.catis.model.Constructor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ConstructorRepository extends CrudRepository<Constructor, Long> {

    List<Constructor> findByActiveStatusTrue();
}
