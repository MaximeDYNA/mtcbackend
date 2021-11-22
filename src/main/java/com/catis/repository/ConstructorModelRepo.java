package com.catis.repository;

import com.catis.model.entity.ConstructorModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ConstructorModelRepo extends CrudRepository<ConstructorModel, UUID> {

    List<ConstructorModel> findByActiveStatusTrue();

}
